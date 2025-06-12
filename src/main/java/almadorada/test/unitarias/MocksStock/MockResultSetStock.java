package main.java.almadorada.test.unitarias.MocksStock;

import main.java.almadorada.test.unitarias.AdaptersStock.ResultSetAdapterStock;

public class MockResultSetStock extends ResultSetAdapterStock {
    private int index = -1;
    private final Object[][] data;

    public MockResultSetStock(String sql, int stock) {
        // Condición flexible para detectar búsquedas por stock
        if (sql.toLowerCase().contains("stock") && stock == 5) {
            data = new Object[][] {
                    {1, "Manzana", "Roja y jugosa", 1.5, 5, new byte[]{1, 2, 3}, 1, "Frutas"}
            };
        } else {
            data = new Object[0][0];
        }
    }

    @Override
    public boolean next() {
        index++;
        return index < data.length;
    }

    @Override
    public int getInt(String columnLabel) {
        return switch (columnLabel) {
            case "id_producto" -> (int) data[index][0];
            case "stock" -> (int) data[index][4];
            case "id_categoria" -> (int) data[index][6];
            default -> 0;
        };
    }

    @Override
    public double getDouble(String columnLabel) {
        if ("precio".equals(columnLabel)) {
            return (double) data[index][3];
        }
        return 0;
    }

    @Override
    public String getString(String columnLabel) {
        return switch (columnLabel) {
            case "nombre" -> (String) data[index][1];
            case "descripcion" -> (String) data[index][2];
            case "nombre_categoria" -> (String) data[index][7];
            default -> "";
        };
    }

    @Override
    public byte[] getBytes(String columnLabel) {
        if ("imagen".equals(columnLabel)) {
            return (byte[]) data[index][5];
        }
        return new byte[0];
    }

    @Override
    public boolean wasNull() {
        return data[index][6] == null;
    }
}
