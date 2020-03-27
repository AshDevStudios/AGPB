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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnector {

  private final Connection connection;
  protected static final Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

  public DatabaseConnector(String host, String user, String password) throws SQLException {

    connection = DriverManager.getConnection(
        String.format("jdbc:mariadb://%s?user=%s&password=%s", host, user, password));
    LOG.info("Connected to database");

  }

  public final void init() {

    try {
      for (Field field : this.getClass().getFields()) {
        if (field.get(this).getClass().getSuperclass() == DataManager.class) {
          DataManager manager = (DataManager) field.get(this);
          if (!connection.getMetaData().getTables(null, null, manager.getTableName(), null)
              .next()) {
            LOG.info("Creating table: " + manager.getTableName());
            Statement s = connection
                .createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            s.closeOnCompletion();
            String str =
                manager.getColumns()[0].name + " " + manager.getColumns()[0].getDataDescription();
            for (int i = 1; i < manager.getColumns().length; i++) {
              str += ", \"" + manager.getColumns()[i].name + "\" " + manager.getColumns()[i]
                  .getDataDescription();
            }
            if (manager.primaryKey() != null) {
              str += ", PRIMARY KEY (" + manager.primaryKey() + ")";
            }
            s.execute("CREATE TABLE " + manager.getTableName() + "(" + str + ")");
          }
          for (SQLColumn col : manager.getColumns()) {
            if (!connection.getMetaData().getColumns(null, null, manager.getTableName(), col.name)
                .next()) {
              LOG.info("Creating column '" + col.name + "' in " + manager.getTableName());
              Statement st = connection
                  .createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
              st.closeOnCompletion();
              st.execute(
                  "ALTER TABLE " + manager.getTableName() + " ADD \"" + col.name + "\" " + col
                      .getDataDescription());
            }
          }
        }
      }
    } catch (SQLException | IllegalAccessException e) {
      LOG.error("Failed to initialize: " + e);
    }

  }

  public final Connection getConnection() {
    return connection;
  }

  public void shutdown() {
    try {
      connection.close();
    } catch (SQLException e) {
      LOG.error("Failed to close connection: " + e);
    }
  }

}
