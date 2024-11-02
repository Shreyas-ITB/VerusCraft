package net.mplugins.pluginsapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import net.mplugins.pluginsapi.core.PropertiesReader;
import net.mplugins.pluginsapi.core.chat.TextFormatter;
import net.mplugins.pluginsapi.core.update.UpdaterChecker;
import org.bukkit.plugin.java.JavaPlugin;

public class MPlugin extends JavaPlugin {
   private static final long UPDATE_INTERVAL = 864000L;
   private static MPlugin instance;
   private PropertiesReader propertiesReader;
   private TextFormatter textFormatter;

   public final void onLoad() {
      instance = this;
      this.readProperties();
      this.prepareConfigurationFile();
      this.loading();
   }

   public final void onEnable() {
      if (this instanceof PremiumMPlugin && !hasValidCheckSum(this)) {
         this.getLogger().severe("Seems like this plugin has been modified. Shutting down...");
         this.getServer().getPluginManager().disablePlugin(this);
      } else {
         this.textFormatter = new TextFormatter(this);
         this.starting();
         new UpdaterChecker(this, 864000L);
      }
   }

   public final void onDisable() {
      this.stopping();
   }

   private void readProperties() {
      Properties defaults = new Properties();
      defaults.setProperty("color", "&7");
      defaults.setProperty("accent-color", "&9");
      defaults.setProperty("success-color", "&a");
      defaults.setProperty("error-color", "&c");
      defaults.setProperty("prefix", "&7[&9" + this.getDescription().getName() + "&7]&r");
      defaults.setProperty("test", "test");
      this.propertiesReader = new PropertiesReader(this, defaults);
   }

   private void prepareConfigurationFile() {
      if (this.getResource("config.yml") == null) {
         this.saveConfig();
      }

      this.saveDefaultConfig();

      try {
         this.getConfig().options().setHeader(List.of("======================", "= Plugin by MPlugins =", "======================", "", "Website: https://mplugins.net", "Plugin id: " + this.getPluginId(), "Plugin version: " + this.getDescription().getVersion(), "", "This plugin is authorized for: " + this.getAuthorizedFor())).setFooter(List.of());
      } catch (NoSuchMethodError var2) {
      }

      this.saveConfig();
   }

   public TextFormatter getTextFormatter() {
      return this.textFormatter;
   }

   public InputStream getPropertiesFile() {
      return this.getResource("application.properties");
   }

   public String getAuthorizedFor() {
      return this.propertiesReader.getAuthorizedFor();
   }

   public String getPluginId() {
      return this.propertiesReader.getPluginId();
   }

   public String getChecksum() {
      return this.propertiesReader.getCheckSum();
   }

   public String getPrefix() {
      return this.propertiesReader.getPrefix();
   }

   public String getColor() {
      return this.propertiesReader.getColor();
   }

   public String getAccentColor() {
      return this.propertiesReader.getAccentColor();
   }

   public String getSuccessColor() {
      return this.propertiesReader.getSuccessColor();
   }

   public String getErrorColor() {
      return this.propertiesReader.getErrorColor();
   }

   public static MPlugin getInstance() {
      return instance;
   }

   private static boolean hasValidCheckSum(MPlugin plugin) {
      try {
         File pluginJar = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
         MessageDigest md = MessageDigest.getInstance("MD5");
         String hex = checksum(pluginJar.getPath(), md);
         System.out.println("checksum: " + hex);
         return plugin.getChecksum().equals(hex);
      } catch (NoSuchAlgorithmException | IOException | URISyntaxException var4) {
         var4.printStackTrace();
         return false;
      }
   }

   private static String checksum(String filepath, MessageDigest md) throws IOException {
      DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md);

      try {
         while(true) {
            if (dis.read() == -1) {
               md = dis.getMessageDigest();
               break;
            }
         }
      } catch (Throwable var8) {
         try {
            dis.close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      dis.close();
      StringBuilder result = new StringBuilder();
      byte[] var3 = md.digest();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         byte b = var3[var5];
         result.append(String.format("%02x", b));
      }

      return result.toString();
   }

   public void loading() {
   }

   public void starting() {
   }

   public void stopping() {
   }
}
