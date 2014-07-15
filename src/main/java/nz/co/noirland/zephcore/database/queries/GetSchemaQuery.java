package nz.co.noirland.zephcore.database.queries;

import nz.co.noirland.zephcore.database.DatabaseTables;
import nz.co.noirland.zephcore.database.Query;

public class GetSchemaQuery extends Query {

    private static final String QUERY = "SELECT `version` FROM `{TABLE}`";

    public GetSchemaQuery() {
        super(QUERY, DatabaseTables.SCHEMA);
    }

}
