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

package tv.AshDev.AGPB;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Constants {

  public final static OffsetDateTime STARTUP = OffsetDateTime.now();
  public final static String PREFIX = "-";
  public final static String SUCCESS = "\\uD83D\\uDC4D"; //Thumbs Up
  public final static String WARNING = "\\uD83D\\uDC4E"; //Thumbs Down
  public final static String ERROR = "\\u270B"; //Raised Hand
  public final static String BOT_EMOJI = "<:AD_BOT:692774673582129235>";
  public final static String OWNER_ID = "160269653136900096";
  public final static int DEFAULT_CACHE_SIZE = 8000;
  public final static String SERVER_INVITE = "https://discord.gg/xMsSev4";
  public final static Collection<GatewayIntent> GATEWAY_INTENTS = Arrays.asList(
      GatewayIntent.GUILD_MEMBERS,
      GatewayIntent.GUILD_EMOJIS,
      GatewayIntent.GUILD_PRESENCES,
      GatewayIntent.DIRECT_MESSAGE_REACTIONS,
      GatewayIntent.DIRECT_MESSAGE_TYPING,
      GatewayIntent.DIRECT_MESSAGES,
      GatewayIntent.GUILD_BANS,
      GatewayIntent.GUILD_INVITES,
      GatewayIntent.GUILD_MESSAGE_REACTIONS,
      GatewayIntent.GUILD_MESSAGE_TYPING,
      GatewayIntent.GUILD_MESSAGES,
      GatewayIntent.GUILD_VOICE_STATES
  );


}
