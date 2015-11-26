package lgtm.test.models

import org.specs2.mutable.Specification
import org.specs2.runner._
import org.junit.runner._
import play.api.test.WithApplication
import play.api.test.Helpers.running
import org.specs2.execute.AsResult
import play.api.test.FakeApplication
import play.api.Play

import models.Favorite
import lgtm.test.util.WithDbData


@RunWith(classOf[JUnitRunner])
class FavoriteSpec extends Specification with appWithTestDatabase {

  "Favorite#register" should {
    "お気に入りを一件追加できる" in new WithDbData {
      Favorite.register("satoshi", 2)
    }

    "user_idとimage_idの組がすでに存在しているならばエラーが発生する" in new WithDbData {
      Favorite.register("satoshi", 1) must throwA[Exception]
    }
  }

  "Favorite#check" should {
    "ユーザのお気に入りの画像であればTrueが返る" in new WithDbData {
      val result = Favorite.check("satoshi", 1)
      result must beTrue
    }

    "ユーザのお気に入りの画像でなければFalseが返る" in new WithDbData {
      val result = Favorite.check("satoshi", 2)
      print(result)
      result must beFalse
    }
  }

  "Favorite#unRegister" should {
    "お気に入りを一件削除できる" in new WithDbData {
      Favorite.unRegister("satoshi", 5)
      Favorite.check("satoshi", 5) must beFalse
    }
  }
}

