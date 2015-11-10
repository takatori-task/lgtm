package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import play.api.Play
import play.api.libs.Files.TemporaryFile

case class Image(id: Long, image_url: String, user_id: Option[String])

object Image {

  def uuid = java.util.UUID.randomUUID.toString

  def create(image: TemporaryFile, user_id: Option[String]): Option[Long] = {
    import java.io.File

    val image_url = Play.current.path.getPath() + current.configuration.getString("file.upload.directory").get + uuid + ".jpg"
    image.moveTo(new File(image_url), replace=true)// ファイル保存

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

  val image = {
    get[Long]("id") ~
    get[String]("image_url") ~
    get[Option[String]]("user_id") map {
      case id ~ image_url ~ user_id => Image(id, image_url, user_id)
    }
  }

}
