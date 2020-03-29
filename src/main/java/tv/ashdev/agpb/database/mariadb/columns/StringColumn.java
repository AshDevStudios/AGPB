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

package tv.ashdev.agpb.database.mariadb.columns;

import java.sql.ResultSet;
import java.sql.SQLException;
import tv.ashdev.agpb.database.mariadb.SQLColumn;

/**
 * The type String column.
 */
public class StringColumn extends SQLColumn<String> {

  private final int maxLength;

  /**
   * Instantiates a new String column.
   *
   * @param name         the name
   * @param nullable     the nullable
   * @param defaultValue the default value
   * @param maxLength    the max length
   */
  public StringColumn(String name, boolean nullable, String defaultValue, int maxLength) {
    this(name, nullable, defaultValue, false, maxLength);
  }

  /**
   * Instantiates a new String column.
   *
   * @param name         the name
   * @param nullable     the nullable
   * @param defaultValue the default value
   * @param publicKey    the public key
   * @param maxLength    the max length
   */
  public StringColumn(String name, boolean nullable, String defaultValue, boolean publicKey,
      int maxLength) {
    super(name, nullable, defaultValue, publicKey);
    this.maxLength = maxLength;
  }

  /**
   * Gets data description.
   *
   * @return the data description
   */
  @Override
  public String getDataDescription() {
    return "VARCHAR(" + maxLength + ")" + (defaultValue == null ? "" : " DEFAULT " + defaultValue)
        + nullable() + (primaryKey ? " PRIMARY KEY" : "");
  }

  /**
   * Gets value.
   *
   * @param results the results
   * @return the value
   * @throws SQLException the sql exception
   */
  @Override
  public String getValue(ResultSet results) throws SQLException {
    return results.getString(name);
  }

  /**
   * Update value.
   *
   * @param results  the results
   * @param newValue the new value
   * @throws SQLException the sql exception
   */
  @Override
  public void updateValue(ResultSet results, String newValue) throws SQLException {
    results.updateString(name, newValue);
  }
}
