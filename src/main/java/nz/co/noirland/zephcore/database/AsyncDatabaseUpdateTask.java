package nz.co.noirland.zephcore.database;

import nz.co.noirland.zephcore.ZephCore;
import nz.co.noirland.zephcore.database.queries.Query;

import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncDatabaseUpdateTask implements Runnable {

    private final static LinkedBlockingQueue<Query> queries = new LinkedBlockingQueue<Query>();
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
                Query query = queries.take();
                try {
                    query.execute();
                    ZephCore.debug().debug("Executed db update statement " + query.toString());
                } catch (SQLException e) {
                    ZephCore.debug().warning("Failed to execute update statement " + query.toString(), e);
                }
            } catch (InterruptedException ignored) {}
        }
    }

    public static void addQuery(Query query) {
        queries.add(query);
    }
}
