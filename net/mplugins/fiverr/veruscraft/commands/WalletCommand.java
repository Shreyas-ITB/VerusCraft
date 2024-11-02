package net.mplugins.fiverr.veruscraft.commands;

import java.text.DecimalFormat;
import net.mplugins.fiverr.veruscraft.VerusCraft;
import net.mplugins.fiverr.veruscraft.data.ActivityManager;
import net.mplugins.fiverr.veruscraft.data.CommunicationManager;
import net.mplugins.fiverr.veruscraft.data.UserDataManager;
import net.mplugins.pluginsapi.MPlugin;
import net.mplugins.pluginsapi.core.chat.TextFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WalletCommand implements CommandExecutor {
   private static final MPlugin plugin = VerusCraft.getInstance();
   private static final TextFormatter formatter;
   private static final ActivityManager activityManager;
   private static final DecimalFormat decimalFormat;

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         int currentPlaytime = (Integer)ActivityManager.getInstance().getActivitySeconds().getOrDefault(player.getUniqueId(), 0) + UserDataManager.getInstance().getMaxTime(player);
         String timeString = currentPlaytime + " seconds";
         if (currentPlaytime > 3600) {
            timeString = currentPlaytime / 3600 + " !!hour" + (currentPlaytime > 7200 ? "s" : "");
         } else if (currentPlaytime > 60) {
            timeString = currentPlaytime / 60 + " !!minute" + (currentPlaytime > 120 ? "s" : "");
         }

         player.sendMessage(formatter.format("Playtime: !!" + timeString));
         DecimalFormat var10002 = decimalFormat;
         player.sendMessage(formatter.format("Earned: ++" + var10002.format(CommunicationManager.convertSecondsToCurrencyEarned(currentPlaytime))));
         return true;
      } else {
         sender.sendMessage(formatter.formatError("Only players can execute this command"));
         return true;
      }
   }

   static {
      formatter = plugin.getTextFormatter();
      activityManager = ActivityManager.getInstance();
      decimalFormat = new DecimalFormat("0.########VRSC");
   }
}
