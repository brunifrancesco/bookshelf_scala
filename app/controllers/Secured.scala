package controllers

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json._
import authentikat.jwt._
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac
import io.really.jwt._


//import models.User

//class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)
class AuthenticatedRequest[A](request: Request[A]) extends WrappedRequest[A](request)

trait Secured {
  def Authenticated = AuthenticatedAction
}

object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[AnyContent](request: Request[AnyContent], block: AuthenticatedRequest[AnyContent] => Future[Result]) ={
      if(request.headers.get("Authorization").isDefined){
        val authorization = request.headers.get("Authorization").orNull
        val result = JWT.decode(authorization, Some("secret-key"))
        println(result)
        if(result != null){
          block(new AuthenticatedRequest(request))
        }else{
          Future.successful(Forbidden("Token is wrong"))  
        }
        
            
         
      }else{
        Future.successful(Forbidden("Authorization header is not present"))
      }
    
  }
}