package nz.co.noirland.zephcore.database.queries;

public class Query {

    private Object[] values;
    private String query;

    /**
     * An abstract query representation.
     * @param nargs Number of args in query
     * @param query SQL PreparedStatement query to be executed.
     */
    public Query(int nargs, String query) {
        this(new Object[nargs-1], query);
    }

    public Query(String query) {
        this(0, query);
    }

    public Query(Object[] values, String query) {
        this.values = values;
        this.query = query;
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
}
