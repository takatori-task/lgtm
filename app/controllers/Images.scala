package controllers

import play.api.Play
import play.api.mvc.{Action, Controller}
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

import models.Image
import models.User

class Images extends Controller {

  def index = Action {
    Redirect(routes.Images.list)
  }

  def list = Action { implicit request =>
    Ok(views.html.index(
      Image.all(),
      User.select(request.session.get("user_id").getOrElse(""))
    ))
  }

  def show(id: Long) = Action { implicit request =>
    val user = User.select(request.session.get("user_id").getOrElse(""))
    Image.select(id) match {
      case Some(image) => Ok(views.html.image(image, user))
      case _ => Ok(views.html.image404(user))
    }
  }

  def upload = Action { implicit request =>
    Ok(views.html.upload(
      request.flash.get("error").getOrElse(""),
      User.select(request.session.get("user_id").getOrElse(""))
    ))
  }

  def save = Action(parse.multipartFormData) { implicit request =>
    request.body.file("image").map { image =>
      // ファイルタイプチェック
      image.contentType match {
        case Some(s) if Set("image/jpeg", "image/png") contains s => println(s)
        case _ => Redirect(routes.Images.upload).flashing(
          "error" -> "File format error"
         )
      }
      // DBに保存
      Image.create(image.ref, request.session.get("user")) match {
        case Some(i) => Redirect(routes.Images.show(i))
        case _ => Redirect(routes.Images.upload).flashing(
          "error" -> "予期しないデータベース・エラーが発生しました - 指定されたレコードを書き込みできません。"
        )
      }
    } getOrElse {
      Redirect(routes.Images.upload).flashing(
        "error" -> "Missing file"
      )
    }
  }
}
