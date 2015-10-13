package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import models.Image
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

class Images extends Controller {

  def index = Action {
    Redirect(routes.Images.list)
  }

  def list = Action { implicit request =>
    Ok(views.html.index(Image.all()))
  }

  def show(id: Long) = TODO

  def upload = Action { implicit request =>
    Ok(views.html.upload(request.flash.get("error").getOrElse("")))
  }

  def save = Action(parse.multipartFormData) { implicit request =>
    request.body.file("image").map { image =>
      import java.io.File

      // ファイルタイプチェック
      image.contentType match {
        case Some(s) if Set("image/jpeg", "image/png") contains s => println(s)
        case _ => Redirect(routes.Images.upload).flashing(
          "error" -> "File format error"
         )
      }
      // ファイル保存
      val image_url = Play.current.path.getPath() + "/public/images/upload" +image.filename
      image.ref.moveTo(new File(image_url), replace=true)

      // DBに保存

      Image.create(image_url, request.session.get("user")) match {
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