package srp

import com.merklerock.common.SRP.SRPServer
import models._

case class AccountData(accountName:String, s:Array[Byte], v:Array[Byte])

/**
 * The class provides an implementation of SRPServer where the data is saved in database,
 * and the session details are hold in couchbase/cache/memory.
 * 
 */
object MerklerockSRPServer extends SRPServer{

  var cache:Map[String, AccountData] = Map() 
  /**
   * Saves the account credentials provided in database.
   * create an otp
   * define status => 0 deactivated | 1 authorized | 2 suspended
   * define role => user | admin
   */
  def save(accountName:String, accountPassword:String, accountToS:String):Unit = {
    if(!hasAccount(accountName)){
      val accountCredential = getUserCredentials(userName, accountPassword)
      val otp = Account.generateOTP
      new Account(accountName, accountCredential.sVal, accountCredential.vVal, otp , None, accountToS, 0, "user", None)
      otp
    }  else None
  }
  
  /**
   * Finder method to get the s and v for the given account with accountName.
   * This implementation reads the serialized objects from the file.
   */
  override def findSV(accountName:String):Option[Tuple2[Array[Byte], Array[Byte]]] = {
  	cache.get(accountName).orElse[AccountData]{
      if (Account.getAccountByXnym(accountName).xnym == accountName) {
      cache += (accountName -> new AccountData(accountName,Account.getAccountByXnym(accountName).s,Account.getAccountByXnym(accountName).v))
      result = true
      }
      if(result)Some(Account.getAccountByXnym(accountName)) else None
  	}.map(data => Some(data.s, data.v)).getOrElse(None)	
  }
  
  def hasAccount(accountName:String) = {
      //Check entity related exceptions
      import scala.util.control.Exception._
	  catching(classOf[Exception]).opt(findSV(accountName).isDefined).getOrElse(false)
  }
  
  /**
   * Provides a way to save sessionid and the associated accountname.
   */
  var currentSession:Map[String,(String,String)] = Map()
  def saveSession(accountName:String, sessionId:String, hsessionId:String):Unit = {
    currentSession += (accountName -> (sessionId,hsessionId))
  }
  
  def validSession(accountName:String, inSessionId:String):Boolean = {
    val (s,h) = currentSession.getOrElse(accountName, ("",""))
    s.equals(inSessionId)
  }
  
  def validateSessionHash(accountName:String, inhSessionId:String):Boolean = {
    val (s,h) = currentSession.getOrElse(accountName, ("",""))
    BigInt(h,16).equals(BigInt(inhSessionId,16))
  }
  
  def getSession(accountName:String) = {
    val (s,h) = currentSession.getOrElse(accountName, ("",""))
    s
  }
  
  def getSessionWithHash(accountName:String) = {
    val (s,h) = currentSession.getOrElse(accountName, ("",""))
    (s,h)
  }  
}