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

package tv.ashdev.agpb.database;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.Collection;
import java.util.Collections;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import org.jetbrains.annotations.NotNull;
import tv.ashdev.agpb.Constants;
import tv.ashdev.agpb.database.mariadb.DataManager;
import tv.ashdev.agpb.database.mariadb.DatabaseConnector;
import tv.ashdev.agpb.database.mariadb.SQLColumn;
import tv.ashdev.agpb.database.mariadb.columns.LongColumn;
import tv.ashdev.agpb.database.mariadb.columns.StringColumn;
import tv.ashdev.agpb.utils.FixedCache;

/**
 * The type Guild settings data manager.
 */
public class GuildSettingsDataManager extends DataManager implements GuildSettingsManager {

  public static final int PREFIX_MAX_LENGTH = 20;
  private static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("UTC+10");
  private static final String SETTINGS_TITLE = "\uD83D\uDCCA Server Settings"; // 📊

  /**
   * The constant GUILD_ID.
   */
  public final static SQLColumn<Long> GUILD_ID = new LongColumn(
      "GUILD_ID", false, 0L, true
  );

  /**
   * The constant PREFIX.
   */
  public final static SQLColumn<String> PREFIX = new StringColumn(
      "PREFIX", true, null, PREFIX_MAX_LENGTH
  );
  /**
   * The constant TIMEZONE.
   */
  public final static SQLColumn<String> TIMEZONE = new StringColumn(
      "TIMEZONE", true, null, 32
  );
  /**
   * The constant WELCOME_MSG.
   */
  public final static SQLColumn<String> WELCOME_MSG = new StringColumn(
      "WELCOME_MSG", true, null, 255
  );

  // Cache
  private final FixedCache<Long, GuildSettings> cache = new FixedCache<>(
      Constants.DEFAULT_CACHE_SIZE);
  private final GuildSettings blankSettings = new GuildSettings();

  /**
   * Instantiates a new Guild settings data manager.
   *
   * @param connector the connector
   */
  public GuildSettingsDataManager(DatabaseConnector connector) {
    super(connector, "GUILD_SETTINGS");
  }

  /**
   * Gets settings.
   *
   * @param guild the guild
   * @return the settings
   */
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

  /**
   * Gets settings display.
   *
   * @param guild the guild
   * @return the settings display
   */
  public Field getSettingsDisplay(Guild guild) {
    GuildSettings settings = getSettings(guild);
    return new Field(SETTINGS_TITLE,
        "Prefix: `" + (settings.prefix == null ? Constants.PREFIX : settings.prefix) + "`\n"
            + "Timezone: **" + settings.timezone + "**\n"
            + "Welcome msg: `" + settings.welcome_msg + "`", true);
  }

  /**
   * Has settings boolean.
   *
   * @param guild the guild
   * @return the boolean
   */
  public boolean hasSettings(Guild guild) {
    return read(selectAll(GUILD_ID.is(guild.getIdLong())), ResultSet::next);
  }

  /**
   * Sets prefix.
   *
   * @param guild  the guild
   * @param prefix the prefix
   */
  public void setPrefix(Guild guild, String prefix) {
    invalidateCache(guild);
    readWrite(select(GUILD_ID.is(guild.getIdLong()), GUILD_ID, PREFIX), rs -> {
      if (rs.next()) {
        PREFIX.updateValue(rs, prefix);
        rs.updateRow();
      } else {
        rs.moveToInsertRow();
        GUILD_ID.updateValue(rs, guild.getIdLong());
        PREFIX.updateValue(rs, prefix);
        rs.insertRow();
      }
    });
  }

  /**
   * Sets timezone.
   *
   * @param guild  the guild
   * @param zoneId the zone id
   */
  public void setTimezone(Guild guild, ZoneId zoneId) {
    invalidateCache(guild);
    readWrite(select(GUILD_ID.is(guild.getIdLong()), GUILD_ID, TIMEZONE), rs -> {
      if (rs.next()) {
        TIMEZONE.updateValue(rs, zoneId.getId());
        rs.updateRow();
      } else {
        rs.moveToInsertRow();
        GUILD_ID.updateValue(rs, guild.getIdLong());
        TIMEZONE.updateValue(rs, zoneId.getId());
        rs.insertRow();
      }
    });
  }

  /**
   * Sets welcome msg.
   *
   * @param guild the guild
   * @param msg   the msg
   */
  public void setWelcomeMsg(Guild guild, String msg) {
    invalidateCache(guild);
    readWrite(select(GUILD_ID.is(guild.getIdLong()), GUILD_ID, WELCOME_MSG), rs -> {
      if (rs.next()) {
        WELCOME_MSG.updateValue(rs, msg);
        rs.updateRow();
      } else {
        rs.moveToInsertRow();
        GUILD_ID.updateValue(rs, guild.getIdLong());
        WELCOME_MSG.updateValue(rs, msg);
        rs.insertRow();
      }
    });
  }

  private void invalidateCache(Guild guild) {
    invalidateCache(guild.getIdLong());
  }

  private void invalidateCache(long guildId) {
    cache.pull(guildId);
  }

  public String getWelcomeMsg(Guild guild) {
    return getSettings(guild).welcome_msg;
  }

  private static class GuildSettings implements GuildSettingsProvider {

    private final String prefix;
    private final ZoneId timezone;
    private final String welcome_msg;

    private GuildSettings() {
      this.prefix = null;
      this.timezone = DEFAULT_TIMEZONE;
      this.welcome_msg = null;
    }

    private GuildSettings(ResultSet rs) throws SQLException {
      this.prefix = PREFIX.getValue(rs);
      this.welcome_msg = WELCOME_MSG.getValue(rs);
      String str = TIMEZONE.getValue(rs);
      ZoneId zid;
      if (str == null) {
        zid = DEFAULT_TIMEZONE;
      } else {
        try {
          zid = ZoneId.of(str);
        } catch (ZoneRulesException e) {
          zid = DEFAULT_TIMEZONE;
        }
      }
      this.timezone = zid;
    }

    /**
     * Gets timezone.
     *
     * @return the timezone
     */
    public ZoneId getTimezone() {
      return timezone;
    }

    /**
     * Gets welcome msg.
     *
     * @return the welcome msg
     */
    public String getWelcomeMsg() {
      return welcome_msg;
    }

    /**
     * Gets prefixes.
     *
     * @return the prefixes
     */
    @Override
    public Collection<String> getPrefixes() {
      if (prefix == null || prefix.isEmpty()) {
        return null;
      }
      return Collections.singleton(prefix);
    }

  }
}
