package me.skijearz.hcDeathTrade;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class which starts a scheduler which checks if a player should be respawned online players only since the joinListener checks that on join
 */
public class DeathTradeRunnable extends BukkitRunnable {
    private final HardcoreDeathTrade HardcoreDeathTradeInstance;
    public DeathTradeRunnable(HardcoreDeathTrade pluginInstance){
        this.HardcoreDeathTradeInstance = pluginInstance;
    }
    @Override
    public void run() {
        if(!Bukkit.getServer().getOnlinePlayers().isEmpty()){
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                Long TimeToRespawn = p.getPersistentDataContainer().get(this.HardcoreDeathTradeInstance.getDeathTimeKey(), PersistentDataType.LONG);
                if(TimeToRespawn != 0 && TimeToRespawn < System.currentTimeMillis()/1000){
                    DeathTradeRespawn.respawnPlayer(p,this.HardcoreDeathTradeInstance);

                }
            }
        }

    }
}
