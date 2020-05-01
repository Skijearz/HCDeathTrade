package me.skijearz.hcDeathTrade;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalTime;

/**
 * Class which listens on the PlayerJoinEvent and checks if the player who joined needs to be respawned
 */
public class ServerJoinListenerRespawner implements Listener {
    private final HardcoreDeathTrade HardCoreDeathTradeInstance;

    /**
     * Constructor which inits the Listener and assigns the pluginInstance field.
     * @param pluginInstance Plugin instance
     */
    public ServerJoinListenerRespawner(HardcoreDeathTrade pluginInstance){
        this.HardCoreDeathTradeInstance = pluginInstance;
    }

    /**
     * Listener method, if a player joins it checks whether he should be respawned or not - if not and he is still dead message him the remaining deathTime
     * @param e PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        if(!e.getPlayer().getPersistentDataContainer().has(this.HardCoreDeathTradeInstance.getDeathTimeKey(), PersistentDataType.LONG)){
            e.getPlayer().getPersistentDataContainer().set(this.HardCoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG,0L);

        }

        Long timeUntilRespawn = e.getPlayer().getPersistentDataContainer().get(this.HardCoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG);

        if(timeUntilRespawn == 0){
            ;
        }else if(timeUntilRespawn < System.currentTimeMillis()/1000){
            DeathTradeRespawn.respawnPlayer(e.getPlayer(),this.HardCoreDeathTradeInstance);
        }else{
            LocalTime respawnTimer = LocalTime.ofSecondOfDay(timeUntilRespawn - System.currentTimeMillis()/1000);
            e.getPlayer().sendMessage(ChatColor.RED + "[HCDeathTrade] " + ChatColor.WHITE + " Zeit bis Respawn: " + ChatColor.GREEN + respawnTimer.toString());
        }


    }

}
