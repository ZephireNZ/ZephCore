package nz.co.noirland.zephcore.database.queries;

import nz.co.noirland.zephcore.database.MySQLDatabase;

public class GetSchemaQuery extends Query {

    private static final String QUERY = "SELECT `version` FROM `{PREFIX}_schema`";
    private MySQLDatabase db;

    public GetSchemaQuery(MySQLDatabase db) {
        super(QUERY);
        this.db = db;

    }

    @Override
    protected MySQLDatabase getDB() {
        return db;
    }
}
