package lgtm.test.util

import org.specs2.execute.AsResult
import org.specs2.execute.Result
import play.api.Application
import play.api.test.FakeApplication
import play.api.test.WithApplication
import play.api.db.DB
import anorm._
import anorm.SqlParser._

abstract class WithDbData(app: Application = FakeApplication()) extends WithApplication(app) {
  override def around[T: AsResult](t: => T): Result = super.around {
    setupData()
    try {
      t
    } finally {
      tearDown()
    }
  }

  def setupData() {
    // setup data
    DB.withConnection { implicit c =>
      SQL("delete from image").executeUpdate()
      // testデータ投入
      SQL("""
          INSERT INTO image (image_url, user_id)
          VALUES ('http://placeimg_1000_180_nature.jpg', 'test'),
                 ('http://placeimg_300_300_tech.jpg', 'takatori'),
                 ('http://placeimg_320_180_arch.jpg', NULL),
                 ('http://placeimg_640_480_any.jpg', NULL),              
                 ('http://placeimg_200_300_people.jpg', 'test');
          """).execute()
    }
  }

  def tearDown() {
    DB.withConnection { implicit c =>
      SQL("delete from image").executeUpdate()
    }
  }
}
