package main.java.almadorada.test.unitarias.MocksPedidos;

import main.java.almadorada.test.unitarias.AdaptersPedidos.ResultSetAdapterPedidos;

import java.sql.*;

public class MockResultSetPedidos extends ResultSetAdapterPedidos {
    private int cursor = -1;
    private final int totalRows = 1;

    @Override
    public boolean next() throws SQLException {
        cursor++;
        return cursor < totalRows;
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        if ("id_pedido".equalsIgnoreCase(columnLabel)) {
            return 1;
        }
        if ("cantidad".equalsIgnoreCase(columnLabel)) {
            return 5;
        }
        return 0;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        if ("nombre_producto".equalsIgnoreCase(columnLabel)) {
            return "Producto Mock";
        }
        if ("estado".equalsIgnoreCase(columnLabel)) {
            return "Pendiente";
        }
        return null;
    }

    @Override
    public void close() {
        // No hace nada
    }
}
