package nz.co.noirland.zephcore.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.queries.GetSchemaQuery;
import nz.co.noirland.zephcore.database.queries.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class MySQLDatabase {

    private ComboPooledDataSource pool;

    protected SortedMap<Integer, Schema> schemas = new TreeMap<Integer, Schema>();

    protected abstract Debug debug();

    protected abstract String getHost();

    protected abstract int getPort();

    protected abstract String getDatabase();

    protected abstract String getUsername();

    protected abstract String getPassword();

    protected abstract String getPrefix();

    protected MySQLDatabase() {
        initPool();
    }

    public PreparedStatement getStatement(Query query) {
        String q = query.getQuery().replaceAll("\\{PREFIX\\}", getPrefix());

        try {
            PreparedStatement statement;
            if(query.getQuery().startsWith("INSERT")) {
                statement = pool.getConnection().prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            } else {
                statement = pool.getConnection().prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }

            Object[] values = query.getValues();
            for(int i = 0; i < values.length; i++) {
                statement.setObject(i, values[i]);
            }
            return statement;
        } catch (SQLException e) {
            debug().disable("Could not create statement for database!", e);
            return null;
        }

    }

    public void checkSchema() {
        int version = getCurrentSchema();
        int latest = getLatestSchema();
        if(version == latest) {
            return;
        }
        if(version > latest) {
            debug().disable("Database schema is newer than this plugin version!");
            return;
        }

        for(Schema schema : schemas.tailMap(version + 1).values()) {
            schema.run();
        }

    }

    private int getCurrentSchema() {
        try {
            ResultSet res = getStatement(new GetSchemaQuery()).executeQuery();
            res.first();
            return res.getInt("version");
        } catch (SQLException e) {
            return 0; // Could not get schema, assume that database is not set up
        }
    }

    public static void runStatementAsync(PreparedStatement statement) {
        AsyncDatabaseUpdateTask.updates.add(statement);
    }

    public void initPool() {
        pool = new ComboPooledDataSource();
        String url = String.format("jdbc:mysql://%s:%s/%s", getHost(), getPort(), getDatabase());
        pool.setJdbcUrl(url);
        pool.setUser(getUsername());
        pool.setPassword(getPassword());
    }

    // -- SCHEMA FUNCTIONS -- //

    protected int getLatestSchema() {
        return schemas.lastKey();
    }

    protected Schema getSchemaDef(int schema) {
        return schemas.get(schema);
    }

}
