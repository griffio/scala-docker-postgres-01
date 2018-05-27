package griffio

import anorm._
import play.api.db._
import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.{ FlatSpec, MustMatchers }

class PostgresDockerSpec extends FlatSpec with MustMatchers with DockerKitDockerJava with DockerTestKit with PostgresDockerKit {

    "postgres service" must "be ready" in {
      isContainerReady(postgresContainer).futureValue mustBe true
    }

  "insert json" in {
    DB.withConnection { implicit c =>
      SQL("create table json_table(data json)").execute()
      SQL("insert into json_table (data) values ({data}::json)").on(
        'data -> "{}"
      ).executeUpdate()

      SQL("select data from json_table").as(
        SqlParser.get[JsObject]("data").*
      )
    }
  }

}
