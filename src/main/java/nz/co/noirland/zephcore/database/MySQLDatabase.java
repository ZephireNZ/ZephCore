package nz.co.noirland.zephcore.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.queries.GetSchemaQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public abstract class MySQLDatabase {

    private ComboPooledDataSource pool;

    protected SortedMap<Integer, Schema> schemas = new TreeMap<Integer, Schema>();

    public abstract Debug debug();

    protected abstract String getHost();

    protected abstract int getPort();

    protected abstract String getDatabase();

    protected abstract String getUsername();

    protected abstract String getPassword();

    public abstract String getPrefix();

    protected MySQLDatabase() {
        initPool();
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
            List<Map<String, Object>> res = new GetSchemaQuery(this).executeQuery();
            return (Integer) res.get(0).get("version");
        } catch (SQLException e) {
            return 0; // Could not get schema, assume that database is not set up
        }
    }

    private void initPool() {
        pool = new ComboPooledDataSource();
        String url = String.format("jdbc:mysql://%s:%s/%s", getHost(), getPort(), getDatabase());
        pool.setJdbcUrl(url);
        pool.setUser(getUsername());
        pool.setPassword(getPassword());
    }

    public void close() {
        pool.close();
    }

    public static List<Map<String, Object>> toMapList(ResultSet res) throws SQLException {
        ResultSetMetaData md = res.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (res.next()){
            Map<String, Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i){
                row.put(md.getColumnName(i),res.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    public Connection getRawConnection() throws SQLException {
        return pool.getConnection();
    }

    // -- SCHEMA FUNCTIONS -- //

    protected int getLatestSchema() {
        return schemas.lastKey();
    }

    protected Schema getSchemaDef(int schema) {
        return schemas.get(schema);
    }

}
