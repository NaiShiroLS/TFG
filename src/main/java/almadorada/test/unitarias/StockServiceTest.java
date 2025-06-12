package main.java.almadorada.test.unitarias;

import main.java.almadorada.model.Producto;
import main.java.almadorada.service.StockService;
import main.java.almadorada.test.unitarias.MocksStock.MockConnectionStock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StockServiceTest {

    private StockService stockService;

    @BeforeEach
    public void setUp() throws Exception {
        stockService = new StockService();

        // Inyectar el MockConnectionStock usando reflexión
        Field field = StockService.class.getDeclaredField("url");
        field.setAccessible(true);
        field.set(stockService, "mock");

        // Redefinir DriverManager para que devuelva nuestro MockConnectionStock
        DriverManager.registerDriver(new java.sql.Driver() {
            @Override
            public Connection connect(String url, java.util.Properties info) {
                return new MockConnectionStock();
            }

            @Override public boolean acceptsURL(String url) { return true; }
            @Override public java.sql.DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) { return new java.sql.DriverPropertyInfo[0]; }
            @Override public int getMajorVersion() { return 1; }
            @Override public int getMinorVersion() { return 0; }
            @Override public boolean jdbcCompliant() { return false; }
            @Override public java.util.logging.Logger getParentLogger() { return null; }
        });
    }

    @Test
    public void testObtenerPorStock_existente() {
        Optional<Producto> productoOpt = stockService.obtenerPorStock(5);

        assertTrue(productoOpt.isPresent(), "El producto debería existir con stock 5");

        Producto producto = productoOpt.get();
        assertEquals(1, producto.getId());
        assertEquals("Manzana", producto.getNombre());
        assertEquals("Roja y jugosa", producto.getDescripcion());
        assertEquals(1.5, producto.getPrecio());
        assertEquals(5, producto.getStock());
        assertEquals(1, producto.getIdCategoria());
        assertEquals("Frutas", producto.getNombreCategoria());
        assertNotNull(producto.getImagen());
        assertEquals(3, producto.getImagen().length); // Verifica que los bytes están cargados
    }

    @Test
    public void testObtenerPorStock_inexistente() {
        Optional<Producto> productoOpt = stockService.obtenerPorStock(99);

        assertFalse(productoOpt.isPresent(), "No debería existir producto con stock 99");
    }
}
