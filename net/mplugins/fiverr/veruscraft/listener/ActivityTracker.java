package net.mplugins.fiverr.veruscraft.listener;

import net.mplugins.fiverr.veruscraft.data.ActivityManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ActivityTracker implements Listener {
   private static final ActivityManager activityManager = ActivityManager.getInstance();

   @EventHandler
   public void onMove(PlayerMoveEvent event) {
      Location from = event.getFrom();
      Location to = event.getTo();
      if (to != null) {
         if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            activityManager.startTracking(event.getPlayer(), 2);
         }

      }
   }

   @EventHandler
   public void onInteract(PlayerItemConsumeEvent event) {
      activityManager.startTracking(event.getPlayer());
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) {
      activityManager.startTracking(event.getPlayer());
   }

   @EventHandler
   public void onBlockPlace(BlockPlaceEvent event) {
      activityManager.startTracking(event.getPlayer());
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      activityManager.stopTracking(event.getPlayer());
   }
}
