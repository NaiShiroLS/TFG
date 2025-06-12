package main.java.almadorada.test.unitarias.MocksStock;

import main.java.almadorada.test.unitarias.AdaptersStock.ConnectionAdapterStock;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class MockConnectionStock extends ConnectionAdapterStock {
    @Override
    public Statement createStatement() {
        return new MockStatementStock();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) {
        return new MockPreparedStatementStock(sql);
    }
}
