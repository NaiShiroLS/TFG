package main.java.almadorada.test.unitarias.MocksStock;

import main.java.almadorada.test.unitarias.AdaptersStock.PreparedStatementAdapterStock;

import java.sql.ResultSet;

public class MockPreparedStatementStock extends PreparedStatementAdapterStock {
    private final String sql;
    private int stock = -1;

    public MockPreparedStatementStock(String sql) {
        this.sql = sql;
    }

    @Override
    public void setInt(int parameterIndex, int value) {
        if (parameterIndex == 1) {
            this.stock = value;
        }
    }

    @Override
    public ResultSet executeQuery() {
        return new MockResultSetStock(sql, stock);
    }

    @Override
    public int executeUpdate() {
        return 1;
    }
}
