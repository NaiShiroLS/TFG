package main.java.almadorada.test.unitarias.MocksPedidos;

import main.java.almadorada.test.unitarias.AdaptersPedidos.PreparedStatementAdapterPedidos;

import java.sql.*;

public class MockPreparedStatementPedidos extends PreparedStatementAdapterPedidos {
    private final String sql;

    public MockPreparedStatementPedidos(String sql) {
        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return new MockResultSetPedidos();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return 1; // Simula éxito
    }

    @Override
    public void close() {
        // No hace nada
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        // Puedes guardar parámetros si quieres
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        // Puedes guardar parámetros si quieres
    }
}
