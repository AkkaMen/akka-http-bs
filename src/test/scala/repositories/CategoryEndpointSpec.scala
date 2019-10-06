package repositories

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import controllers.CategoryController
import model.{Category, CategoryJson}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, MustMatchers}
import service.ConfigService
import services.FlywayService


class CategoryEndpointSpec extends AsyncWordSpec
  with MustMatchers
  with BeforeAndAfterAll
  with ConfigService
  with WebApi
  with ScalatestRouteTest
  with CategoryJson{

  val flywayService = new FlywayService(jdbcUrl, dbUser, dbPassword)
  override val executor = system.dispatcher

  override def beforeAll {
    // Let's make sure our schema is created
    flywayService.migrateDatabase
  }

  override def afterAll {
    // Let's make sure our schema is dropped
    flywayService.dropDatabase
  }

  "A Categoryendpoint" must {
    val categories = new CategoryController

    "return an empty list at the beginning" in {
      Get("/categories/") ~> categories.routes ~> check {
        status mustBe StatusCodes.OK
        val categories = responseAs[List[Category]]
        categories must have size 0
      }
    }
  }
}