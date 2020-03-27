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
import tv.AshDev.AGPB.database.mariadb.SQLColumn;

public class LongColumn extends SQLColumn<Long> {

  public LongColumn(String name, boolean nullable, long defaultValue) {
    this(name, nullable, defaultValue, false);
  }

  public LongColumn(String name, boolean nullable, long defaultValue, boolean primaryKey) {
    super(name, nullable, defaultValue, primaryKey);
  }

  @Override
  public String getDataDescription() {
    return "BIGINT" + (defaultValue == null ? "" : " DEFAULT " + defaultValue) + nullable() + (
        primaryKey ? " PRIMARY KEY" : "");
  }

  @Override
  public Long getValue(ResultSet results) throws SQLException {
    return results.getLong(name);
  }

  @Override
  public void updateValue(ResultSet results, Long newValue) throws SQLException {
    results.updateLong(name, newValue);
  }

}
