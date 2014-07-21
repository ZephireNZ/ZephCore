package nz.co.noirland.zephcore.database.queries;

import nz.co.noirland.zephcore.database.MySQLDatabase;

public class GetSchemaQuery extends Query {

    private static final String QUERY = "SELECT `version` FROM `{PREFIX}_schema`";

    public GetSchemaQuery(MySQLDatabase db) {
        super(db, QUERY);
    }

}
