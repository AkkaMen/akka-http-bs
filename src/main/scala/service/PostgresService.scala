package service

import services.DatabaseService
import slick.driver.{JdbcProfile, PostgresDriver}
import slick.driver.PostgresDriver.api._

class PostgresService(jdbcUrl: String, dbUser: String, dbPassword: String) extends DatabaseService {

  // Setup our database driver, Postgres in this case

  val driver: JdbcProfile = PostgresDriver
  val db: Database = Database.forURL(jdbcUrl, dbUser, dbPassword)
  db.createSession()

}
