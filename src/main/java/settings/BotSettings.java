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

package settings;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Bot settings.
 */
public class BotSettings {

    // Will hold the settings for each Guild
    private final Map<Long, SettingsManager> settings = new HashMap<>();

    /**
     * Create file.
     *
     * @param guildId the guild ID
     */
    public void createFile(final Long guildId) {
        File settingFile = new File("guilds\\" + guildId + ".yml");

        final SettingsManager settingsManager = SettingsManagerBuilder
                .withYamlFile(settingFile).configurationData(GuildProperties.class)
                .useDefaultMigrationService()
                .create();

        // Puts the setting manager in the Map
        settings.put(guildId, settingsManager);
    }

    /**
     * Settings settings manager.
     *
     * @param guildId The guild ID
     * @return The settings manager for that guild
     */
    public SettingsManager getSettings(final long guildId) {
        // Returns the settings manager for this guild ID
        return settings.get(guildId);
    }
}
