package fr.n005.hunt;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	  @Override
	  public void onEnable() 
	  {
		    ItemManager.init();
		    getServer().getPluginManager().registerEvents(new Events(), this);
		    getCommand("givetracker").setExecutor(new Commands());
		    getCommand("settarget").setExecutor(new Commands());
	  }
	  
	  @Override
	  public void onDisable() 
	  {

	  }
}