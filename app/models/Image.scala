package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Image(id: Long, image_url: String, user_id: Option[String])

object Image {

  def create(image_url: String, user_id: Option[String]): Option[Long] = {
    DB.withConnection { implicit c =>
      val id: Option[Long] = SQL("insert into image(image_url, user_id) values ({image_url}, {user_id})").on(
        'image_url -> image_url,
        'user_id -> user_id
      ).executeInsert()
      id
    }
  }

  def all(): List[Image] = {
    DB.withConnection { implicit c =>
      SQL("select * from image").as(image *)
    }
  }

  val image = {
    get[Long]("id") ~
    get[String]("image_url") ~
    get[Option[String]]("user_id") map {
      case id ~ image_url ~ user_id => Image(id, image_url, user_id)
    }
  }
}
