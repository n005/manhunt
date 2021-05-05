package fr.n005.hunt;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager 
{
	  public static ItemStack compass;
	  
	  public static void init()
	  {
		    CreateCompass();
	  }
	  
	  private static void CreateCompass()
	  {
		    ItemStack item = new ItemStack(Material.COMPASS);
		    ItemMeta meta = item.getItemMeta();
		    
		    meta.setDisplayName("Tracker");
		    meta.addEnchant(Enchantment.LUCK, 1, false);
		    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);		    
		    
		    item.setItemMeta(meta);
		    compass = item;
	  }
	  
	  public static void ResetCompass(Player player)
	  {
			  player.getInventory().setItemInMainHand(null);
			  compass = null;
			  CreateCompass();
			  player.getInventory().setItemInMainHand(ItemManager.compass);
	  }
}