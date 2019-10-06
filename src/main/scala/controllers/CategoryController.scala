package controllers

import akka.http.scaladsl.server.Directives._
import model.{Category, CategoryJson}

class CategoryController extends CategoryJson{

  val routes = pathPrefix("categories") {
    pathEndOrSingleSlash {
      get {
        complete{
          val list: List[Category] = List()
          list
        }
      }
    }
  }

}
