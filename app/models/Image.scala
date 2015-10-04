package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Image(id: Long, image_url: String, user_id: String)


object Image {

  val image = {
    get[Long]("id") ~
    get[String]("image_url") ~
    get[String]("user_id") map {
      case id ~ image_url ~ user_id => Image(id, image_url, user_id)
    }
  }
}
