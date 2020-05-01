package me.skijearz.hcDeathTrade;

import org.bukkit.*;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DeathListener implements Listener {
   private final HardcoreDeathTrade HardCoreDeathTradeInstance;

    /**
     * Constructor which gets an instance of the plugin to get the namespacedkeys
     * @param pluginInstance Instance of the Plugin gets passed to the constructor through the pluginmanager#registerevents
     */
    public DeathListener(HardcoreDeathTrade pluginInstance){
        this.HardCoreDeathTradeInstance = pluginInstance;
    }

    /**
     * Eventlistener, listen on the playerDeath
     * @param e, Deathevent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        incrementDeathCount(e.getEntity().getPlayer());
        setDeathTime(e.getEntity().getPlayer());
        Bukkit.broadcastMessage(ChatColor.RED + "[HCDeathTrade] " + ChatColor.GREEN + e.getEntity().getName() + ChatColor.WHITE + " ist bereits " + ChatColor.RED + getDeathCount(e.getEntity().getPlayer()) + ChatColor.WHITE + " mal gestorben!");

    }

    /**
     * Method which returns the amount of deaths of a player
     * @param p, Player object
     * @return int
     */
    private int getDeathCount(Player p){
            return p.getPlayer().getPersistentDataContainer().get(this.HardCoreDeathTradeInstance.getDeathCountKey(),PersistentDataType.INTEGER);

    }

    /**
     * Method which sets the deathTime on each Player who dies
     * @param p
     */
    private void setDeathTime(Player p){
        //Sets the deathTime to the next day
        /**
         * TODO: Maybe add config support to set the deathTime to a user-choice time
         */
        //Long timeUntilRespawn = (System.currentTimeMillis()/1000)+Math.abs((LocalTime.now().toSecondOfDay())-86400);
        p.getPersistentDataContainer().set(this.HardCoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG,(System.currentTimeMillis()/1000)+25);
    }

    /**
     * Increments the total amount of deaths on a player
     * @param p, Player object which gets passed to the method through the playerDeathEvent
     */
    private void incrementDeathCount(Player p){
        if(!p.getPlayer().getPersistentDataContainer().has(this.HardCoreDeathTradeInstance.getDeathCountKey(),PersistentDataType.INTEGER)){
            p.getPersistentDataContainer().set(this.HardCoreDeathTradeInstance.getDeathCountKey(),PersistentDataType.INTEGER,1);
        }else{
           int Deathcount = p.getPersistentDataContainer().get(this.HardCoreDeathTradeInstance.getDeathCountKey(),PersistentDataType.INTEGER);
            p.getPersistentDataContainer().set(this.HardCoreDeathTradeInstance.getDeathCountKey(),PersistentDataType.INTEGER,Deathcount+1);
        }
    }
}
