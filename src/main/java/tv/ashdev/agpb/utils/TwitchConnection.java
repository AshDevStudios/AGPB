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

package tv.ashdev.agpb.utils;

import kong.unirest.Unirest;
import tv.ashdev.agpb.data.TwitchClientID;

/**
 * The type Twitch connection.
 */
public class TwitchConnection {

  private static final String APILink = "https://api.twitch.tv/kraken/";
  /**
   * The Client id.
   */
  TwitchClientID clientID = new TwitchClientID();

  /**
   * Connection string.
   *
   * @param url the url
   * @return the string
   */
  public String Connection(String url) {
    return Unirest.get(APILink + url)
        .header("Client-ID", clientID.getId())
        .header("Accept", "application/vnd.twitchtv.v5+json")
        .asString()
        .getBody();
  }

  /**
   * Clip data string.
   *
   * @param url     the url
   * @param channel the channel
   * @return the string
   */
  public String ClipData(String url, String channel) {
    return Unirest.get(APILink + url)
        .header("Client-ID", clientID.getId())
        .header("Accept", "application/vnd.twitchtv.v5+json")
        .queryString("channel", channel)
        .queryString("period", "day")
        .asString()
        .getBody();
  }

}
