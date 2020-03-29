/*
 * MIT License
 *
 * Copyright (c) 2020 AshDev (Ashley Tonkin)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package tv.ashdev.agpb.database.mariadb;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Data manager.
 */
public class DataManager {

  private final DatabaseConnector connector;
  private final String tableName;
  private final SQLColumn[] columns;

  /**
   * Instantiates a new Data manager.
   *
   * @param connector the connector
   * @param tableName the table name
   */
  public DataManager(DatabaseConnector connector, String tableName) {
    this.connector = connector;
    this.tableName = tableName;
    List<Field> fields = new LinkedList<>();
    for (Field field : this.getClass().getDeclaredFields()) {
      if (field.getType() == SQLColumn.class) {
        fields.add(field);
      }
    }
    columns = new SQLColumn[fields.size()];
    try {
      for (int i = 0; i < columns.length; i++) {
        columns[i] = (SQLColumn) fields.get(i).get(null);
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * Read t.
   *
   * @param <T>   the type parameter
   * @param query the query
   * @param rf    the rf
   * @return the t
   */
  protected final <T> T read(String query, ResultsFunction<T> rf) {
    return read(query, rf, null);
  }

  /**
   * Read t.
   *
   * @param <T>   the type parameter
   * @param query the query
   * @param rf    the rf
   * @param err   the err
   * @return the t
   */
  protected final <T> T read(String query, ResultsFunction<T> rf, T err) {
    try (Statement statement = getConnection().createStatement();
        ResultSet results = statement.executeQuery(query)) {
      return rf.apply(results);
    } catch (SQLException e) {
      DatabaseConnector.LOG.error("Exception in SQL: " + e);
      return err;
    }
  }

  /**
   * Read write.
   *
   * @param query the query
   * @param rc    the rc
   */
  protected final void readWrite(String query, ResultsConsumer rc) {
    try (Statement statement = getConnection()
        .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet results = statement.executeQuery(query)) {
      rc.consume(results);
    } catch (SQLException e) {
      DatabaseConnector.LOG.error("Exception in SQL: " + e);
    }
  }

  /**
   * Read write t.
   *
   * @param <T>   the type parameter
   * @param query the query
   * @param rf    the rf
   * @return the t
   */
  protected final <T> T readWrite(String query, ResultsFunction<T> rf) {
    return readWrite(query, rf, null);
  }

  /**
   * Read write t.
   *
   * @param <T>   the type parameter
   * @param query the query
   * @param rf    the rf
   * @param err   the err
   * @return the t
   */
  protected final <T> T readWrite(String query, ResultsFunction<T> rf, T err) {
    try (Statement statement = getConnection()
        .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet results = statement.executeQuery(query)) {
      return rf.apply(results);
    } catch (SQLException e) {
      DatabaseConnector.LOG.error("Exception in SQL: " + e);
      return err;
    }
  }

  /**
   * Gets table name.
   *
   * @return the table name
   */
  public final String getTableName() {
    return tableName;
  }

  /**
   * Get columns sql column [ ].
   *
   * @return the sql column [ ]
   */
  public final SQLColumn[] getColumns() {
    return columns.clone();
  }

  /**
   * Gets connection.
   *
   * @return the connection
   */
  protected final Connection getConnection() {
    return connector.getConnection();
  }

  /**
   * Select string.
   *
   * @param where   the where
   * @param columns the columns
   * @return the string
   */
  protected final String select(String where, SQLColumn... columns) {
    StringBuilder selection = new StringBuilder(columns[0].name);
    for (int i = 1; i < columns.length; i++) {
      selection.append(", ").append(columns[i].name);
    }
    return select(where, selection.toString());
  }

  /**
   * Select all string.
   *
   * @param where the where
   * @return the string
   */
  protected final String selectAll(String where) {
    return select(where, "*");
  }

  /**
   * Select all string.
   *
   * @return the string
   */
  protected final String selectAll() {
    return select(null, "*");
  }

  private String select(String where, String columns) {
    return "SELECT " + columns + " FROM " + tableName + (where == null ? "" : " WHERE " + where);
  }

  /**
   * Primary key string.
   *
   * @return the string
   */
  public String primaryKey() {
    return null;
  }


  /**
   * The interface Results consumer.
   */
  @FunctionalInterface
  public interface ResultsConsumer {

    /**
     * Consume.
     *
     * @param results the results
     * @throws SQLException the sql exception
     */
    void consume(ResultSet results) throws SQLException;
  }

  /**
   * The interface Results function.
   *
   * @param <T> the type parameter
   */
  @FunctionalInterface
  public interface ResultsFunction<T> {

    /**
     * Apply t.
     *
     * @param results the results
     * @return the t
     * @throws SQLException the sql exception
     */
    T apply(ResultSet results) throws SQLException;
  }

}
