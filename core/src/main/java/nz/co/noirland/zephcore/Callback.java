package nz.co.noirland.zephcore;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * An abstract class designed to make callbacks easier.
 * Most useful when wanting to do something Async, and then continue back in sync.
 */
public abstract class Callback extends BukkitRunnable {

    private Plugin plugin;

    public Callback(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Schedule this Callback for running on the next tick.
     */
    public void schedule() {
        this.runTask(plugin);
    }
}
