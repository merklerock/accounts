package models

/**
 * Created by i-django on 02/06/13.
 */

class Account {
  var id : Long = _
  var firstname : String = _
  var lastname : String = _
  var email : String = _
  var passhash : String = _
  var birthday : String = _
  var gender : String = _
  var mobilephone : String = _
  var country : String = _
}

object Account {

  def apply(id: Option[Long], firstname: String, lastname: String, email: String, passhash: String, birthday: String, gender: String, mobilephone: String, country: String) = {
    val c = new User
    c.id = id.getOrElse(0)
    c.firstname = firstname
    c.lastname = lastname
    c.email = email
    c.passhash = passhash
    c.birthday = birthday
    c.gender = gender
    c.mobilephone = mobilephone
    c.country = country
    c
  }

  def unapply(c : User) = Some((Option(c.id), c.firstname, c.lastname, c.email, c.passhash, c.birthday, c.gender, c.mobilephone, c.country))

}