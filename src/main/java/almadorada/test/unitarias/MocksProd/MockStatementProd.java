package main.java.almadorada.test.unitarias.MocksProd;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.almadorada.test.unitarias.AdaptersProd.StatementAdapterProd;

public class MockStatementProd extends StatementAdapterProd {

    private ResultSet resultSet;

    /**
     * Permite establecer el ResultSet que devolverá executeQuery y getResultSet.
     */
    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return resultSet;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        // Simula que siempre afecta 1 fila
        return 1;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return resultSet;
    }

    @Override
    public void close() {
        // Se puede dejar vacío o poner lógica de cierre si quieres
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
