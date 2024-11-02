package net.mplugins.fiverr.veruscraft.commands;

import net.mplugins.fiverr.veruscraft.VerusCraft;
import net.mplugins.fiverr.veruscraft.data.UserDataManager;
import net.mplugins.pluginsapi.MPlugin;
import net.mplugins.pluginsapi.core.chat.TextFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkWalletCommand implements CommandExecutor {
   private static final MPlugin plugin = VerusCraft.getInstance();
   private static final TextFormatter formatter;

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (args.length < 1) {
            player.sendMessage(formatter.format("--Usage: --/" + label + " --<wallet-addr>"));
            return true;
         } else if (!args[0].matches("[0-9A-Za-z]{34}")) {
            String invalidWallet = args[0];
            if (args[0].length() >= 15) {
               invalidWallet = args[0].substring(0, 15) + "...";
            }

            player.sendMessage(formatter.format("--" + invalidWallet + " does not look like a valid address!"));
            return true;
         } else {
            if (!UserDataManager.getInstance().link(player, args[0])) {
               player.sendMessage(formatter.format("You --already linked your wallet!"));
            } else {
               player.sendMessage(formatter.format("Your wallet has been linked ++successfully"));
            }

            return true;
         }
      } else {
         sender.sendMessage("§cOnly players can execute this command!");
         return true;
      }
   }

   static {
      formatter = plugin.getTextFormatter();
   }
}
