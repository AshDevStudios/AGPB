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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Sql column.
 *
 * @param <T> the type parameter
 */
public abstract class SQLColumn<T> {

  /**
   * The Name.
   */
  public final String name;
  /**
   * The Nullable.
   */
  public final boolean nullable;
  /**
   * The Default value.
   */
  public final T defaultValue;
  /**
   * The Primary key.
   */
  public final boolean primaryKey;

  /**
   * Instantiates a new Sql column.
   *
   * @param name         the name
   * @param nullable     the nullable
   * @param defaultValue the default value
   */
  protected SQLColumn(String name, boolean nullable, T defaultValue) {
    this(name, nullable, defaultValue, false);
  }

  /**
   * Instantiates a new Sql column.
   *
   * @param name         the name
   * @param nullable     the nullable
   * @param defaultValue the default value
   * @param primaryKey   the primary key
   */
  protected SQLColumn(String name, boolean nullable, T defaultValue, boolean primaryKey) {
    this.name = name;
    this.nullable = nullable;
    this.defaultValue = defaultValue;
    this.primaryKey = primaryKey;
  }

  /**
   * Nullable string.
   *
   * @return the string
   */
  protected String nullable() {
    return nullable ? "" : " NOT NULL";
  }

  /**
   * Is string.
   *
   * @param value the value
   * @return the string
   */
  public String is(String value) {
    return name + " = " + value;
  }

  /**
   * Is string.
   *
   * @param value the value
   * @return the string
   */
  public String is(long value) {
    return is(Long.toString(value));
  }

  /**
   * Is string.
   *
   * @param value the value
   * @return the string
   */
  public String is(int value) {
    return is(Integer.toString(value));
  }

  /**
   * Is less than string.
   *
   * @param value the value
   * @return the string
   */
  public String isLessThan(long value) {
    return name + " < " + value;
  }

  /**
   * Is greater than string.
   *
   * @param value the value
   * @return the string
   */
  public String isGreaterThan(long value) {
    return name + " > " + value;
  }

  /**
   * To string string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Gets data description.
   *
   * @return the data description
   */
  public abstract String getDataDescription();

  /**
   * Gets value.
   *
   * @param results the results
   * @return the value
   * @throws SQLException the sql exception
   */
  public abstract T getValue(ResultSet results) throws SQLException;

  /**
   * Update value.
   *
   * @param resultSet the result set
   * @param newValue  the new value
   * @throws SQLException the sql exception
   */
  public abstract void updateValue(ResultSet resultSet, T newValue) throws SQLException;

}
