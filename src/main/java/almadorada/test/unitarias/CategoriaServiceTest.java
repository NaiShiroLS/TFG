package main.java.almadorada.test.unitarias;

import main.java.almadorada.model.Categoria;
import main.java.almadorada.service.CategoriaService;
import main.java.almadorada.test.unitarias.MocksCat.MockConnectionCat;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaServiceTest {

    CategoriaService service;

    @BeforeEach
    void setUp() {
        service = new CategoriaService() {
            @Override
            public Connection getConnection() throws SQLException {
                return new MockConnectionCat();
            }
        };
    }

    @Test
    void testObtenerTodos_devuelveListaCategorias() {
        List<Categoria> categorias = service.obtenerTodos();
        assertNotNull(categorias);
        assertEquals(2, categorias.size());
        assertEquals("Frutas", categorias.get(0).getNombre());
    }

    @Test
    void testExisteCategoriaPorNombre_existenteDevuelveTrue() {
        boolean existe = service.existeCategoriaPorNombre("Frutas", 0);
        assertTrue(existe);
    }

    @Test
    void testExisteCategoriaPorNombre_noExistenteDevuelveFalse() {
        boolean existe = service.existeCategoriaPorNombre("NoExiste", 0);
        assertFalse(existe);
    }

    @Test
    void testTieneProductosAsociados_conProductosDevuelveTrue() {
        boolean tiene = service.tieneProductosAsociados(1);
        assertTrue(tiene);
    }

    @Test
    void testTieneProductosAsociados_sinProductosDevuelveFalse() {
        boolean tiene = service.tieneProductosAsociados(999);
        assertFalse(tiene);
    }

    @Test
    void testBuscarPorId_existenteDevuelveCategoria() {
        Categoria cat = service.buscarPorId(1);
        assertNotNull(cat);
        assertEquals("Frutas", cat.getNombre());
    }

    @Test
    void testBuscarPorId_noExistenteDevuelveNull() {
        Categoria cat = service.buscarPorId(999);
        assertNull(cat);
    }
}
