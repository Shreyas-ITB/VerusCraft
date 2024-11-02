package net.mplugins.fiverr.veruscraft.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.mplugins.fiverr.veruscraft.VerusCraft;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class CommunicationManager {
   private static final DecimalFormat decimalFormat = new DecimalFormat("0.########VRSC");
   private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   private static final UserDataManager userDataManager = UserDataManager.getInstance();
   private static final double CURRENCY_PER_TEN_MINUTES = 3.875E-5D;
   private static CommunicationManager instance;

   private CommunicationManager() {
   }

   public void start() {
      (new BukkitRunnable() {
         public void run() {
            Map<UUID, Integer> activitySeconds = ActivityManager.getInstance().getActivitySeconds();
            PlayerPlaytimeData[] data = new PlayerPlaytimeData[activitySeconds.size()];
            int i = 0;
            Iterator var4 = activitySeconds.entrySet().iterator();

            while(var4.hasNext()) {
               Entry<UUID, Integer> entry = (Entry)var4.next();
               OfflinePlayer player = Bukkit.getOfflinePlayer((UUID)entry.getKey());
               String walletAddress = CommunicationManager.userDataManager.getWalletAddress(player);
               int lastPlaytime = CommunicationManager.userDataManager.getLastPlaytime(player);
               int currentPlaytime = (Integer)entry.getValue() + CommunicationManager.userDataManager.getMaxTime(player);
               int playtimeDifference = currentPlaytime - lastPlaytime;
               double pending = CommunicationManager.convertSecondsToCurrencyEarned(playtimeDifference);
               System.out.println("playtimeDifference = " + playtimeDifference);
               System.out.println("pending = " + pending);
               String timeString = currentPlaytime + " seconds";
               if (currentPlaytime > 3600) {
                  timeString = currentPlaytime / 3600 + " hour" + (currentPlaytime > 7200 ? "s" : "");
               } else if (currentPlaytime > 60) {
                  timeString = currentPlaytime / 60 + " minute" + (currentPlaytime > 120 ? "s" : "");
               }

               data[i++] = new PlayerPlaytimeData(timeString, CommunicationManager.decimalFormat.format(pending), walletAddress);
               CommunicationManager.userDataManager.saveLastPlaytime(player, currentPlaytime);
            }

            CommunicationManager.this.postPlayTimeUpdate(data);
         }
      }).runTaskTimerAsynchronously(VerusCraft.getInstance(), 0L, 20L * VerusCraft.getInstance().getConfig().getLong("interval"));
   }

   private void postPlayTimeUpdate(PlayerPlaytimeData... data) {
      String json = gson.toJson(data);
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VerusCraft.getInstance().getConfig().getString("post-endpoint"))).setHeader("Content-Type", "application/json").POST(BodyPublishers.ofString(json)).build();

      try {
         client.send(request, BodyHandlers.ofString());
      } catch (InterruptedException | IOException var6) {
         throw new RuntimeException(var6);
      }
   }

   public static double convertSecondsToCurrencyEarned(int seconds) {
      return (double)seconds / 480.0D * 3.875E-5D;
   }

   public static CommunicationManager getInstance() {
      if (instance == null) {
         instance = new CommunicationManager();
      }

      return instance;
   }
}
