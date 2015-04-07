package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.Future
import play.api.libs.functional.syntax._
import authentikat.jwt._
import reactivemongo.api.Cursor
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import models._
import models.JsonFormats._
import play.api.libs.concurrent.Execution.Implicits._
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac
import io.really.jwt._
import scala.util.Success
import scala.util.Success
import views.html.defaultpages.badRequest
import scala.util.Failure


object Application extends Controller with Secured with MongoController {

  def collection: JSONCollection = db.collection[JSONCollection]("users")

  /***
   * Log user in;
   * @return the token used for next requests
   */
  def logUserIn = Action.async(parse.json) { request =>
   val username = (request.body \ "username").toString().replaceAll("^\"|\"$", "")
   val password = (request.body \ "password").toString().replaceAll("^\"|\"$", "")
   
   Logger.info("checking against username and password..")
   val query = Json.obj("username"->username, "password"->password)
   val futureItem = collection.find(query).one[JsValue]
   futureItem.map {
    case Some(item) => {
      val payload = (item.\ ("_id")).as[JsObject]
      val jwt = JWT.encode("secret-key", payload)
      Ok(Json.obj("token" -> jwt))
    }
    case None => NotFound(Json.obj("message" -> "No such item"))
              }
  }
  
  def createUser = Action.async (parse.json){ request =>
    val username = request.body.\("username").toString().replaceAll("^\"|\"$", "");
    val user = new User(username, "test@test.it", "")
    user.setPassword("Ciccio")
    collection.insert(user).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Ok("Added")
    }
    
  }
  
  def retrieveUsers = Authenticated.async { request =>
   
    // let's do our query
    val cursor: Cursor[User] = collection.find(Json.obj()).cursor[User]

    // gather all the JsObjects in a list
    val futurePostsList: Future[List[User]] = cursor.collect[List]()

    val futurePostsJsonArray: Future[JsArray] = futurePostsList.map { users =>
      println(users)
      Json.arr(users)
    }

    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      println(posts)
      Ok(posts)
    }
  }

}