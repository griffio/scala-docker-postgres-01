# scala-docker-postgres-01
Scala docker postgres integration
```
package griffio

import anorm._
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.{FlatSpec, MustMatchers}
import play.api.db.Databases

class PostgresDockerSpec extends FlatSpec with MustMatchers with DockerTestKit with PostgresDockerKit {

  "postgres service" must "be ready" in {
    isContainerReady(postgresContainer).futureValue mustBe true
  }

  val config: Map[String, _] = Map("username" -> PostgresUser, "password" -> PostgresPassword)

  "connection" must "select" in Databases.withDatabase(
    "org.postgresql.Driver",
    s"jdbc:postgresql://localhost:$PostgresExposedPort/$PostgresDatabasename",
    PostgresDatabasename,
    config
  ) { database =>
    database.withConnection { implicit c =>
      SQL("Select 1").execute()
    }
  }
}
```
