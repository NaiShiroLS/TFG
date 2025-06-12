package main.java.almadorada.test.unitarias.MocksPedidos;

import main.java.almadorada.test.unitarias.AdaptersPedidos.ConnectionAdapterPedidos;

import java.sql.*;

public class MockConnectionPedidos extends ConnectionAdapterPedidos {
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new MockPreparedStatementPedidos(sql);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new MockStatementPedidos();
    }
}
