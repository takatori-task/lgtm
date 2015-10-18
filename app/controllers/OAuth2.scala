package controllers

import play.api.Play
import play.api.http.{MimeTypes, HeaderNames}
import play.api.libs.ws.WS
import play.api.mvc.{Results, Action, Controller}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class OAuth2 extends Controller {
  lazy val oauth2 = new utils.OAuth2(Play.current)

  def callback(codeOpt: Option[String] = None, stateOpt: Option[String] = None) = Action.async { implicit request =>
    (for {
      code <- codeOpt
      state <- stateOpt
      oauthState <- request.session.get("oauth-state")
    } yield {
      if (state == oauthState) {
        oauth2.getToken(code).map { accessToken =>
          Redirect(routes.OAuth2.success()).withSession("oauth-token" -> accessToken)
        }.recover {
          case ex: IllegalStateException => Unauthorized(ex.getMessage)
        }
      }
      else {
        Future.successful(BadRequest("Invalid github login"))
      }
    }).getOrElse(Future.successful(BadRequest("No parameters supplied")))
  }

  def success() = Action.async { request =>
    implicit val app = Play.current
    request.session.get("oauth-token").fold(Future.successful(Unauthorized("No way Jose"))) { authToken =>
      WS.url("https://api.github.com/user").
        withHeaders(HeaderNames.AUTHORIZATION -> s"token $authToken").
        get().map { response =>
          val user = (response.json \ "login").as[String]
          Redirect(controllers.routes.Images.index)withSession("user" -> user)
        }
    }
  }
}
