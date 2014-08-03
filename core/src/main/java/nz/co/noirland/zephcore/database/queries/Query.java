package nz.co.noirland.zephcore.database.queries;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Query {

    public void execute() throws SQLException;

    public void executeAsync();

    public List<Map<String, Object>> executeQuery() throws SQLException;
}
