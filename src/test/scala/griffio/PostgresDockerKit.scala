package griffio

import java.net.Socket

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try
import com.whisk.docker.{ DockerCommandExecutor, DockerContainer, DockerContainerState,  DockerKit, DockerReadyChecker }

/**
 * https://docs.docker.com/samples/library/postgres/#environment-variables
 * The environment variables recommended for you to use the PostgreSQL image.
 * Sets the superuser password for Postgres.
 * The default superuser is defined by the POSTGRES_USER
 */
trait PostgresDockerKit extends DockerKit {
  import scala.concurrent.duration._
  def PostgresAdvertisedPort = 5432
  def PostgresExposedPort = 44444
  def PostgresUser = "postgres"
  def PostgresPassword = "password"

  val postgresContainer: DockerContainer = DockerContainer("postgres:9.5.3")
    .withPorts((PostgresAdvertisedPort, Some(PostgresExposedPort)))
    .withEnv(s"POSTGRES_USER=$PostgresUser", s"POSTGRES_PASSWORD=$PostgresPassword")
    .withReadyChecker(
      new PostgresReadyChecker(Some(PostgresExposedPort))
        .looped(15, 1.second)
    )

  abstract override def dockerContainers: List[DockerContainer] =
    postgresContainer :: super.dockerContainers
}

class PostgresReadyChecker(port: Option[Int] = None)
  extends DockerReadyChecker {

  override def apply(container: DockerContainerState)(implicit docker: DockerCommandExecutor,
                                                      ec: ExecutionContext): Future[Boolean] =
    container
      .getPorts()
      .map(ports =>
        Try {
          val socket = new Socket(docker.host, port.getOrElse(ports.values.head))
          socket.close()
          true
        }.getOrElse(false))
}
