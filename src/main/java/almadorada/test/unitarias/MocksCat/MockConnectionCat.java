package main.java.almadorada.test.unitarias.MocksCat;

import main.java.almadorada.test.unitarias.AdaptersCat.ConnectionAdapterCat;

import java.sql.*;

public class MockConnectionCat extends ConnectionAdapterCat {
    @Override
    public Statement createStatement() {
        return new MockStatementCat();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) {
        return new MockPreparedStatementCat(sql);
    }
}
