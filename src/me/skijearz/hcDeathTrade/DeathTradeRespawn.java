package me.skijearz.hcDeathTrade;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

//Static class for the playerRespawn, takes the player to be respawned and an instance from the Plugin
public class DeathTradeRespawn {
    /**
     * Static class-method to respawn a player from all other the plugin and resets the deathTimer
     * @param p Player object
     * @param pluginInstance Plugin instance to get the namespacedkeys
     */
    public static void respawnPlayer(Player p,HardcoreDeathTrade pluginInstance){
        p.spigot().respawn();
        //Check if the player has a bedspawn if not spawn him at the world spawn
        if(p.getBedSpawnLocation() == null){
            p.teleport(p.getServer().getWorld("world").getSpawnLocation());
        }else{
            p.teleport(p.getBedSpawnLocation());
        }
        //set the gamemode to survivial and remove the deathtimer from the persistendatacontaiener
        p.setGameMode(GameMode.SURVIVAL);
        p.getPersistentDataContainer().set(pluginInstance.getDeathTimeKey(), PersistentDataType.LONG,0L);
    }
}
