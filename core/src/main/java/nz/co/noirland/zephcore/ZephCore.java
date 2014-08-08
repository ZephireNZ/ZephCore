package nz.co.noirland.zephcore;

import nz.co.noirland.zephcore.database.AsyncDatabaseUpdateTask;
import nz.co.noirland.zephcore.nms.NMSHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ZephCore extends JavaPlugin implements Listener {

    private static ZephCore inst;
    private static Debug debug;
    private static NMSHandler nms;

    public static ZephCore inst() {
        return inst;
    }

    public static Debug debug() { return debug; }

    @Override
    public void onEnable() {
        inst = this;
        debug = new Debug(this);
        nms = findNMS();
        setupUUIDFetcher();
        AsyncDatabaseUpdateTask.inst().start();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        AsyncDatabaseUpdateTask.inst().finish();
    }

    private void setupUUIDFetcher() {
        long start = System.currentTimeMillis();
        OfflinePlayer[] oPlayers = getServer().getOfflinePlayers();
        for(OfflinePlayer oPlayer : oPlayers) {
            UUIDFetcher.updatePlayer(oPlayer.getName(), oPlayer.getUniqueId());
        }
        debug.debug("Fetched " + oPlayers.length + " UUIDs in " + (System.currentTimeMillis() - start));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UUIDFetcher.updatePlayer(player.getName(), player.getUniqueId());
    }

    public static NMSHandler getNMS() {
        return nms;
    }

    private NMSHandler findNMS() {
        String pName = getServer().getClass().getPackage().getName();
        String ver = pName.substring(pName.lastIndexOf('.') + 1);
        try {
            Class<?> clazz = Class.forName("nz.co.noirland.zephcore.nms." + ver + ".NMS");
            if(NMSHandler.class.isAssignableFrom(clazz)) {
                return (NMSHandler) clazz.getConstructor().newInstance();
            }
            throw new Exception();
        } catch (Exception e) {
            debug.disable("Minecraft version " + ver + " unsupported!", e);
            return null;
        }
    }
}
