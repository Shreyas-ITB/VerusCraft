package net.mplugins.pluginsapi.core;

import java.io.IOException;
import java.util.Properties;
import net.mplugins.pluginsapi.MPlugin;

public class PropertiesReader {
   private final Properties properties;

   public PropertiesReader(MPlugin plugin) {
      this(plugin, (Properties)null);
   }

   public PropertiesReader(MPlugin plugin, Properties defaults) {
      this.properties = new Properties(defaults);

      try {
         this.properties.load(plugin.getPropertiesFile());
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   public String getPluginId() {
      return this.getProperty("plugin-id");
   }

   public String getAuthorizedFor() {
      return this.getProperty("authorized-for");
   }

   public String getCheckSum() {
      return this.getProperty("checksum");
   }

   public String getPrefix() {
      return this.getProperty("prefix");
   }

   public String getColor() {
      return this.getProperty("color");
   }

   public String getAccentColor() {
      return this.getProperty("accent-color");
   }

   public String getSuccessColor() {
      return this.getProperty("success-color");
   }

   public String getErrorColor() {
      return this.getProperty("error-color");
   }

   private String getProperty(String key) {
      String property = this.properties.getProperty(key);
      return property == null ? "Property " + key + " not found." : property;
   }
}
