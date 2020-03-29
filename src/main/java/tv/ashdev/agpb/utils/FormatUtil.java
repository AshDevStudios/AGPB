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

import java.util.List;
import java.util.function.Function;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class FormatUtil {

  private final static String MULTIPLE_FOUND = "**Multiple %s found matching \"%s\":**";
  private final static String CMD_EMOJI = "\uD83D\uDCDC";

  public static String filterEveryone(String input) {
    return input.replace("\u202E", "")
        .replace("@everyone", "@\u0435veryone")
        .replace("@here", "@h\u0435ere")
        .replace("discord.gg/", "dis\u0441ord.gg");
  }

  public static String formatMessage(Message m) {
    StringBuilder sb = new StringBuilder(m.getContentRaw());
    m.getAttachments().forEach(attachment -> sb.append("\n").append(attachment.getUrl()));
    return sb.length() > 2048 ? sb.toString().substring(0, 2040) : sb.toString();
  }

  public static String formatFullUserId(long userid) {
    return "<@" + userid + "> (ID:" + userid + ")";
  }

  public static String formatUser(User user) {
    return filterEveryone("**" + user.getName() + "**#" + user.getDiscriminator());
  }

  public static String formateFullUser(User user) {
    return filterEveryone(
        "**" + user.getName() + "**#" + user.getDiscriminator() + " (ID:" + user.getId() + ")");
  }

  public static String capitalize(String input) {
    if (input == null || input.isEmpty()) {
      return "";
    }
    if (input.length() == 1) {
      return input.toUpperCase();
    }
    return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
  }

  public static String join(String delimiter, char... items) {
    if (items == null || items.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder().append(items[0]);

    for (int i = 1; i < items.length; i++) {
      sb.append(delimiter).append(items[i]);
    }
    return sb.toString();
  }

  public static <T> String join(String delimiter, Function<T, String> function, T... items) {
    if (items == null || items.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder(function.apply(items[0]));
    for (int i = 1; i < items.length; i++) {
      sb.append(delimiter).append(function.apply(items[i]));
    }
    return sb.toString();
  }

  public static String listOfRoles(List<Role> list, String query) {
    StringBuilder out = new StringBuilder(String.format(MULTIPLE_FOUND, "roles", query));
    for (int i = 0; i < 6 && i < list.size(); i++) {
      out.append("\n - ").append(list.get(i).getName()).append(" (ID:").append(list.get(i).getId())
          .append(")");
    }
    if (list.size() > 6) {
      out.append("\n**And ").append(list.size() - 6).append(" more...**");
    }
    return out.toString();
  }

  public static String listOfUsers(List<User> list, String query) {
    StringBuilder out = new StringBuilder(String.format(MULTIPLE_FOUND, "users", query));
    for (int i = 0; i < 6 && i < list.size(); i++) {
      out.append("\n - **").append(list.get(i).getName()).append("**#")
          .append(list.get(i).getDiscriminator()).append(" (ID:").append(list.get(i).getId());
    }
    if (list.size() > 6) {
      out.append("\n**And ").append(list.size() - 6).append(" more...**");
    }
    return out.toString();
  }

  public static String listOfMembers(List<Member> list, String query) {
    StringBuilder out = new StringBuilder(String.format(MULTIPLE_FOUND, "members", query));
    for (int i = 0; i < 6 && i < list.size(); i++) {
      out.append("\n - **").append(list.get(i).getUser().getName()).append("**#")
          .append(list.get(i).getUser().getDiscriminator()).append(" (ID:")
          .append(list.get(i).getId());
    }
    if (list.size() > 6) {
      out.append("\n**And ").append(list.size() - 6).append(" more...**");
    }
    return out.toString();
  }

  public static String secondsToTime(long timeseconds) {
    StringBuilder builder = new StringBuilder();
    int years = (int) (timeseconds / (60 * 60 * 24 * 365));
    if (years > 0) {
      builder.append("**").append(years).append("** years, ");
      timeseconds = timeseconds % (60 * 60 * 24 * 365);
    }
    int weeks = (int) (timeseconds / (60 * 60 * 24 * 7));
    if (years > 0) {
      builder.append("**").append(weeks).append("** weeks, ");
      timeseconds = timeseconds % (60 * 60 * 24 * 7);
    }
    int days = (int) (timeseconds / (60 * 60 * 24));
    if (years > 0) {
      builder.append("**").append(days).append("** days, ");
      timeseconds = timeseconds % (60 * 60 * 24);
    }
    int hours = (int) (timeseconds / (60 * 60));
    if (years > 0) {
      builder.append("**").append(hours).append("** hours, ");
      timeseconds = timeseconds % (60 * 60);
    }
    int minutes = (int) (timeseconds / (60));
    if (years > 0) {
      builder.append("**").append(minutes).append("** minutes, ");
      timeseconds = timeseconds % (60 * 60 * 24 * 365);
    }
    if (timeseconds > 0) {
      builder.append("**").append(timeseconds).append("** seconds");
    }
    String str = builder.toString();
    if (str.endsWith(", ")) {
      str = str.substring(0, str.length() - 2);
    }
    if (str.isEmpty()) {
      str = "**No time**";
    }
    return str;
  }

  public static String secondsToTimeCompact(long timeseconds) {
    StringBuilder builder = new StringBuilder();
    int years = (int) (timeseconds / (60 * 60 * 24 * 365));
    if (years > 0) {
      builder.append("**").append(years).append("**y, ");
      timeseconds = timeseconds % (60 * 60 * 24 * 365);
    }
    int weeks = (int) (timeseconds / (60 * 60 * 24 * 7));
    if (years > 0) {
      builder.append("**").append(weeks).append("**w, ");
      timeseconds = timeseconds % (60 * 60 * 24 * 7);
    }
    int days = (int) (timeseconds / (60 * 60 * 24));
    if (years > 0) {
      builder.append("**").append(days).append("**d, ");
      timeseconds = timeseconds % (60 * 60 * 24);
    }
    int hours = (int) (timeseconds / (60 * 60));
    if (years > 0) {
      builder.append("**").append(hours).append("**h, ");
      timeseconds = timeseconds % (60 * 60);
    }
    int minutes = (int) (timeseconds / (60));
    if (years > 0) {
      builder.append("**").append(minutes).append("**m, ");
      timeseconds = timeseconds % (60 * 60 * 24 * 365);
    }
    if (timeseconds > 0) {
      builder.append("**").append(timeseconds).append("**s");
    }
    String str = builder.toString();
    if (str.endsWith(", ")) {
      str = str.substring(0, str.length() - 2);
    }
    if (str.isEmpty()) {
      str = "**No time**";
    }
    return str;
  }

}
