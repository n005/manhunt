package fr.n005.hunt;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {
	
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
    	if(e.getView().getTitle().equalsIgnoreCase("Config")) {
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
        Config.changeconfig(e.getCurrentItem().getItemMeta().getLore());
        e.getWhoClicked().closeInventory();
        ConfigGui gui = new ConfigGui();
		gui.openInventory(Bukkit.getPlayer(e.getWhoClicked().getName()));
    	}
        
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) {
    	if(e.getView().getTitle().equalsIgnoreCase("Config")) {
          e.setCancelled(true);
        }
    }
    
}
