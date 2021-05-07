package fr.n005.hunt;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Config {

	private static Main plugin;
    public static void initialize(Main instance) {
        plugin = instance;
    }
	
	public static ItemStack Woolinfo(String detail)
	{
		Boolean c = plugin.getConfig().getBoolean(detail);
		if (c) {
			return createGuiItem(Material.GREEN_WOOL, "ON", detail);
		}
		else {
			return createGuiItem(Material.RED_WOOL, "OFF", detail);
		} 
		//return createGuiItem(Material.RED_WOOL, "OFF");
	}

    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    
    public static void changeconfig(List<String> list)
    {
    	if(list.get(0).equalsIgnoreCase("strengh")) {plugin.getConfig().set("strengh", !plugin.getConfig().getBoolean("strengh"));}
    	if(list.get(0).equalsIgnoreCase("resitance")) {plugin.getConfig().set("resitance", !plugin.getConfig().getBoolean("resitance"));}
    	if(list.get(0).equalsIgnoreCase("fire_resitance")) {plugin.getConfig().set("fire_resitance", !plugin.getConfig().getBoolean("fire_resitance"));}
    	if(list.get(0).equalsIgnoreCase("looting")) {plugin.getConfig().set("looting", !plugin.getConfig().getBoolean("looting"));}
    	if(list.get(0).equalsIgnoreCase("hunted_info")) {plugin.getConfig().set("hunted_info", !plugin.getConfig().getBoolean("hunted_info"));}
    	if(list.get(0).equalsIgnoreCase("hunter_info")) {plugin.getConfig().set("hunter_info", !plugin.getConfig().getBoolean("hunter_info"));}
    	if(list.get(0).equalsIgnoreCase("piglin_drop")) {plugin.getConfig().set("piglin_drop", !plugin.getConfig().getBoolean("piglin_drop"));}
    	if(list.get(0).equalsIgnoreCase("givecompass")) {plugin.getConfig().set("givecompass", !plugin.getConfig().getBoolean("givecompass"));}
    	if(list.get(0).equalsIgnoreCase("customcraft")) {plugin.getConfig().set("customcraft", !plugin.getConfig().getBoolean("customcraft"));}
    	if(list.get(0).equalsIgnoreCase("keepinvhunted")) {plugin.getConfig().set("keepinvhunted", !plugin.getConfig().getBoolean("keepinvhunted"));}
    	if(list.get(0).equalsIgnoreCase("restockbug")) {plugin.getConfig().set("restockbug", !plugin.getConfig().getBoolean("restockbug"));}
    }
}
