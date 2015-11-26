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

  def unRegister(user_id: String, id: Long): Unit = {
    DB.withConnection { implicit c =>
      SQL("""
          DELETE
          FROM favorite
          WHERE user_id = {user_id} AND id = {id}           
          """)
        .on(
        'user_id -> user_id,
         'id -> id
      ).executeUpdate()
    }
  }


  def select(user_id: String, id: Long): Option[Favorite] = {
    DB.withConnection { implicit c =>
      SQL("""
          SELECT * 
          FROM favorite  
          WHERE user_id = {user_id} AND id = {id}
          """)
        .on(
        'user_id -> user_id,
        'id -> id
      ).as(favorite.singleOpt) 
    }
  }

  def check(user_id: String, id: Long): Boolean = {
    Favorite.select(user_id, id) match {
      case Some(x) => true
      case _ => false
    }
  }

  val favorite = {
    get[String]("user_id") ~
    get[Long]("id") map {
      case user_id ~ id => Favorite(user_id, id)
    }
  }

}
