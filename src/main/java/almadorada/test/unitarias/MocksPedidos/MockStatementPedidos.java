package main.java.almadorada.test.unitarias.MocksPedidos;

import main.java.almadorada.test.unitarias.AdaptersPedidos.StatementAdapterPedidos;

import java.sql.*;

public class MockStatementPedidos extends StatementAdapterPedidos {
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return new MockResultSetPedidos();
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return 1; // Simula éxito
    }

    @Override
    public void close() {
        // No hace nada
    }
}
