package models

import anorm._
import anorm.SqlParser._
import awscala._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.Files.TemporaryFile
import s3._

case class Image(id: Long, image_url: String, user_id: Option[String])

object Image {

  def uuid = java.util.UUID.randomUUID.toString
  implicit val s3 = S3.at(Region.Tokyo)
  val bucket = s3.bucket("lgtm-tokyo").get // TODO: Noneの場合の処理

  def uploadS3(name: String, file: File) = {
    bucket.putAsPublicRead(name, file)
  }

  def create(image: TemporaryFile, user_id: Option[String]): Option[Long] = {

    val extension = image.file.getName().split('.').last // 拡張子取得
    val name = uuid + extension

    val image_url = "https://s3-ap-northeast-1.amazonaws.com/lgtm-tokyo/" + name
    uploadS3(name, image.file)

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

  def select(id: Long): Option[Image] = {
    DB.withConnection { implicit c =>
      SQL("""
          SELECT * 
          FROM image
          WHERE id = {id}
          """)
        .on('id -> id)
        .as(image.singleOpt)
    }
  }

  def delete(id: Long, user_id: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("""
          DELETE 
          FROM image
          WHERE id = {id} AND user_id = {user_id}
          """)
        .on('id -> id, 'user_id -> user_id)
        .executeUpdate()
    }
  }

  def enumerate(ids: List[Long]): List[Image] = {
    DB.withConnection { implicit c =>
      SQL("""
          SELECT * 
          FROM image
          WHERE id in ({ids})
          """)
        .on('ids -> ids)
        .as(image *)
    }
  }

  def fetch(user_id: String): List[Image] = {
    DB.withConnection { implicit c =>
      SQL(
        """
        SELECT *
        FROM image
        WHERE user_id = {user_id}
        """
      )
        .on('user_id -> user_id)
        .as(image *)
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
