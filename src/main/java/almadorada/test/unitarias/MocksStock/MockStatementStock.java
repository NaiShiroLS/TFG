package main.java.almadorada.test.unitarias.MocksStock;

import main.java.almadorada.test.unitarias.AdaptersStock.StatementAdapterStock;
import java.sql.*;

public class MockStatementStock extends StatementAdapterStock {
    @Override
    public ResultSet executeQuery(String sql) {
        return new MockResultSetStock(sql, 5);
    }
}
