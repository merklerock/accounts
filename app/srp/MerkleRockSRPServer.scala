package srp
import net.fwbrasil.activate.ActivateContext
import com.github.mauricio.async.db.Configuration
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import net.fwbrasil.activate.storage.relational.async.AsyncPostgreSQLStorage

import com.merklerock.common.SRPServer
import java.io._

case class AccountData(accountName:String, s:Array[Byte], v:Array[Byte])

object asyncPostgreSQLContext extends ActivateContext {
    val storage = new AsyncPostgreSQLStorage {
        def configuration =
            new Configuration(
                username = "user",
                host = "localhost",
                password = Some("password"),
                database = Some("database_name"))
        lazy val objectFactory = new PostgreSQLConnectionFactory(configuration)
    }
}

/**
 * Need this class to update the saved data file. 
 */
class AppendableObjectOutputStream(out:OutputStream) extends ObjectOutputStream(out) {
  
  @Override
  override def writeStreamHeader():Unit = {
    // do not write a header
  }
  
}

/**
 * The class provides a sample implementation of SRPServer where the data is saved in database,
 * and the session details are hold in couchbase.
 */
object MerkleRockSRPServer extends SRPServer{

  var cache:Map[String, AccountData] = Map() 
  /**
   * Saves the account credentials provided in file.
   */
  override def save(accountName:String, s:Array[Byte], v:Array[Byte]):Unit = {
    val file = new File("account.data");
    def fileOs = new FileOutputStream(file.getName(),true);
    if(!hasAccount(accountName)){
	  var out:ObjectOutputStream = null
      if(file.exists()){
        out = new AppendableObjectOutputStream(fileOs);
      }else{
        out = new ObjectOutputStream(fileOs);
      }
	  out.writeObject(AccountData(accountName,s,v))
	  out.flush();
      out.close();
    }
    
  }
  
  /**
   * Finder method to get the s and v for the given account with accountName.
   * This implementation reads the serialized objects from the file.
   */
  override def findSV(accountName:String):Option[Tuple2[Array[Byte], Array[Byte]]] = {
	cache.get(accountName).orElse[AccountData]{
	  val file = new File("account.data");
	  if(file.exists()){
		  val fileIs = new FileInputStream(file.getName());
		  val in = new ObjectInputStream(fileIs);
		  var ud:Object = null
		  var flag:Boolean = false
		  var result:Boolean = false
		  while(flag == false){
		    try{
		    	ud = in.readObject();
			    if(ud.asInstanceOf[AccountData].accountName == accountName){
				  cache += (ud.asInstanceOf[AccountData].accountName -> ud.asInstanceOf[AccountData])
				  result = true
				  flag = true
			    }
		    }catch{
		      case e:EOFException =>
		        flag=true
		    }
		  }
		  in.close();
		  if(result)Some(ud.asInstanceOf[AccountData]) else None
	  }else{
	      None
	  }
	}.map(data => Some(data.s, data.v)).getOrElse(None)	
  }
  
  def hasAccount(accountName:String) = {
      import scala.util.control.Exception._
	  catching(classOf[FileNotFoundException],classOf[EOFException]).opt(findSV(accountName).isDefined).getOrElse(false)
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