package controllers

import play.api._
import play.api.mvc._
import data.Form
import data.Forms._
import libs.json.Json

object Application extends Controller with Secured {

	def check(mail: String, password: String) = mail == "tony@stark.com" && password == "ironman"

	val loginForm = Form(
	    tuple(
	      "mail" -> text,
	      "password" -> text
	    ) verifying ("Invalid email or password", result => result match {
	      case (email, password) => check(email, password)
	    })
	)  

	def protectedResource = IsAuthenticated{
	    username => _ =>

	    Ok(Json.obj("test"->"1234"))
  	}

  	def authenticate = Action { implicit request =>
	    loginForm.bindFromRequest.fold(
	      formWithErrors => Forbidden,
	      user => {
	        Ok.withSession("mail" -> user._1)
	      }
	    )
	}

	def logout = Action{
	    Ok.withNewSession
	}

  	def index = Action {
   		Ok(views.html.index("Your new application is ready."))
  	}

	val signupForm = Form(
	    tuple(
	      "xnym" -> text verifying pattern("/^[13n][1-9A-Za-z][^OIl]{20,40}/".r, error="A valid XPM Address is required"),
	      "password" -> nonEmptyText(minLength = 8, maxLenght = 64),
	      "tos" -> nonEmptyText
	    )
	) 

	/**
	 * request for a new Account
	 * The input username and password are used to save 
	 * the credentials by MerklerockSRPServer.
	 * http://blog.knoldus.com/2013/01/13/design-forms-in-play2-0-using-scala-and-mongodb/
	 */
	def signup = Action.async { implicit request =>
		asyncTransactionalChain { implicit ctx =>
			signupForm.bindFromRequest.fold(
				
				errors => BadRequest(views.html.signUpForm(errors, "There is some error")),
				
				signupForm => {
				    Accounts.getAccountbyXnym(signup.xnym).isEmpty match {
				      
				      case false =>
				        Ok(views.html.signup(Application.signupForm, "This account is already registered"))
				      
				      case true =>
				      	//persist the new account using SRP protocol
				        val otp = MerklerockSRPServer.save(signup.xnym,signup.password,signup.tos)
				        Ok(views.html.registerDetail(signup.xnym,otp))
				    }

				}
			)
		}

	}


	val signupData = signupForm.bindFromRequest.get
  
}

/**
 * Provide security features
 */
trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("mail")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Unauthorized

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized){
      user => Action(request => f(user)(request))
    }


}