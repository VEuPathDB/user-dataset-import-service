package org.veupathdb.service.userds.util;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbMan
{
  private static DataSource importDb;

  public static DataSource initImportDb() {
    final var config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://postgres:5432/postgres");
    config.setUsername("postgres");
    config.setPassword("1234");
    return importDb = new HikariDataSource(config);
  }

  public static DataSource getImportDb() {
    return importDb;
  }
}
