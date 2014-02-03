package persistence

/**
 * Created by i-django on 03/06/13.
 */
import org.mybatis.scala.mapping._
import models._

object UserStore {
  val search = new SelectListBy[String,User] {
    def xsql = <xsql>
      SELECT *
      FROM user
      WHERE
      lower(firstname) LIKE lower(#{{value}})
      OR lower(lastname) LIKE lower(#{{value}})
      ORDER BY lastname
    </xsql>
  }

  val findById = new SelectOneBy[Long,User] {
    def xsql = <xsql>
      SELECT *
      FROM user
      WHERE id = #{{id}}
    </xsql>
  }

  val update = new Update[User] {
    def xsql = <xsql>
      UPDATE user
      SET firstname = #{{firstname}}, lastname = #{{lastname}}, email= #{{email}}, passhash= #{{passhash}}, birthday= #{{birthday}}, gender= #{{gender}}, mobilephone= #{{mobilephone}}, country = #{{country}}
      WHERE id = #{{id}}
    </xsql>
  }

  val insert = new Insert[User] {
    def xsql = <xsql>
      INSERT INTO contact (firstname, lastname, email, passhash, birthday, gender, mobilephone, country )
      VALUES (#{{firstname}}, #{{lastname}}, #{{email}}, #{{passhash}}, #{{birthday}}, #{{gender}}, #{{mobilephone}}, #{{country}})
    </xsql>
  }

  val delete = new Delete[Long] {
    def xsql = <xsql>DELETE FROM user WHERE id = #{{id}}</xsql>
  }

  def bind = Seq(search, findById, update, insert, delete)

}
