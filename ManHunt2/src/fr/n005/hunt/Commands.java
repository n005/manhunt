package fr.n005.hunt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
		
		if(!(sender instanceof Player)) return false;
		
		Player player = (Player)sender;
		
		  if(cmd.getName().equalsIgnoreCase("hunter"))
		    {
			  if (args.length < 1 | args.length > 3) {sendInvalid(sender); return false; }
			  
			  if (args[0].equalsIgnoreCase("config")) {
				  
				  ConfigGui gui = new ConfigGui();
				  gui.openInventory(player,"main");
				  
			  }
			  else {
					sendInvalid(sender); }
		    }
		return false;
	}
	
	
    private void sendInvalid(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Utilisation non valide. Veuillez utiliser:");
        sender.sendMessage(ChatColor.RED + "/hunter config");
      }
    

}
