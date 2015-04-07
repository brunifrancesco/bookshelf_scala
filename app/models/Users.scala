package models

import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._
import java.security.MessageDigest


case class User(username: String, email :String, var password: String){
  
  def setPassword(clear_password : String){
    password = clear_password + "-"+username
  }
}

object JsonFormats {
  implicit val postFormat = Json.format[User]
}
