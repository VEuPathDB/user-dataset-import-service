package org.veupathdb.service.userds.util;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.health.ExternalDependency;

public class DatabaseDependency extends ExternalDependency
{
  private static final String TEST_QUERY = "SELECT 1;";

  private final Logger     log;
  private final DataSource ds;
  private final String     url;
  private final short      port;

  public DatabaseDependency(
    String name,
    String url,
    short port,
    DataSource ds
  ) {
    super(name);

    this.url = url;
    this.port = port;
    this.ds = ds;
    this.log = LogManager.getLogger(DatabaseDependency.class);
  }

  @Override
  public TestResult test() {
    log.info("Checking dependency health for database {}", name);

    if (!pinger.isReachable(url, port))
      return new TestResult(this, false, Status.UNKNOWN);

    try (
      var con = ds.getConnection();
      var stmt = con.createStatement()
    ) {
      stmt.execute(TEST_QUERY);
      return new TestResult(this, true, Status.ONLINE);
    } catch (SQLException e) {
      log.warn("Health check failed for database {}", name);
      log.debug(e);
      return new TestResult(this, true, Status.UNKNOWN);
    }
  }

  @Override
  public void close() throws Exception {
    ((HikariDataSource)ds).close();
  }
}
