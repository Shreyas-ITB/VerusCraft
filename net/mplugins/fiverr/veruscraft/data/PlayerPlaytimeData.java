package net.mplugins.fiverr.veruscraft.data;

public record PlayerPlaytimeData(String time, String verus, String addr) {
   public PlayerPlaytimeData(String time, String verus, String addr) {
      this.time = time;
      this.verus = verus;
      this.addr = addr;
   }

   public String time() {
      return this.time;
   }

   public String verus() {
      return this.verus;
   }

   public String addr() {
      return this.addr;
   }
}
