package controllers

import java.io.IOException
import models.{Favorite, Image, User}
import play.api.mvc.{Action, ActionBuilder, ActionTransformer, Controller, Request, WrappedRequest}
import scala.concurrent.Future

class UserRequest[A](val user: Option[User], request: Request[A]) extends WrappedRequest[A](request)

object UserAction extends ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
  def transform[A](request: Request[A]) = Future.successful(new UserRequest(User.select(request.session.get("user_id").getOrElse("")), request))
}

class Images extends Controller {

  def index = Action(Redirect(routes.Images.list))

  def list = UserAction { implicit request =>
    Ok(views.html.index(Image.all(), request.user))
  }

  def show(id: Long) = UserAction { implicit request =>
    val favorite = Favorite.check(request.session.get("user_id").getOrElse(""), id)
    Image.select(id) match {
      case Some(image) => Ok(views.html.image(image, request.user, favorite))
      case _ => Ok(views.html.image404(request.user))
    }
  }

  def upload = UserAction { implicit request =>
    Ok(views.html.upload(request.flash.get("error").getOrElse(""), request.user))
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
      Image.create(image.ref, request.session.get("user_id")) match {
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

  def favorite(id: Long) = UserAction { implicit request =>
    request.user map { user =>
      try {
        Favorite.register(user.user_id, id)
        Redirect(routes.Images.show(id))
      } catch {
        case e: IOException => {
          Redirect(routes.Images.show(id)).flashing("error" -> "ログインしてください")
        }
      }
    } getOrElse {
      Redirect(routes.Images.show(id)).flashing("error" -> "ログインしてください")
    }
  }

  def delete(id: Long) = UserAction { implicit request =>
    request.user map { user =>
      try {
        Image.delete(id, user.user_id)
        Redirect(routes.Images.list())
      } catch {
        case e: IOException => {
          Redirect(routes.Images.show(id)).flashing("error" -> "ログインしてください")
        }
      }
    } getOrElse {
      Redirect(routes.Images.show(id)).flashing("error" -> "ログインしてください")
    }
  }

  def unfavorite(id: Long) = UserAction { implicit request =>
    request.user map { user =>
      try {
        Favorite.unRegister(user.user_id, id)
        Redirect(routes.Images.show(id))
      } catch {
        case e: IOException => {
          Redirect(routes.Images.show(id)).flashing("error" -> "ログインしてください")
        }
      }
    } getOrElse {
      Redirect(routes.Images.show(id)).flashing("error" -> "ログインしてください")
    }
  }
}
