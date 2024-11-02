package net.mplugins.pluginsapi.core.update;

import net.mplugins.pluginsapi.MPlugin;

public class UpdaterChecker {
   private final String url = "https://plugin.mplugins.net/update/%s";

   public UpdaterChecker(MPlugin plugin, long interval) {
      plugin.getLogger().info("Checking for updates.");
   }
}
