package fr.n005.hunt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.bukkit.event.block.Action;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin implements Listener, CommandExecutor {
	private Set<UUID> hunters;
	static List<Player> liste =  new ArrayList<Player>() ;
	public static Entity runner;
	int hunter = 0;
	int count = 0;
	@Override
    public void onEnable() {
		ItemManager.init();
		this.saveDefaultConfig();
        System.out.println("Le plugin ManHunt est activé");
        getServer().getPluginManager().registerEvents(this, (Plugin)this);
        for (String command : getDescription().getCommands().keySet())
          getServer().getPluginCommand(command).setExecutor(this); 
        this.hunters = new HashSet<>();
        
        ItemStack compasscraft = new ItemStack(Material.COMPASS, 1);
        NamespacedKey nsKey = new NamespacedKey(this, "01");
        ShapedRecipe craft = new ShapedRecipe(nsKey, compasscraft);
        craft.shape("%*%","*%*","%*%");
        craft.setIngredient('*', Material.IRON_INGOT);
        craft.setIngredient('%', Material.AIR);
        if (this.getConfig().getBoolean("customcraft")) {
        getServer().addRecipe(craft); }
    }

    @Override
    public void onDisable() {

    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { 
        if (command.getName().equalsIgnoreCase("hunter")) { 
        	if (args.length < 1 | args.length > 3) {sendInvalid(sender); return false; }
        	if (args.length == 2) {
          Player player = Bukkit.getPlayer(args[1]);
          if (player == null) {
            sender.sendMessage(ChatColor.RED + "Joueur non trouvé.");
            return false;
          } 
          if (args[0].equalsIgnoreCase("add")) {
            this.hunters.add(player.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + player.getName() + " est maintenant un chasseur.");
            liste.add(player);
            //player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.COMPASS) });
            if (this.getConfig().getBoolean("givecompass")) {
            player.getInventory().addItem(ItemManager.compass); }
          } else if (args[0].equalsIgnoreCase("remove")) {
            this.hunters.remove(player.getUniqueId());
            sender.sendMessage(ChatColor.GREEN + player.getName() + " n'est plus un chasseur.");
            player.getInventory().remove(new ItemStack(Material.COMPASS));
            liste.remove(player);
          }  } else if (args[0].equalsIgnoreCase("start")) {
              Bukkit.broadcastMessage("La game commence !");
              hunter = 1;
              ItemStack sword = new ItemStack(Material.GOLDEN_SWORD, 1);
              sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
              
              ItemStack potion = new ItemStack(Material.POTION, this.getConfig().getInt("nb_potions"));

              PotionMeta potionmeta = (PotionMeta) potion.getItemMeta();
              if (this.getConfig().getBoolean("strengh")) {
            	  PotionEffect strengh = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, this.getConfig().getInt("potionduration")*20*60, 0);
            	  potionmeta.addCustomEffect(strengh, true);
              }
              if (this.getConfig().getBoolean("resitance")) {
              PotionEffect resitance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, this.getConfig().getInt("potionduration")*20*60, 0);
              potionmeta.addCustomEffect(resitance, true);
              }
              if (this.getConfig().getBoolean("fire_resitance")) {
              PotionEffect fire_resitance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, this.getConfig().getInt("potionduration")*20*60, 0);
              potionmeta.addCustomEffect(fire_resitance, true);
              }
              potionmeta.setDisplayName("§9POTION");
              potion.setItemMeta(potionmeta);
              for (Player ponline : Bukkit.getOnlinePlayers()) {
            	  if (liste.contains(ponline)) continue; 
            	  if (this.getConfig().getBoolean("looting")) {
            	  ponline.getInventory().addItem(sword); }
            	  ponline.getInventory().addItem(potion);
            	}
              BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
              scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                  @Override
                  public void run() {
                      Bukkit.broadcastMessage("Les chasseurs partent à la chasse.");
                      hunter = 0;
                  }
              }, (this.getConfig().getInt("avance")*20*60));
            } else if (args[0].equalsIgnoreCase("stop")) {
            	count = 0;
            	Bukkit.broadcastMessage("La game se termine.");
            	hunter = 0;
            	for (Player ponline : Bukkit.getOnlinePlayers()) {
            		ponline.getInventory().remove(new ItemStack(Material.COMPASS));
            		ponline.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            		ponline.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            		ponline.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            		ponline.getInventory().clear();
            	}
            }
            else { 
            sendInvalid(sender);
          } 
        } 
        return false;
      }
    
    private void sendInvalid(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Utilisation non valide. Veuillez utiliser:");
        sender.sendMessage(ChatColor.RED + "/hunter add <name>");
        sender.sendMessage(ChatColor.RED + "/hunter remove <name>");
        sender.sendMessage(ChatColor.RED + "/hunter start");
        sender.sendMessage(ChatColor.RED + "/hunter stop");
      }
    
  /*  @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
      Player player = event.getPlayer();
      if (this.hunters.contains(player.getUniqueId()) && event.hasItem() && event.getItem().getType() == Material.COMPASS && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
        Player nearest = null;
        double distance = Double.MAX_VALUE;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          if (onlinePlayer.equals(player) || !onlinePlayer.getWorld().equals(player.getWorld()) || this.hunters.contains(onlinePlayer.getUniqueId()))
            continue; 
          double distanceSquared = onlinePlayer.getLocation().distanceSquared(player.getLocation());
          if (distanceSquared < distance) {
            distance = distanceSquared;
            nearest = onlinePlayer;
          } 
        } 
        if (nearest == null) {
          player.sendMessage(ChatColor.RED + "Aucun joueur à suivre!");
          return;
        } 
        player.setCompassTarget(nearest.getLocation());
        player.sendMessage(ChatColor.GREEN + "La boussole pointe maintenant vers " + nearest.getName() + ".");
      } 
    } */
    
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
      if (this.hunters.contains(event.getEntity().getUniqueId()))
        event.getDrops().removeIf(next -> (next.getType() == Material.COMPASS)); 
      else {
    	  count = count + 1;
    	  if (count>(this.getConfig().getInt("nb_vie")-1)) {Bukkit.broadcastMessage("Les chasseurs ont gagnés");}
      }
    }
    
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for (Player s1 : liste) {
       	 if (e.getEntity() != s1) {
                for (Player ponline : Bukkit.getOnlinePlayers()) {
              	  if (liste.contains(ponline)) continue; 
              	  if (this.getConfig().getBoolean("keepinvhunted")) {
              		e.setKeepInventory(true);
              		e.getDrops().clear(); }
              	  } } }
    }
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
      if (this.hunters.contains(event.getPlayer().getUniqueId()) && event.getItemDrop().getItemStack().getType() == Material.COMPASS)
        event.setCancelled(true); 
    }
    
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
      Player player = event.getPlayer();
      if (this.hunters.contains(player.getUniqueId()))
    	  player.getInventory().addItem(ItemManager.compass);
    }
 
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
    	List<Player> liste54 =  new ArrayList<Player>() ;
    	Player runner54 = null;
    	Player player = e.getPlayer();
    	 for (Player s : liste) {
    		 if (player == s && hunter==1) {
    		 player.setVelocity(new Vector().zero());
    		} }
         //for (Player s1 : liste) {
        	// if (player != s1) {
                 for (Player ponline : Bukkit.getOnlinePlayers()) {
               	  if (liste.contains(ponline)) continue; 
               	  //player.setCompassTarget(ponline.getLocation());
               	  runner54 = ponline;
               	  liste54.add(ponline);
                } //} }
         
         //} 
         Player nearest = null;
         double distance = Double.MAX_VALUE;
         for (Player liste542 : liste54) {
         for (Player onlinePlayer : liste) {
           if (liste542 == null || onlinePlayer.equals(player) || !liste542.getWorld().equals(player.getWorld()) || onlinePlayer == player || liste54.contains(onlinePlayer))
             continue; 
           if (liste542 == player) {
           double distanceSquared = liste542.getLocation().distanceSquared(onlinePlayer.getLocation());
           if (distanceSquared < distance) {
             distance = distanceSquared;
             nearest = onlinePlayer;
           } } }
         if (distance > Math.pow(this.getConfig().getInt("hunted_distance"), 2)) {
             /*for (Player s1 : liste) {
            	 if (player == s1) {
                     for (Player ponline : Bukkit.getOnlinePlayers()) {
                   	  if (liste.contains(ponline)) continue; 
                    } } }*/
           } else {
         //for (Player s1 : liste) {
        	 //if (player == s1) {
                 //for (Player ponline : Bukkit.getOnlinePlayers()) {
               	  //if (liste.contains(ponline)) continue; 
               	if (this.getConfig().getBoolean("hunted_info") && liste542 == player) {
               		liste542.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("Le joueur le plus proche se situe à " + (int)Math.round(Math.sqrt(distance)) + " blocks."));
                } } } //} } }
         double distanceSquared = 0;
         for (Player s1 : liste) {
        	 if (player == s1) {
                 for (Player ponline : Bukkit.getOnlinePlayers()) {
               	  if (liste.contains(ponline)) continue; 
                  for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                      if (onlinePlayer.equals(player) || !onlinePlayer.getWorld().equals(player.getWorld()))
                        continue; 
                       distanceSquared = ponline.getLocation().distanceSquared(player.getLocation());}
                  if (distanceSquared < Math.pow(this.getConfig().getInt("hunter_distance"), 2)) {
                	  if (this.getConfig().getBoolean("hunter_info")) {
                	  player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("La chassé est à moins de 500 blocks"));}
                	  }
                } } }
         } 
    	 //Double test = runner.getLocation().distanceSquared(nearplayer)
    	// runner.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("Test"));
    //}
    
    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        double distance = Double.MAX_VALUE;
    	Player player = event.getPlayer();
        for (Player s1 : liste) {
       	 if (player == s1) {
                for (Player ponline : Bukkit.getOnlinePlayers()) {
              	  if (liste.contains(ponline)) continue; 
              	  //player.setCompassTarget(ponline.getLocation());
              	  //runner = ponline;
              	 double distanceSquared = ponline.getLocation().distanceSquared(player.getLocation());
              	if (distanceSquared < distance) {
                    distance = distanceSquared;
                    runner = ponline;
                  }
              	  } } }
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        	if(event.getItem() != null) {
        	if(event.getItem().getType() == Material.COMPASS) {
        		if(player.getWorld().getEnvironment() == runner.getWorld().getEnvironment()) {
        			CompassMeta compass = (CompassMeta)player.getInventory().getItemInMainHand().getItemMeta();
        			compass.setLodestone(runner.getLocation());
        			compass.setLodestoneTracked(false);
        			player.getInventory().getItemInMainHand().setItemMeta(compass);
        			ItemManager.compass.setItemMeta(compass); 
        		}
        		else { player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "La cible est dans une autre dimension!"); }
        } } }
    }
    @EventHandler
    public void onPiglinDrop(EntityDropItemEvent e) {
    	if (this.getConfig().getBoolean("piglin_drop")) {
      if (!(e.getEntity() instanceof org.bukkit.entity.Piglin))
        return; 
      double r = Math.random() * 100.0D;
      double rc = this.getConfig().getDouble("pearldropchance");
      if (r > rc)
        return; 
      e.setCancelled(true);
      ItemStack ep = new ItemStack(Material.ENDER_PEARL, 2);
      e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), ep);
    	}
    }
    @EventHandler
    public void onBed(EntityDamageEvent e) {

        if(e.getEntity() instanceof EnderDragon) {

            if(e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {

                EnderDragon ed = (EnderDragon) e.getEntity();
                Location l = ed.getLocation();
                ed.setVelocity(ed.getVelocity().add(new Vector(0, 0.25, 0)));
             
                ed.damage(20);
             
                new BukkitRunnable() {
                    int time;
                    public void run() {
                        ed.teleport(l);
                        time++;
                        if(time > 2) {
                       
                            ed.setVelocity(ed.getVelocity().add(new Vector(0, 0.23, 0)));
                            this.cancel();
                        }
                    }
                }.runTaskTimer((this) , 0, 1);
            }
        }

    }
    

    @EventHandler
    public void interact(PlayerInteractEntityEvent event) {
    	if (this.getConfig().getBoolean("restockbug")) {
            Player p = event.getPlayer();
            if (!(event.getRightClicked() instanceof Villager)) {
                return;
            }
            Villager villager = (Villager) event.getRightClicked();
            
            List<MerchantRecipe> recipes = villager.getRecipes();
            for (MerchantRecipe recipe: recipes) {
                recipe.setUses(0);
                recipe.setMaxUses(1_000_000);
                
                try {
                    Field handle = findField(recipe, "handle");
                    handle.setAccessible(true);
                    Object NMSRecipe = handle.get(recipe);
                    Field demand = findField(NMSRecipe, "demand");
                    demand.setAccessible(true);
                    demand.setInt(NMSRecipe, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } }
    }
    
    Field findField(Object obj, String name) {
        Class cls = obj.getClass();
        
        while (cls != null) {
            for (Field f: cls.getDeclaredFields()) {
                if (f.getName().equals(name)) {
                    return f;
                }
            }
            cls = cls.getSuperclass();
        }
        
        System.err.println("Field "+name+" was not found. Here's what I have:\n");
        cls = obj.getClass();
        while (cls != null) {
            for (Field f: cls.getDeclaredFields()) {
                System.err.println("   - "+f.getName());
            }
            cls = cls.getSuperclass();
        }
        
        return null;
    }
}