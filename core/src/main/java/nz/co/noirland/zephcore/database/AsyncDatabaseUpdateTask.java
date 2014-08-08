package nz.co.noirland.zephcore.database;

import nz.co.noirland.zephcore.ZephCore;
import nz.co.noirland.zephcore.database.queries.Query;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncDatabaseUpdateTask extends Thread {

    private final static LinkedBlockingQueue<Query> queries = new LinkedBlockingQueue<Query>();
    private static AsyncDatabaseUpdateTask inst;

    private AsyncDatabaseUpdateTask() {
        super("AsyncDatabaseUpdateTask");
    }

    public static AsyncDatabaseUpdateTask inst() {
        if(inst == null) {
            inst = new AsyncDatabaseUpdateTask();
        }
        return inst;
    }

    public void finish() {
        interrupt();
        LinkedList<Query> drain = new LinkedList<Query>();
        queries.drainTo(drain);
        for(Query query : drain) {
            execute(query);
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                execute(queries.take());
            }
        } catch (InterruptedException ignored) {}

    }

    private void execute(Query query) {
        try {
            query.execute();
            ZephCore.debug().debug("Executed db update statement " + query.toString());
        } catch (SQLException e) {
            ZephCore.debug().warning("Failed to execute update statement " + query.toString(), e);
        }
    }

    public static void addQuery(Query query) {
        queries.add(query);
    }
}
