package main.java.almadorada.test.unitarias.MocksCat;

import main.java.almadorada.test.unitarias.AdaptersCat.ResultSetAdapterCat;

public class MockResultSetCat extends ResultSetAdapterCat {
    private int index = -1;
    private final String[][] data;

    public MockResultSetCat() {
        data = new String[][] {
            {"1", "Frutas", "Categoría de frutas"},
            {"2", "Verduras", "Categoría de verduras"}
        };
    }

    public MockResultSetCat(String sql, String nombre, int id) {
        if (sql.contains("productos")) {
            data = (id == 1) ? new String[][]{{"1"}} : new String[][]{{"0"}};
        } else if (sql.contains("COUNT(*)") && nombre != null) {
            data = nombre.equals("Frutas") ? new String[][]{{"1"}} : new String[][]{{"0"}};
        } else if (sql.contains("id_categoria = ?")) {
            data = (id == 1) ? new String[][]{{"1", "Frutas", "Categoría de frutas"}} : new String[0][0];
        } else {
            data = new String[0][0];
        }
    }

    @Override
    public boolean next() {
        index++;
        return index < data.length;
    }

    @Override
    public int getInt(int columnIndex) {
        return Integer.parseInt(data[index][columnIndex - 1]);
    }

    @Override
    public int getInt(String columnLabel) {
        switch (columnLabel) {
            case "id_categoria": return Integer.parseInt(data[index][0]);
            default: return Integer.parseInt(data[index][0]);
        }
    }

    @Override
    public String getString(String columnLabel) {
        switch (columnLabel) {
            case "nombre": return data[index][1];
            case "descripcion": return data[index][2];
            default: return "";
        }
    }
}
