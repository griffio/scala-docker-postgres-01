package griffio

import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.{ FlatSpec, MustMatchers }

class PostgresDockerSpec extends FlatSpec with MustMatchers with DockerKitDockerJava with DockerTestKit with PostgresDockerKit {

    "postgres service" must "be ready" in {
      isContainerReady(postgresContainer).futureValue mustBe true
    }

}
