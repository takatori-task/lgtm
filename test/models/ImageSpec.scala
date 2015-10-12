import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import models.Image


@RunWith(classOf[JUnitRunner])
class ImageSpec extends Specification {

  "Image#create" should {
    "return id: Option[Long] if succeed in saveing to DB" in new WithApplication {
      val image_url = "./tmp/test.png"
      val user_id: Option[String] = None      
      val id = Image.create(image_url, user_id)
      id must beSome // check if an element is Some(_)
    }

    "throw Exception if image_url is Null" in new WithApplication {
      val image_url = null
      val user_id: Option[String] = Some("takatori")      
      Image.create(image_url, user_id) must throwA[Exception]
    }
  }

  "Image#all" should {
    "return all images" in new WithApplication {
      val images = Image.all()
      images must not be empty
    }
  }
}
