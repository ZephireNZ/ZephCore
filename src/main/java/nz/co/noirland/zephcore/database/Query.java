package nz.co.noirland.zephcore.database;

public class Query {

    private Object[] values;
    private String query;
    private String table;

    /**
     * An abstract query representation.
     * @param length Size of the array to create
     * @param query SQL PreparedStatement query to be executed.
     */
    public Query(int length, String query, String table) {
        this(new Object[length-1], query, table);
    }

    public Query(String query, String table) {
        this(0, query, table);
    }

    public Query(Object[] values, String query, String table) {
        this.values = values;
        this.query = query;
        this.table = table;
    }

    protected void setValue(int key, Object value) {
        if(key < values.length) {
            values[key] = value;
        }
    }

    public Object[] getValues() {
        return values;
    }

    public String getQuery() {
        return query;
    }

    public String getTable() {
        return table;
    }
}
