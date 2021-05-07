package fr.n005.hunt;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConfigGui implements Listener {
    private Inventory inv;
    private List<ItemStack> list;
    public static Main plugin;

    // You can open the inventory with this
    public void openInventory(final Player ent, String root) {
    	Config.setCurrentInv(root);
    	list = Config.ItemList(root);
    	inv = Bukkit.createInventory(null, 9*4, Config.getCurrentInv());
    	initializeItems();
        ent.openInventory(inv);
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
    	for(ItemStack key : list)
    	{
    		inv.addItem(key);
    	}
    	
        inv.setItem(31, Config.createGuiItem(Material.GREEN_WOOL, "RETURN"));
 
    }

    public Inventory getInventory() {
        return inv;
    }
    
}