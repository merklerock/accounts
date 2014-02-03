// @SOURCE:C:/Users/i-django/IdeaProjects/account/conf/routes
// @HASH:22782e8176ebd01f1405be2a1b52876bf0cbd53a
// @DATE:Tue Jan 28 23:24:21 CET 2014

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:14
// @LINE:13
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:6
package controllers {

// @LINE:14
// @LINE:13
// @LINE:6
class ReverseAssets {
    

// @LINE:14
// @LINE:13
// @LINE:6
def at(file:String): Call = {
   (file: @unchecked) match {
// @LINE:6
case (file) if file == "index.html" => Call("GET", _prefix)
                                                        
// @LINE:13
case (file) if true => Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
                                                        
// @LINE:14
case (file) if true => Call("GET", _prefix + { _defaultPrefix } + "public/" + implicitly[PathBindable[String]].unbind("file", file))
                                                        
   }
}
                                                
    
}
                          

// @LINE:10
// @LINE:9
// @LINE:8
class ReverseApplication {
    

// @LINE:9
def authenticate(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "login")
}
                                                

// @LINE:10
def logout(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "logout")
}
                                                

// @LINE:8
def protectedResource(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "protectedResource")
}
                                                
    
}
                          
}
                  


// @LINE:14
// @LINE:13
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:6
package controllers.javascript {

// @LINE:14
// @LINE:13
// @LINE:6
class ReverseAssets {
    

// @LINE:14
// @LINE:13
// @LINE:6
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      if (file == """ + implicitly[JavascriptLitteral[String]].to("index.html") + """) {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
      if (true) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
      if (true) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "public/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
      }
   """
)
                        
    
}
              

// @LINE:10
// @LINE:9
// @LINE:8
class ReverseApplication {
    

// @LINE:9
def authenticate : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.authenticate",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "login"})
      }
   """
)
                        

// @LINE:10
def logout : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.logout",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "logout"})
      }
   """
)
                        

// @LINE:8
def protectedResource : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.protectedResource",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "protectedResource"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:14
// @LINE:13
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:6
package controllers.ref {

// @LINE:14
// @LINE:13
// @LINE:6
class ReverseAssets {
    

// @LINE:6
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Home page""", _prefix + """""")
)
                      
    
}
                          

// @LINE:10
// @LINE:9
// @LINE:8
class ReverseApplication {
    

// @LINE:9
def authenticate(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.authenticate(), HandlerDef(this, "controllers.Application", "authenticate", Seq(), "POST", """""", _prefix + """login""")
)
                      

// @LINE:10
def logout(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.logout(), HandlerDef(this, "controllers.Application", "logout", Seq(), "GET", """""", _prefix + """logout""")
)
                      

// @LINE:8
def protectedResource(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.protectedResource(), HandlerDef(this, "controllers.Application", "protectedResource", Seq(), "GET", """""", _prefix + """protectedResource""")
)
                      
    
}
                          
}
                  
      