package main.java.almadorada.test.unitarias.MocksProd;

import main.java.almadorada.model.Producto;
import main.java.almadorada.test.unitarias.AdaptersProd.PreparedStatementAdapterProd;

import java.sql.*;
import java.util.*;

public class MockPreparedStatementProd extends PreparedStatementAdapterProd {

    private String sql;
    private boolean returnGeneratedKeys = false;
    private final Map<Integer, Object> parameters = new HashMap<>();
    private MockResultSetProd resultSet = new MockResultSetProd();

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setReturnGeneratedKeys(boolean flag) {
        this.returnGeneratedKeys = flag;
    }

    public void setResultSet(MockResultSetProd rs) {
        this.resultSet = rs;
    }

    @Override
    public ResultSet executeQuery() {
        return resultSet;
    }

    @Override
    public int executeUpdate() {
        return 1; // Simular 1 fila afectada
    }

    @Override
    public ResultSet getGeneratedKeys() {
        if (returnGeneratedKeys) {
            return resultSet;
        }
        return null;
    }

    @Override
    public void setInt(int parameterIndex, int x) {
        parameters.put(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) {
        parameters.put(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) {
        parameters.put(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {
        parameters.put(parameterIndex, x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) {
        parameters.put(parameterIndex, null);
    }

    // Otros métodos vacíos o no usados
    @Override public void close() {}
    @Override public boolean execute() { return false; }
    // Más métodos vacíos o excepciones...
    @Override public ResultSet executeQuery(String sql) { throw new UnsupportedOperationException(); }
    @Override public int executeUpdate(String sql) { throw new UnsupportedOperationException(); }
    @Override public void clearParameters() { parameters.clear(); }
    @Override public void setBoolean(int parameterIndex, boolean x) { parameters.put(parameterIndex, x); }
    // Agrega o ignora según se necesite
    @Override public <T> T unwrap(Class<T> iface) { throw new UnsupportedOperationException(); }
    @Override public boolean isWrapperFor(Class<?> iface) { return false; }

    public void setGeneratedKeys(MockResultSetProd generatedKeys) {
        this.resultSet = generatedKeys;
    }
    public int getParameterInt(int parameterIndex) {
        Object value = parameters.get(parameterIndex);
        if (value == null) {
            return 0; // o lanzar excepción si prefieres
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        throw new IllegalArgumentException("El parámetro en índice " + parameterIndex + " no es un entero");
    }

}
