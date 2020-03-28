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

package tv.AshDev.AGPB.database.mariadb.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import tv.AshDev.AGPB.database.mariadb.SQLColumn;

/**
 * The type Instant milli column.
 */
public class InstantMilliColumn extends SQLColumn<Instant> {

  /**
   * Instantiates a new Instant milli column.
   *
   * @param name         the name
   * @param nullable     the nullable
   * @param defaultValue the default value
   */
  public InstantMilliColumn(String name, boolean nullable, Instant defaultValue) {
    this(name, nullable, defaultValue, false);
  }

  /**
   * Instantiates a new Instant milli column.
   *
   * @param name         the name
   * @param nullable     the nullable
   * @param defaultValue the default value
   * @param primaryKey   the primary key
   */
  public InstantMilliColumn(String name, boolean nullable, Instant defaultValue,
      boolean primaryKey) {
    super(name, nullable, defaultValue, primaryKey);
  }

  /**
   * Gets data description.
   *
   * @return the data description
   */
  @Override
  public String getDataDescription() {
    return "BIGINT" + (defaultValue == null ? "" : " DEFAULT " + defaultValue.toEpochMilli()) + (
        nullable ? "" : " NOT NULL") + (primaryKey ? " PRIMARY KEY" : "");
  }

  /**
   * Gets value.
   *
   * @param results the results
   * @return the value
   * @throws SQLException the sql exception
   */
  @Override
  public Instant getValue(ResultSet results) throws SQLException {
    long val = results.getLong(name);
    return val == 0 ? null : Instant.ofEpochSecond(val);
  }

  /**
   * Update value.
   *
   * @param results  the results
   * @param newValue the new value
   * @throws SQLException the sql exception
   */
  @Override
  public void updateValue(ResultSet results, Instant newValue) throws SQLException {
    results.updateLong(name, newValue.toEpochMilli());
  }

}
