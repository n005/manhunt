package fr.n005.hunt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Config {

	private static Main plugin;
	private static String current; 
    public static void initialize(Main instance) {
        plugin = instance;
    }
	
	public static ItemStack Woolinfo(String detail)
	{
		Boolean c = plugin.getConfig().getBoolean(detail);
		if (c) {
			return createGuiItem(Material.LIME_CONCRETE, "ON", detail);
		}
		else {
			return createGuiItem(Material.RED_CONCRETE, "OFF", detail);
		} 
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
    
    public static void changeconfig(List<String> list, Player p)
    {
    	if (getCurrentInv().equals("main"))
    	{
    		
    		for(String key : SubConf(""))
    		{
    			if(list.get(0).equalsIgnoreCase(key)) {
    			Config.setCurrentInv(key);
    			}
    		}
    		
    		for(String key : BooleanList(""))
    		{
    			if(list.get(0).equalsIgnoreCase(key)) {plugin.getConfig().set(key, !plugin.getConfig().getBoolean(key));}
    		}
    	}
    	else
    	{
    		for(String key : BooleanList(getCurrentInv()))
    		{
    			if(list.get(0).equalsIgnoreCase(key)) {plugin.getConfig().set(key, !plugin.getConfig().getBoolean(key));}
    		}
    	}
    }
    
    public static List<String> BooleanList(String sect)
    {
    	List<String> list = new ArrayList<String>();
    	for(String key : plugin.getConfig().getConfigurationSection(sect).getKeys(false))
    	{
    		String value = plugin.getConfig().getString(sect+"."+key);
    		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
    			list.add(key);
    		} else {
    		    // throw some exception
    		}
    	}
		return list;
    }
    
    public static List<String> SubConf(String sect)
    {
    	List<String> list = new ArrayList<String>();
    	for(String key : plugin.getConfig().getConfigurationSection(sect).getKeys(false))
    	{
    		String value = plugin.getConfig().getString(sect+"."+key);
    		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false") || StringUtils.isNumericSpace(value)) {
    		} else {
    			list.add(key);
    		}
    	}
		return list;
    }
    
    public static List<ItemStack> ItemList(String root)
    {
    	List<ItemStack> list = new ArrayList<ItemStack>();
		if(root=="main") {
			for(String key : BooleanList(""))
			{
				list.add(Woolinfo(key));
			}
			for (String key : SubConf(""))
			{
				list.add(createGuiItem(Material.BLUE_CONCRETE, key, key));
			}
		}
		else
			{
				for(String key : BooleanList(getCurrentInv()))
				{
					list.add(Woolinfo(key));
				}
			}
		return list;
    	
    }
    
    
    public static void setCurrentInv(String var1) {
		current = var1;
    }
    
    public static String getCurrentInv() {
		return current;
    }
}
