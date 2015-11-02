package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Favorite(user_id: String, image_id: Long)

object Favorite {

  def register(user_id: String, image_id: Long): Unit = {
    DB.withConnection { implicit c =>
      SQL("""
          INSERT INTO favorite (user_id, id)
          VALUES ({user_id}, {image_id})
          """)
        .on(
          'user_id -> user_id,
          'image_id -> image_id
        ).executeInsert()
    }
  }

  def unRegister(): Unit = {

  }
}
