package me.skijearz.hcDeathTrade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
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
    private final Player p;
    private BukkitRunnable myTimer;

    /**
     * Register the listener here because if it would register onLoad,there would be different instances for the listener and the inventory opening method hence the timer task cannot be stopped from the event handler.
     * also there is a instance of the inventory for each player so each player has a listener on the events in this class.
     * so each event has to check if the playerid is the id from the field in each instance.
     * Otherwise if a player uses a diamond or closes the inventory, this would affect every player not just him.
     * @param pluginInstance instance from the plugin here HardcoreDeathTrade class
     */
    public DeathTradeInventory(HardcoreDeathTrade pluginInstance,Player p){
        this.dt_Inventory = Bukkit.createInventory(this,9,"DeathTrade");
        this.HardcoreDeathTradeInstance = pluginInstance;
        this.HardcoreDeathTradeInstance.getServer().getPluginManager().registerEvents(this,HardcoreDeathTradeInstance);
        this.p = p;
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
        Long currentTime = System.currentTimeMillis()/1000;
        ItemStack timerItem = dt_Inventory.getItem(1);
        this.myTimer = new BukkitRunnable(){

            @Override
            public void run() {
                Long remainingTime = h.getPersistentDataContainer().get(HardcoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG);


                if(remainingTime -currentTime <= 0){
                    this.cancel();
                }
                ItemMeta meta = timerItem.getItemMeta();
                meta.getLore().clear();
                List a = new ArrayList();
                a.add("§aZeit bis zum Respawn:");
                if(remainingTime - currentTime <= 0){
                    a.add("0");
                }else{
                    a.add(LocalTime.ofSecondOfDay(remainingTime - currentTime).toString());
                }
                meta.setLore(a);
                timerItem.setItemMeta(meta);
                h.getPersistentDataContainer().set(HardcoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG,remainingTime - 1);

            }
        };
        myTimer.runTaskTimer(this.HardcoreDeathTradeInstance,0,20L);
    }

    /**
     * As soon as the player closes the inventory the scheduler needs to be canceled also unregister the listener from all events
     * @param e InventoryCloseEvent e
     */
    @EventHandler
    public void closeInventoryEvent(InventoryCloseEvent e) {
        if (e.getPlayer().getUniqueId() == this.p.getUniqueId()) {
            if (e.getView().getTitle().equals("DeathTrade")) {
                HandlerList.unregisterAll(this);
                myTimer.cancel();
            }
        }
    }
    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getUniqueId() == this.p.getUniqueId()){
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) {
                e.setCancelled(true);
                return;
            }
            e.getWhoClicked().setItemOnCursor(null);
            if (e.getView().getTitle().equals("DeathTrade")) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("DeathTrade")) {
                    if (e.getWhoClicked().getInventory().contains(Material.DIAMOND)) {
                        ItemStack[] is = e.getWhoClicked().getInventory().getContents();
                        for (ItemStack item : is) {
                            if (item == null) {
                                continue;
                            }
                            if (item.getType().equals(Material.DIAMOND)) {
                                item.setAmount(item.getAmount() - 1);
                                decreaseRespawnTime(e.getWhoClicked());
                                break;
                            }
                        }
                    } else {
                        e.getWhoClicked().sendMessage(ChatColor.RED + "[HCDeathTrade] " + ChatColor.WHITE + "nicht genügend Diamanten!");
                    }
            }
        }
    }
    }
    private void decreaseRespawnTime(HumanEntity e){
        Long remainingTime = e.getPersistentDataContainer().get(this.HardcoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG);
        if(remainingTime - 3600 <0){
            remainingTime = 0L;
        }else{
            remainingTime -= 3600L;
        }
        e.getPersistentDataContainer().set(this.HardcoreDeathTradeInstance.getDeathTimeKey(),PersistentDataType.LONG,remainingTime);
    }

    @Override
    public Inventory getInventory() {
        return dt_Inventory;
    }

}
