package nu.nerd.sheepnuker;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.plugin.java.JavaPlugin;

public class SheepNuker extends JavaPlugin {
	
    protected static final Logger log = Logger.getLogger("Minecraft");
    private Random rand = new Random();
    
    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.getConfig().addDefault("sheepnuker.maxsheep", 10);
        final int limit = this.getConfig().getInt("sheepnuker.maxsheep");
        saveConfig();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                log.log(Level.INFO, "Starting sheep check");
                for (World world : getServer().getWorlds()) {
                    for (Chunk chunk : world.getLoadedChunks()) {
                        int sheep = 0;
                        ArrayList<Entity> sheeps = new ArrayList<Entity>();
                        for (Entity entity : chunk.getEntities()) {
                            if ((entity instanceof Sheep)) {
                                sheep++;
                                sheeps.add(entity);
                            }
                        }
                        if (sheep > limit) {
                            log.log(Level.INFO, "TOO MANY SHEEP! Spawning a wolf!");
                            int toSwap = rand.nextInt(sheep+1) - 1;
                            world.spawnCreature(sheeps.get(toSwap).getLocation(), EntityType.WOLF);
                            if (!sheeps.get(toSwap).isDead()) {
                                sheeps.get(toSwap).remove();
                            }
                        }
                        sheeps.clear();
                    }
                }
                log.log(Level.INFO, "Finishing sheep check");
            }
        }, 600, 600);
    }
    
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}