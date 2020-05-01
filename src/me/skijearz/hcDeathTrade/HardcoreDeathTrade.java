package me.skijearz.hcDeathTrade;

import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class HardcoreDeathTrade extends JavaPlugin {
    private NamespacedKey deathCountKey;
    private NamespacedKey deathTimeKey;
    @Override
    public void onEnable(){
        //KeepInventory on death
        getServer().getWorld("world").setGameRule(GameRule.KEEP_INVENTORY,true);
        //NamespacedKey for Deathcount and the time until when the player should be dead
        NamespacedKey key_DeatCount = new NamespacedKey(this,"DeathCount");
        NamespacedKey key_DeathTradeTime = new NamespacedKey(this,"DeathTime");
        this.deathCountKey = key_DeatCount;
        this.deathTimeKey = key_DeathTradeTime;


        //Register PluginListener
        getServer().getPluginManager().registerEvents(new DeathListener(this),this);
        getServer().getPluginManager().registerEvents(new ServerJoinListenerRespawner(this),this);
        //Register new command, only useable while being in spectator mode
        this.getCommand("deathTrade").setExecutor(new DeathTradeCommand(this));

        //Scheduler that checks if a online player should be respawned, checks that every 10seconds to lower the load on the server
        DeathTradeRunnable dtr = new DeathTradeRunnable(this);
        dtr.runTaskTimer(this,0,200L);
    }
    @Override
    public void onDisable(){

    }

    public void main(String arg[]){

    }
    //Getter methods for the namespacedkeys
    public NamespacedKey getDeathCountKey() {
        return deathCountKey;
    }
    public NamespacedKey getDeathTimeKey(){return deathTimeKey;}


}
