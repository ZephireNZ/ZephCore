package nz.co.noirland.zephcore;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * @deprecated Please use {@link nz.co.noirland.zephcore.Util#cancelFall}
 */
public class CheckFallingTask extends BukkitRunnable {

    private UUID player;

    public CheckFallingTask(UUID player) {
        this.player = player;
    }

    public void run() {
        Player p = Util.player(player).getPlayer();
        if(p == null) {
            cancel();
            return;
        }

        if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) { // Check if player has stopped falling
            p.setFallDistance(0f);
            this.cancel();
        }
    }

    public void start() {
        runTaskTimer(ZephCore.inst(), 0L, 1L);
    }

}
