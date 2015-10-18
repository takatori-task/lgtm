package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

import java.util.UUID
import models.User

class Users extends Controller {

  def singin = Action { implicit request =>

    // singin済みならtopページへ遷移
    if(request.session.get("user").isEmpty) Redirect(routes.Images.index)

    val oauth2 = new utils.OAuth2(Play.current) // OAuth2インスタンス作成
    val callbackUrl = routes.OAuth2.callback(None, None).absoluteURL() //callbackURLの取得
    val scope = "user"   // github scope - request repo access
    val state = UUID.randomUUID().toString  // random confirmation string
    val redirectUrl = oauth2.getAuthorizationUrl(callbackUrl, scope, state)

    Redirect(redirectUrl).withSession("oauth-state" -> state)
  }

  def singout = TODO

}
