package nz.co.noirland.zephcore.database;

import nz.co.noirland.zephcore.ZephCore;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncDatabaseUpdateTask implements Runnable {

    public final static LinkedBlockingQueue<PreparedStatement> updates = new LinkedBlockingQueue<PreparedStatement>();
    private static AsyncDatabaseUpdateTask inst;

    private static volatile boolean stop = false;

    private AsyncDatabaseUpdateTask() {
        new Thread(this).start();
    }

    public static AsyncDatabaseUpdateTask inst() {
        if(inst == null) {
            inst = new AsyncDatabaseUpdateTask();
        }
        return inst;
    }

    public static void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while(!stop) {
            try {
                PreparedStatement statement = updates.take();
                try {
                    if(statement.isClosed()) continue;
                    statement.execute();
                    ZephCore.debug().debug("Executed db update statement " + statement.toString());
                } catch (SQLException e) {
                    ZephCore.debug().warning("Failed to execute update statement " + statement.toString(), e);
                } finally {
                    try {
                        statement.getConnection().close();
                    } catch (SQLException e) {
                        ZephCore.debug().warning("Could not close connection " + statement.toString(), e);
                    }
                }
            } catch (InterruptedException ignored) {}
        }
    }

}
