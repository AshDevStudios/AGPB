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

package tv.AshDev.AGPB.database.mariadb;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class DataManager {

  private final DatabaseConnector connector;
  private final String tableName;
  private SQLColumn[] columns;

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

  protected final <T> T read(String query, ResultsFunction<T> rf) {
    return read(query, rf, null);
  }

  protected final <T> T read(String query, ResultsFunction<T> rf, T err) {
    try (Statement statement = getConnection().createStatement();
        ResultSet results = statement.executeQuery(query)) {
      return rf.apply(results);
    } catch (SQLException e) {
      DatabaseConnector.LOG.error("Exception in SQL: " + e);
      return err;
    }
  }

  protected final void readWrite(String query, ResultsConsumer rc) {
    try (Statement statement = getConnection()
        .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet results = statement.executeQuery(query)) {
      rc.consume(results);
    } catch (SQLException e) {
      DatabaseConnector.LOG.error("Exception in SQL: " + e);
    }
  }

  protected final <T> T readWrite(String query, ResultsFunction<T> rf) {
    return readWrite(query, rf, null);
  }

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

  public final String getTableName() {
    return tableName;
  }

  public final SQLColumn[] getColumns() {
    return columns.clone();
  }

  protected final Connection getConnection() {
    return connector.getConnection();
  }

  protected final String select(String where, SQLColumn... columns) {
    StringBuilder selection = new StringBuilder(columns[0].name);
    for (int i = 1; i < columns.length; i++) {
      selection.append(", ").append(columns[i].name);
    }
    return select(where, selection.toString());
  }

  protected final String selectAll(String where) {
    return select(where, "*");
  }

  protected final String selectAll() {
    return select(null, "*");
  }

  private String select(String where, String columns) {
    return "SELECT " + columns + " FROM " + tableName + (where == null ? "" : " WHERE " + where);
  }

  public String primaryKey() {
    return null;
  }


  @FunctionalInterface
  public interface ResultsConsumer {

    void consume(ResultSet results) throws SQLException;
  }

  @FunctionalInterface
  public interface ResultsFunction<T> {

    T apply(ResultSet results) throws SQLException;
  }

}
