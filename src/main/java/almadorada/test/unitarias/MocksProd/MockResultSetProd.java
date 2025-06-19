package main.java.almadorada.test.unitarias.MocksProd;

import main.java.almadorada.test.unitarias.AdaptersProd.ResultSetAdapterProd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MockResultSetProd extends ResultSetAdapterProd {

    private int cursor = -1;

    // Cambiado a lista para agregar filas dinámicamente
    private final List<Object[]> dataList = new ArrayList<>();
    private String[] columns;

    public MockResultSetProd() {
        // columnas vacías por defecto
        this.columns = new String[0];
    }

    public MockResultSetProd(Object[][] data, String[] columns) {
        for (Object[] row : data) {
            dataList.add(row);
        }
        this.columns = columns;
    }

    // Nuevo método para agregar fila
    public void addRow(Object[] row) {
        dataList.add(row);
    }

    @Override
    public boolean next() {
        if (cursor + 1 < dataList.size()) {
            cursor++;
            return true;
        }
        return false;
    }

    private int findColumnIndex(String columnLabel) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equalsIgnoreCase(columnLabel)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        int idx = findColumnIndex(columnLabel);
        if (idx == -1) throw new SQLException("Columna no encontrada: " + columnLabel);
        Object val = dataList.get(cursor)[idx];
        if (val == null) return 0;
        if (val instanceof Integer) return (Integer) val;
        throw new SQLException("Valor no es entero para " + columnLabel);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        int idx = findColumnIndex(columnLabel);
        if (idx == -1) throw new SQLException("Columna no encontrada: " + columnLabel);
        Object val = dataList.get(cursor)[idx];
        if (val == null) return null;
        return val.toString();
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        int idx = findColumnIndex(columnLabel);
        if (idx == -1) throw new SQLException("Columna no encontrada: " + columnLabel);
        Object val = dataList.get(cursor)[idx];
        if (val == null) return 0;
        if (val instanceof Double) return (Double) val;
        if (val instanceof Float) return ((Float) val).doubleValue();
        throw new SQLException("Valor no es double para " + columnLabel);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        int idx = findColumnIndex(columnLabel);
        if (idx == -1) throw new SQLException("Columna no encontrada: " + columnLabel);
        Object val = dataList.get(cursor)[idx];
        if (val == null) return null;
        if (val instanceof byte[]) return (byte[]) val;
        throw new SQLException("Valor no es byte[] para " + columnLabel);
    }

    private boolean wasNullFlag = false;

    @Override
    public boolean wasNull() {
        return wasNullFlag;
    }

    // Otros métodos vacíos o no implementados (lanza UnsupportedOperationException)
    @Override public void close() {}
    @Override public boolean isClosed() { return false; }
    @Override public <T> T unwrap(Class<T> iface) { throw new UnsupportedOperationException(); }
    @Override public boolean isWrapperFor(Class<?> iface) { return false; }
}
