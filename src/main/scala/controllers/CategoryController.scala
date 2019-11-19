package controllers

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import model.{Category, CategoryJson}
import repository.CategoryRepository

class CategoryController(val categoryRepository: CategoryRepository) extends CategoryJson {

  val routes: Route = pathPrefix("categories") {
    pathEndOrSingleSlash {
      get {
        complete {
          categoryRepository.all
        }
      } ~
        post {
          decodeRequest {
            entity(as[Category]) { category =>
              onSuccess(categoryRepository.findByTitle(category.title)) {
                case Some(_) => complete(StatusCodes.BadRequest)
                case None => complete(StatusCodes.Created, categoryRepository.create(category))
              }
            }
          }
        }
    }
  }

}
