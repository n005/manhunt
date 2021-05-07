package fr.n005.hunt;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigGui implements Listener {
    private final Inventory inv;
    public static Main plugin;

    public ConfigGui() {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 9*2, "Config");

        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
    	inv.addItem(Config.Woolinfo("strengh"));
        inv.addItem(Config.Woolinfo("resitance"));
        inv.addItem(Config.Woolinfo("fire_resitance"));
        inv.addItem(Config.Woolinfo("looting"));
        inv.addItem(Config.Woolinfo("hunted_info"));
        inv.addItem(Config.Woolinfo("hunter_info"));
        inv.addItem(Config.Woolinfo("piglin_drop"));
        inv.addItem(Config.Woolinfo("givecompass"));
        inv.addItem(Config.Woolinfo("customcraft"));
        inv.addItem(Config.Woolinfo("keepinvhunted"));
        inv.addItem(Config.Woolinfo("restockbug")); 
    }

    // You can open the inventory with this
    public void openInventory(final Player ent) {
         ent.openInventory(inv);
    }
    public Inventory getInventory() {
        return inv;
    }
    
}