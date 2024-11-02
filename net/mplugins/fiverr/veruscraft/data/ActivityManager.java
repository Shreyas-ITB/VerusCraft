package net.mplugins.fiverr.veruscraft.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.mplugins.fiverr.veruscraft.VerusCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActivityManager {
   private static final UserDataManager walletManager = UserDataManager.getInstance();
   private static ActivityManager instance;
   private final Map<UUID, BukkitRunnable> runnables = new HashMap();
   private final Map<UUID, Integer> activitySeconds = new HashMap();
   private final Set<UUID> currentlyTracking = new HashSet();

   private ActivityManager() {
      this.increaseActivityForTrackedPlayers();
   }

   public void startTracking(Player player) {
      this.startTracking(player, 1);
   }

   public void startTracking(final Player player, int stopAfterSeconds) {
      if (walletManager.hasWalletLinked(player)) {
         this.currentlyTracking.add(player.getUniqueId());
         if (stopAfterSeconds > -1) {
            BukkitRunnable newRunnable = new BukkitRunnable() {
               public void run() {
                  ActivityManager.this.stopTracking(player);
               }
            };
            newRunnable.runTaskLaterAsynchronously(VerusCraft.getInstance(), (long)stopAfterSeconds * 20L);
            this.stopOldRunnable(player);
            this.runnables.put(player.getUniqueId(), newRunnable);
         }

      }
   }

   public void stopTracking(Player player) {
      if (this.isBeingTracked(player)) {
         this.currentlyTracking.remove(player.getUniqueId());
         this.stopOldRunnable(player);
      }

   }

   private void increaseActivityForTrackedPlayers() {
      (new BukkitRunnable() {
         public void run() {
            Iterator var1 = ActivityManager.this.currentlyTracking.iterator();

            while(var1.hasNext()) {
               UUID uuid = (UUID)var1.next();
               Player player = Bukkit.getPlayer(uuid);
               int trackedSeconds = (Integer)ActivityManager.this.activitySeconds.getOrDefault(player.getUniqueId(), 0);
               Map var10000 = ActivityManager.this.activitySeconds;
               UUID var10001 = player.getUniqueId();
               ++trackedSeconds;
               var10000.put(var10001, trackedSeconds);
               player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§a§lCurrently earning VerusCoins"));
            }

         }
      }).runTaskTimerAsynchronously(VerusCraft.getInstance(), 0L, 20L);
   }

   private void stopOldRunnable(Player player) {
      BukkitRunnable oldRunnable = (BukkitRunnable)this.runnables.get(player.getUniqueId());
      if (oldRunnable != null) {
         oldRunnable.cancel();
      }

   }

   public void saveUserTimesToConfig() {
      Map<UUID, Integer> activitySeconds = getInstance().getActivitySeconds();
      Iterator var2 = activitySeconds.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<UUID, Integer> entry = (Entry)var2.next();
         UserDataManager.getInstance().saveUserTimesToConfig(Bukkit.getOfflinePlayer((UUID)entry.getKey()), (Integer)entry.getValue());
      }

   }

   public Map<UUID, Integer> getActivitySeconds() {
      return this.activitySeconds;
   }

   public boolean isBeingTracked(Player player) {
      return this.currentlyTracking.contains(player.getUniqueId());
   }

   public static ActivityManager getInstance() {
      if (instance == null) {
         instance = new ActivityManager();
      }

      return instance;
   }
}
