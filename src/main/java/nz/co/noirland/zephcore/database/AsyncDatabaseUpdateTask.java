package nz.co.noirland.zephcore.database;

import nz.co.noirland.zephcore.ZephCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncDatabaseUpdateTask extends BukkitRunnable {

    public final static ConcurrentLinkedQueue<PreparedStatement> updates = new ConcurrentLinkedQueue<PreparedStatement>();
    private static AsyncDatabaseUpdateTask inst;

    private AsyncDatabaseUpdateTask() {
        runTaskTimerAsynchronously(ZephCore.inst(), 0, 1);
    }

    public static AsyncDatabaseUpdateTask inst() {
        if(inst == null) {
            inst = new AsyncDatabaseUpdateTask();
        }
        return inst;
    }

    @Override
    public void run() {
        if(!updates.isEmpty()) {
            while(!updates.isEmpty()) {
                PreparedStatement statement = updates.poll();
                try {
                    if(statement.isClosed()) continue;
                    statement.execute();
                    ZephCore.debug().debug("Executed db update statement " + statement.toString());
                } catch (SQLException e) {
                    ZephCore.debug().warning("Failed to execute update statement " + statement.toString(), e);
                } finally {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        ZephCore.debug().warning("Could not close statement " + statement.toString(), e);
                    }
                }
            }
        }
    }

}
