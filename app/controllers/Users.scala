package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

import java.util.UUID
import models.User

class Users extends Controller {


  def signin = Action { implicit request =>

    // singin済みならtopページへ遷移
    if(request.session.get("user").isEmpty) Redirect(routes.Images.index)

    val oauth2 = new utils.OAuth2(Play.current) // OAuth2インスタンス作成
    val callbackUrl = routes.OAuth2.callback(None, None).absoluteURL() //callbackURLの取得
    val scope = "user"   // github scope - request repo access
    val state = UUID.randomUUID().toString  // random confirmation string
    val redirectUrl = oauth2.getAuthorizationUrl(callbackUrl, scope, state)

    Redirect(redirectUrl).withSession("oauth-state" -> state)
  }

  def signup(user_id: String, name: Option[String], avatar_url: Option[String]) = Action { implicit request =>
    // アカウントがなければ作成する
    if (User.select(user_id).isEmpty)  User.create(user_id, name, avatar_url)
    Redirect(routes.Images.list).withSession("user_id" -> user_id)
  }

  def signout = TODO

}