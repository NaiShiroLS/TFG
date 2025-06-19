package main.java.almadorada.test.unitarias.MocksCat;

import main.java.almadorada.test.unitarias.AdaptersCat.StatementAdapterCat;

import java.sql.*;

public class MockStatementCat extends StatementAdapterCat {
    @Override
    public ResultSet executeQuery(String sql) {
        return new MockResultSetCat(); // Se puede mejorar con lógica condicional si lo necesitas
    }
}
