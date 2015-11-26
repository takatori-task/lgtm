package lgtm.test.models

import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._
import play.api.test.WithApplication
import play.api.test.Helpers.running
import org.specs2.execute.AsResult
import play.api.test.FakeApplication
import play.api.Play

import models.User
import lgtm.test.util.WithDbData

@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification with appWithTestDatabase {

  "User#select" should {
    "idを指定して一件取得できる" in new WithDbData {
      val user = User.select("satoshi")
      user must beSome
      user.get.name == "takatori"
      user.get.avatar_url == "http://placeimg.com/200/300/people"
    }

    "idが存在しなければOptionがNoneで返される" in new WithDbData {
      val user = User.select("takatori")
      user must beNone
    }

  }

  "User#create" should {
    "createで一件追加できる" in new WithDbData {
      val user_id = "takatori"
      val name = Some("takatori")
      val avatar_url = Some("http://placeimg.com/640/480/any")
      User.create(user_id, name, avatar_url)
    }

    "idが既にに存在しているならば追加しない" in new WithDbData {
      User.create("satoshi", Some("test"), None)
      val user = User.select("satoshi")
      user must beSome
      user.get.name != "test"
      user.get.name == "takatori"
    }
  }


}
