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

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLColumn<T> {

  public final String name;
  public final boolean nullable;
  public final T defaultValue;
  public final boolean primaryKey;

  protected SQLColumn(String name, boolean nullable, T defaultValue) {
    this(name, nullable, defaultValue, false);
  }

  protected SQLColumn(String name, boolean nullable, T defaultValue, boolean primaryKey) {
    this.name = name;
    this.nullable = nullable;
    this.defaultValue = defaultValue;
    this.primaryKey = primaryKey;
  }

  protected String nullable() {
    return nullable ? "" : " NOT NULL";
  }

  public String is(String value) {
    return name + " = " + value;
  }

  public String is(long value) {
    return is(Long.toString(value));
  }

  public String is(int value) {
    return is(Integer.toString(value));
  }

  public String isLessThan(long value) {
    return name + " < " + value;
  }

  public String isGreaterThan(long value) {
    return name + " > " + value;
  }

  @Override
  public String toString() {
    return name;
  }

  public abstract String getDataDescription();

  public abstract T getValue(ResultSet results) throws SQLException;

  public abstract void updateValue(ResultSet resultSet, T newValue) throws SQLException;

}
