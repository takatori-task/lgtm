package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class User(user_id: String, name: Option[String], avatar_url: Option[String])

object User {

  def select(user_id: String): Option[User] = {
    DB.withConnection { implicit c =>
      SQL("""
          SELECT * 
          FROM user 
          WHERE user_id = {user_id}
          """)
        .on('user_id -> user_id)
        .as(user.singleOpt)
    }
  }

  def create(user_id: String, name: Option[String], avatar_url: Option[String]): Unit = {
    DB.withConnection { implicit c =>
      SQL("""
          INSERT IGNORE INTO user (user_id, name, avatar_url)
          VALUES ({user_id}, {name}, {avatar_url})
          """)
        .on('user_id -> user_id,
            'name -> name,
            'avatar_url -> avatar_url
      ).executeInsert()
    }
  }

  val user = {
    get[String]("user_id") ~
    get[Option[String]]("name") ~
    get[Option[String]]("avatar_url") map {
      case user_id ~ name ~ avatar_url => User(user_id, name, avatar_url)
    }
  }
}


