package repositories

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import controllers.CategoryController
import helpers.CategorySpecHelper
import model.{Category, CategoryJson}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, MustMatchers}
import repository.CategoryRepository
import service.{ConfigService, PostgresService}
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

  val databaseService = new PostgresService(jdbcUrl, dbUser, dbPassword)

  val categoryRepository = new CategoryRepository(databaseService)

  val categorySpecHelper = new CategorySpecHelper(categoryRepository)

  override def beforeAll {
    // Let's make sure our schema is created
    flywayService.migrateDatabase
  }

  override def afterAll {
    // Let's make sure our schema is dropped
    flywayService.dropDatabase
  }

  "A Categoryendpoint" must {
    val categories = new CategoryController(categoryRepository)

    "return an empty list at the beginning" in {
      categorySpecHelper.createAndDelete() { c =>
        Get("/categories/") ~> categories.routes ~> check {
          status mustBe StatusCodes.OK
          val categories = responseAs[List[Category]]
          categories must have size 1
        }
      }
    }

    "create a category" in {
      Post("/categories/", categorySpecHelper.category) ~> categories.routes ~> check {
        status mustBe StatusCodes.Created
        val category = responseAs[Category]
        categoryRepository.delete(category.id.get)
        category.id mustBe defined
        category.title mustBe categorySpecHelper.category.title
      }
    }
  }
}