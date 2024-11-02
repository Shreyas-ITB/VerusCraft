package net.mplugins.pluginsapi.core.chat;

import net.mplugins.pluginsapi.MPlugin;
import org.bukkit.ChatColor;

public class TextFormatter {
   private final String prefix;
   private final String primaryColor;
   private final String accentColor;
   private final String successColor;
   private final String errorColor;

   public TextFormatter(MPlugin plugin) {
      this.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getPrefix().trim() + " ");
      this.primaryColor = ChatColor.translateAlternateColorCodes('&', plugin.getColor());
      this.accentColor = ChatColor.translateAlternateColorCodes('&', plugin.getAccentColor());
      this.successColor = ChatColor.translateAlternateColorCodes('&', plugin.getSuccessColor());
      this.errorColor = ChatColor.translateAlternateColorCodes('&', plugin.getErrorColor());
   }

   public String formatSuccess(String message) {
      return this.formatSuccess(true, message);
   }

   public String formatSuccess(boolean usePrefix, String message) {
      return this.prefix + this.successColor + message;
   }

   public String formatError(String message) {
      return this.formatError(true, message);
   }

   public String formatError(boolean usePrefix, String message) {
      return this.prefix + this.errorColor + message;
   }

   public String format(String message) {
      return this.format(true, message);
   }

   public String format(boolean usePrefix, String message) {
      StringBuilder stringBuilder = new StringBuilder();
      String[] var4 = message.split(" ");
      int var5 = var4.length;

      String var10000;
      for(int var6 = 0; var6 < var5; ++var6) {
         String str = var4[var6];
         if (str.startsWith("!!")) {
            var10000 = this.accentColor;
            str = var10000 + str.replaceFirst("!!", "");
         }

         if (str.startsWith("++")) {
            var10000 = this.successColor;
            str = var10000 + str.replaceFirst("\\+\\+", "");
         }

         if (str.startsWith("--")) {
            var10000 = this.errorColor;
            str = var10000 + str.replaceFirst("--", "");
         }

         stringBuilder.append(ChatColor.translateAlternateColorCodes('&', this.primaryColor)).append(str).append(" ");
      }

      var10000 = this.prefix;
      return var10000 + stringBuilder.toString();
   }
}
