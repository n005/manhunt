package fr.n005.hunt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	  @Override
	  public void onEnable() 
	  {
		    ItemManager.init();
		    getServer().getPluginManager().registerEvents(new Events(), this);
		    getCommand("hunter").setExecutor(new Commands());
		    this.saveDefaultConfig();
		    Config.initialize(this);
		    System.out.println("Le plugin ManHunt est activé");
	  }
	  
	  @Override
	  public void onDisable() 
	  {
		  this.saveConfig();
	  }
}