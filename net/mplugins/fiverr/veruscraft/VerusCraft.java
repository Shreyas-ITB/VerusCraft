package net.mplugins.fiverr.veruscraft;

import net.mplugins.fiverr.veruscraft.commands.LinkWalletCommand;
import net.mplugins.fiverr.veruscraft.commands.WalletCommand;
import net.mplugins.fiverr.veruscraft.data.ActivityManager;
import net.mplugins.fiverr.veruscraft.data.CommunicationManager;
import net.mplugins.fiverr.veruscraft.listener.ActivityTracker;
import net.mplugins.pluginsapi.MPlugin;

public final class VerusCraft extends MPlugin {
   public void starting() {
      this.getCommand("linkwallet").setExecutor(new LinkWalletCommand());
      this.getCommand("wallet").setExecutor(new WalletCommand());
      this.getServer().getPluginManager().registerEvents(new ActivityTracker(), this);
      CommunicationManager.getInstance().start();
   }

   public void stopping() {
      ActivityManager.getInstance().saveUserTimesToConfig();
   }
}
