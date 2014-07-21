package nz.co.noirland.zephcore.database.queries;

import nz.co.noirland.zephcore.database.AsyncDatabaseUpdateTask;
import nz.co.noirland.zephcore.database.MySQLDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class Query {

    private Object[] values;
    private String query;
    private MySQLDatabase db;

    /**
     * An abstract query representation.
     * @param nargs Number of args in query
     * @param query SQL PreparedStatement query to be executed.
     */
    public Query(MySQLDatabase db, int nargs, String query) {
        this(db, new Object[nargs-1], query);
    }

    public Query(MySQLDatabase db, String query) {
        this(db, 0, query);
    }

    public Query(MySQLDatabase db, Object[] values, String query) {
        this.values = values;
        this.query = query;
        this.db = db;
    }

    /**
     * Sets the (natural) index in the query to the given value.
     * @param index index of value, starting at 1
     * @param value The value
     * @throws IllegalArgumentException if index is out of range
     */
    protected void setValue(int index, Object value) throws IllegalArgumentException {
        if(index == 0 || index > values.length) {
            throw new IllegalArgumentException();
        }
        values[index - 1] = value;
    }

    public Object[] getValues() {
        return values;
    }

    public String getQuery() {
        return query;
    }

    public void execute() throws SQLException {
        PreparedStatement stmt = getStatement();
        stmt.execute();
        stmt.getConnection().close();
    }

    public void executeAsync() {
        AsyncDatabaseUpdateTask.addQuery(this);
    }

    public List<Map<String, Object>> executeQuery() throws SQLException {
        PreparedStatement stmt = getStatement();
        List<Map<String, Object>> ret = MySQLDatabase.toMapList(stmt.executeQuery());
        stmt.getConnection().close();
        return ret;
    }

    public PreparedStatement getStatement() {
        String q = getQuery().replaceAll("\\{PREFIX\\}", db.getPrefix());

        try {
            PreparedStatement statement;
            //TODO: May not be necessary to change add options
            if(getQuery().startsWith("INSERT")) {
                statement = db.getRawConnection().prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            } else {
                statement = db.getRawConnection().prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }

            for(int i = 0; i < values.length; i++) {
                statement.setObject(i, values[i]);
            }
            return statement;
        } catch (SQLException e) {
            db.debug().disable("Could not create statement for database!", e);
            return null;
        }
    }
}
