package lgtm.test.mode

import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._
import play.api.test.WithApplication
import play.api.test.Helpers.running
import org.specs2.execute.AsResult
import play.api.test.FakeApplication
import play.api.Play

import java.io.File
import play.api.libs.Files
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import scalax.file.Path

import models.Image
import lgtm.test.util.WithDbData

trait appWithTestDatabase {

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

      // fileをcopyしてテストに使用するファイルを作成する
      // createによって元のファイルがなくなら無いようにするため
      val in = Path.fromString(Play.current.path.getPath() + "/test/resources/image.jpg")
      val out = Path.fromString(Play.current.path.getPath() + "/test/resources/testimage.jpg")
      in.copyTo(out,replaceExisting=true)

      // create temporaryfile
      val testImageFile = new File(Play.current.path.getPath() + "/test/resources/testimage.jpg")
      val ref = TemporaryFile(testImageFile)

      val id = Image.create(ref, user_id=None)
      id must beSome // check if an element is Some(_)
      Image.all() must have size 6
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
      images(0).image_url === "http://placeimg.com/300/480/any"
      images(0).user_id must beSome.which { _  === "test" }
      images(2).image_url === "http://placeimg.com/320/180/arch"
      images(2).user_id must beNone
    }
  }
}
