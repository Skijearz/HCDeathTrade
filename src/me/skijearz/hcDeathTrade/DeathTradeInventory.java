package me.skijearz.hcDeathTrade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DeathTradeInventory implements InventoryHolder, Listener {
    private final Inventory dt_Inventory;
    private final HardcoreDeathTrade HardcoreDeathTradeInstance;
    private BukkitRunnable myTimer;

    /**
     * Register the listener here because if it would register onLoad,there would be different instances for the listener and the inventory opening method hence the timer task cannot be stopped from the event handler.
     * @param pluginInstance instance from the plugin here HardcoreDeathTrade class
     */
    public DeathTradeInventory(HardcoreDeathTrade pluginInstance){
        this.dt_Inventory = Bukkit.createInventory(this,9,"DeathTrade");
        this.HardcoreDeathTradeInstance = pluginInstance;
        this.HardcoreDeathTradeInstance.getServer().getPluginManager().registerEvents(this,HardcoreDeathTradeInstance);
    }

    /**
     * Inits the items a player sees when they open the inv
     * @param p HumanEntity
     */
    private void initItems(HumanEntity p){
        this.dt_Inventory.addItem(createGuiItem(Material.ZOMBIE_HEAD,"DeathCounter","§aAnzahl Tode:","§a" + p.getPersistentDataContainer().get(this.HardcoreDeathTradeInstance.getDeathCountKey(), PersistentDataType.INTEGER)));
        this.dt_Inventory.addItem(createGuiItem(Material.CLOCK,"RespawnTimer","§aZeit bis zum Respawn:"));
        this.dt_Inventory.addItem(createGuiItem(Material.DIAMOND,"DeathTrade","§aKlick um die Zeit bis zum Respawn zu verkürzen","§a1x Diamond 1h"));
    }
    protected ItemStack createGuiItem(final Material mat,final String name, final String... lore ){
        final ItemStack item = new ItemStack(mat,1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Method which opens the Inventory and starts a timer on the item lore to see how long the remaining deathTimer is
     * @param h
     */
    public void openInventory(final HumanEntity h){
        initItems(h);
        h.openInventory(dt_Inventory);
        Long timeToRespawn = h.getPersistentDataContainer().get(HardcoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG);
        Long timeUntilRespawn = timeToRespawn - System.currentTimeMillis()/1000;
        ItemStack timerItem = dt_Inventory.getItem(1);
        this.myTimer = new BukkitRunnable(){
            Long innerTimer = timeUntilRespawn;

            @Override
            public void run() {
                if(innerTimer == 0){
                    this.cancel();
                }
                ItemMeta meta = timerItem.getItemMeta();
                meta.getLore().clear();
                List a = new ArrayList();
                a.add("§aZeit bis zum Respawn:");
                a.add(LocalTime.ofSecondOfDay(innerTimer).toString());
                meta.setLore(a);
                timerItem.setItemMeta(meta);
                innerTimer--;
            }
        };
        myTimer.runTaskTimer(this.HardcoreDeathTradeInstance,0,20L);
    }

    /**
     * As soon as the player closes the inventory the scheduler needs to be canceled
     * @param e InventoryCloseEvent e
     */
    @EventHandler
    public void closeInventoryEvent(InventoryCloseEvent e){
        if(e.getView().getTitle().equals("DeathTrade")){
            myTimer.cancel();
        }
    }
    @EventHandler
    public void onItemClick(InventoryClickEvent e){

    }


    @Override
    public Inventory getInventory() {
        return dt_Inventory;
    }

}
