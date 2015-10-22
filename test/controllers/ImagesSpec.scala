package lgtm.test.controller

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import controllers.Images

@RunWith(classOf[JUnitRunner])
class ImagesSpec extends Specification {

  "Images#upload" should {

    "render the upload page" in new WithApplication {
      val upload = route(FakeRequest(GET, "/upload")).get

      status(upload) must equalTo(OK)
      contentType(upload) must beSome.which(_ == "text/html")
      contentAsString(upload) must contain ("Drop files here or click to upload.")
    }
  }
}
