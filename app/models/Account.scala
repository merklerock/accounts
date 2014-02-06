package models

import accountPersistenceContext._
import net.fwbrasil.activate.play.EntityForm
import net.fwbrasil.radon.transaction.TransactionalExecutionContext
import java.util.Date
import scala.concurrent.Future


/**
 * Created by i-django on 02/06/13.
 */

class Account(
  var xnym: String,
  var s: Array[Byte],
  var v: Array[Byte],
  var otp : Short,
  var base58key : Option[String],
  var tos : Boolean,
  var status : Short,
  var role : String,
  var extended : Option[String])
      extends Entity

object Account {

  def getAccountByXnym(xnym: String) = 
    query {
      (account: Account) => 
        where(account.xnym :== xnym) select (account) orderBy (account.xnym) limit(1)
    }
  
}

}