package me.skijearz.hcDeathTrade;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command class, specifies the /deathtrade command and its execution method
 */
public class DeathTradeCommand implements CommandExecutor {
    private final HardcoreDeathTrade HardCoreDeathTradeInstance;

    public DeathTradeCommand(HardcoreDeathTrade pluginInstance){
        this.HardCoreDeathTradeInstance = pluginInstance;
    }

    /**
     * Method which will be executed if a player uses the /deathtrade command.
     * Only usable if the player is in spectator mode
     * @param commandSender commandSender object which called the command
     * @param command command object of the called command
     * @param s command string
     * @param strings arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(p.getGameMode().equals(GameMode.SPECTATOR)){
                DeathTradeInventory dt = new DeathTradeInventory(this.HardCoreDeathTradeInstance,p);
                dt.openInventory(p);
                return true;
            }
           return false;
        }else{
            Bukkit.getLogger().warning("Command can only be used as a player");
            return false;
        }
    }
}
