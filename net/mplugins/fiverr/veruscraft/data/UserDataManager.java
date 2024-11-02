package net.mplugins.fiverr.veruscraft.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.mplugins.fiverr.veruscraft.VerusCraft;
import net.mplugins.pluginsapi.MPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class UserDataManager {
   private static final MPlugin plugin = VerusCraft.getInstance();
   private static UserDataManager instance;
   private final Map<UUID, Integer> lastPlaytimes = new HashMap();
   private final File configFile;
   private final FileConfiguration config;

   private UserDataManager() {
      this.configFile = new File(plugin.getDataFolder(), "data/wallets.yml");
      this.config = YamlConfiguration.loadConfiguration(this.configFile);
   }

   public void saveUserTimesToConfig(OfflinePlayer player, int seconds) {
      this.config.set(String.valueOf(player.getUniqueId()) + ".max-time", seconds + this.getMaxTime(player));
      this.config.set(String.valueOf(player.getUniqueId()) + ".last-playtime", this.getLastPlaytime(player));
      this.saveConfig();
   }

   public int getMaxTime(OfflinePlayer player) {
      return this.config.getInt(String.valueOf(player.getUniqueId()) + ".max-time");
   }

   public void saveLastPlaytime(OfflinePlayer player, int lastPlaytime) {
      this.lastPlaytimes.put(player.getUniqueId(), lastPlaytime);
   }

   public int getLastPlaytime(OfflinePlayer player) {
      int lastPlaytime = (Integer)this.lastPlaytimes.getOrDefault(player.getUniqueId(), this.config.getInt(String.valueOf(player.getUniqueId()) + ".last-playtime"));
      this.lastPlaytimes.put(player.getUniqueId(), lastPlaytime);
      return lastPlaytime;
   }

   public String getWalletAddress(OfflinePlayer player) {
      if (!this.hasWalletLinked(player)) {
         throw new UnsupportedOperationException("Player has no wallet linked!");
      } else {
         return this.config.getString(String.valueOf(player.getUniqueId()) + ".wallet");
      }
   }

   public boolean link(Player player, String walletAddress) {
      if (!this.hasWalletLinked(player)) {
         this.config.set(String.valueOf(player.getUniqueId()) + ".wallet", walletAddress);
         this.saveConfig();
         return true;
      } else {
         return false;
      }
   }

   public boolean hasWalletLinked(OfflinePlayer player) {
      return this.config.contains(String.valueOf(player.getUniqueId()) + ".wallet");
   }

   private void saveConfig() {
      try {
         this.config.save(this.configFile);
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static UserDataManager getInstance() {
      if (instance == null) {
         instance = new UserDataManager();
      }

      return instance;
   }
}
