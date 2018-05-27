package griffio

import com.whisk.docker.DockerReadyChecker.LogLineContains
import com.whisk.docker._
import com.whisk.docker.impl.dockerjava._
/**
 * https://docs.docker.com/samples/library/postgres/#environment-variables
 * The environment variables recommended for you to use the PostgreSQL image.
 * Sets the superuser password for Postgres.
 * The default superuser is defined by the POSTGRES_USER
 */
trait PostgresDockerKit extends DockerKit with DockerKitDockerJava {
  import scala.concurrent.duration._
  def PostgresAdvertisedPort = 5432
  def PostgresExposedPort = 54320
  def PostgresUser = "postgres"
  def PostgresPassword = "password"
  def PostgresDatabasename = "postgresdb"
  def PostgresInitDbArgs = "--nosync" // By default, initdb will wait for all files to be written safely to disk. This option causes initdb to return without waiting, which is faster, but means that a subsequent operating system crash can leave the data directory corrupt. Generally, this option is useful for testing, but should not be used when creating a production installation.

  val postgresContainer: DockerContainer = DockerContainer("postgres:9.6.9")
    .withPorts((PostgresAdvertisedPort, Some(PostgresExposedPort)))
    .withEnv(s"POSTGRES_USER=$PostgresUser", s"POSTGRES_PASSWORD=$PostgresPassword",
      s"POSTGRES_DB=$PostgresDatabasename", s"POSTGRES_INITDB_ARGS=$PostgresInitDbArgs")
    .withReadyChecker(
      // Currently check for log output unless it proves to be flakey
      new LogLineContains("PostgreSQL init process complete") // postgres log has a start/stop/start init process
        .looped(15, 1.second)
    )

  abstract override def dockerContainers: List[DockerContainer] =
    postgresContainer :: super.dockerContainers
}
