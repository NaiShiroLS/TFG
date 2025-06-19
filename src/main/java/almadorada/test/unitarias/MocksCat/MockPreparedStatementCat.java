package main.java.almadorada.test.unitarias.MocksCat;

import main.java.almadorada.test.unitarias.AdaptersCat.PreparedStatementAdapterCat;

import java.sql.*;

public class MockPreparedStatementCat extends PreparedStatementAdapterCat {
    private final String sql;
    private String nombre;
    private int id;

    public MockPreparedStatementCat(String sql) {
        this.sql = sql;
    }

    @Override
    public void setString(int parameterIndex, String value) {
        if (parameterIndex == 1) this.nombre = value;
    }

    @Override
    public void setInt(int parameterIndex, int value) {
        if (sql.contains("id_categoria = ?")) {
            this.id = value;
        }
    }

    @Override
    public ResultSet executeQuery() {
        return new MockResultSetCat(sql, nombre, id);
    }

    @Override
    public int executeUpdate() {
        return 1;
    }
}
