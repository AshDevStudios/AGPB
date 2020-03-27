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

package tv.AshDev.AGPB.database;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import org.jetbrains.annotations.NotNull;
import tv.AshDev.AGPB.Constants;
import tv.AshDev.AGPB.database.mariadb.DataManager;
import tv.AshDev.AGPB.database.mariadb.DatabaseConnector;
import tv.AshDev.AGPB.database.mariadb.SQLColumn;
import tv.AshDev.AGPB.database.mariadb.columns.LongColumn;
import tv.AshDev.AGPB.database.mariadb.columns.StringColumn;
import tv.AshDev.AGPB.utils.FixedCache;

public class GuildSettingsDataManager extends DataManager implements GuildSettingsManager {

  private static final int PREFIX_MAX_LENGTH = 20;
  private static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("UTC+10");
  private static final String SETTINGS_TITLE = "\uD83D\uDCCA Server Settings"; // 📊

  public final static SQLColumn<Long> GUILD_ID = new LongColumn("GUILD_ID", false, 0L, true);

  public final static SQLColumn<String> PREFIX = new StringColumn("PREFIX", true, null,
      PREFIX_MAX_LENGTH);

  // Cache
  private final FixedCache<Long, GuildSettings> cache = new FixedCache<>(
      Constants.DEFAULT_CACHE_SIZE);
  private final GuildSettings blankSettings = new GuildSettings();

  public GuildSettingsDataManager(DatabaseConnector connector) {
    super(connector, "GUILD_SETTINGS");
  }

  @NotNull
  @Override
  public GuildSettings getSettings(Guild guild) {
    if (cache.contains(guild.getIdLong())) {
      return cache.get(guild.getIdLong());
    }
    GuildSettings settings = read(selectAll(GUILD_ID.is(guild.getIdLong())),
        rs -> rs.next() ? new GuildSettings(rs) : blankSettings);
    cache.put(guild.getIdLong(), settings);
    return settings;
  }

  public Field getSettingsDisplay(Guild guild) {
    GuildSettings settings = getSettings(guild);
    return new Field(SETTINGS_TITLE,
        "Prefix: `" + (settings.prefix == null ? Constants.PREFIX : settings.prefix) + "`", true);
  }

  private class GuildSettings implements GuildSettingsProvider {

    private final String prefix;

    private GuildSettings() {
      this.prefix = null;
    }

    private GuildSettings(ResultSet rs) throws SQLException {
      this.prefix = PREFIX.getValue(rs);
    }

    @Override
    public Collection<String> getPrefixes() {
      if (prefix == null || prefix.isEmpty()) {
        return null;
      }
      return Collections.singleton(prefix);
    }

  }
}
