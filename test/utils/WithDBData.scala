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
          VALUES ('http://placeimg.com/300/480/any', 'test'),
                 ('http://placeimg.com/300/300/tech', 'takatori'),
                 ('http://placeimg.com/320/180/arch', NULL),
                 ('http://placeimg.com/640/480/any', NULL),              
                 ('http://placeimg.com/200/300/people', 'test');
          """).execute()
    }
  }

  def tearDown() {
    DB.withConnection { implicit c =>
      SQL("delete from image").executeUpdate()
    }
  }
}
