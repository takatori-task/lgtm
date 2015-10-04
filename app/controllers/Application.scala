package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import models.Image
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.libs.json._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def upload = Action { implicit request =>
    Ok(views.html.upload(request.flash.get("error").getOrElse("")))
  }

  def save = Action(parse.multipartFormData) { implicit request =>
    request.body.file("image").map { image =>
      import java.io.File

      val filename = image.filename
      val contentType = image.contentType
      val image_url = s"./tmp/$filename"

      image.ref.moveTo(new File("./tmp", filename), true) // 第二引数は上書きするかどうか
      Image.create(image_url, request.session.get("user"))

      Ok("File uploaded")

    }.getOrElse {
      Redirect(routes.Application.upload).flashing(
        "error" -> "Missing file"
      )
    }
  }

}
