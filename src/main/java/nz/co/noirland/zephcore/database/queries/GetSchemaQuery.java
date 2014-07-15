package nz.co.noirland.zephcore.database.queries;

public class GetSchemaQuery extends Query {

    private static final String QUERY = "SELECT `version` FROM `{PREFIX}_schema`";

    public GetSchemaQuery() {
        super(QUERY);
    }

}
