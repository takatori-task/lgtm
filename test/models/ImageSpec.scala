package lgtm.test.mode

import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._
import play.api.test.WithApplication
import play.api.test.Helpers.running
import org.specs2.execute.AsResult
import play.api.test.FakeApplication

import models.Image
import lgtm.test.util.WithDbData

trait appWithTestDatabase extends {

  val config = Map(
    "db.default.driver" -> "com.mysql.jdbc.Driver",
    "db.default.url" -> sys.env.getOrElse("JDBC_DATABASE_URL", "jdbc:mysql://localhost/lgtm_test"),
    "db.default.username" -> sys.env.getOrElse("JDBC_DATABASE_USER", "root")
  )

  def app: FakeApplication = FakeApplication(additionalConfiguration = config)
}


@RunWith(classOf[JUnitRunner])
class ImageSpec extends Specification with appWithTestDatabase {

  "Image#create" should {
    "return id: Option[Long] if succeed in saveing to DB" in new WithDbData(app) {
      val image_url = "./tmp/test.png"
      val user_id: Option[String] = None
      val id = Image.create(image_url, user_id)
      id must beSome // check if an element is Some(_)
    }

    "throw Exception if image_url is Null" in new WithDbData(app){
      val image_url = null
      val user_id: Option[String] = Some("takatori")
      Image.create(image_url, user_id) must throwA[Exception]
    }
  }

  "Image#all" should {
    "return all images" in new WithDbData(app) {
      val images = Image.all()
      images must not be empty
      images must have size 5
      images(0).image_url === "http://placeimg_1000_180_nature.jpg"
      images(0).user_id must beSome.which { _  === "test" }
      images(2).image_url === "http://placeimg_320_180_arch.jpg"
      images(2).user_id must beNone
    }
  }
}
