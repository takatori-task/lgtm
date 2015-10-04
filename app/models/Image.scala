package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Image(id: Long, image_url: String, user_id: String)


object Image {

  def create(image_url: String, user_id: Option[String]) {

    DB.withConnection {implicit c =>
      SQL("insert into image(image_url, user_id) values ({image_url}, {user_id})").on(
        'image_url -> image_url,
        'user_id -> user_id
      ).executeUpdate()
    }
  }

  val image = {
    get[Long]("id") ~
    get[String]("image_url") ~
    get[String]("user_id") map {
      case id ~ image_url ~ user_id => Image(id, image_url, user_id)
    }
  }
}
