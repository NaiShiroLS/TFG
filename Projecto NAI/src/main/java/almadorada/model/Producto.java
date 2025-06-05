package main.java.almadorada.model;

import java.util.Objects;
import java.util.Arrays; // Importar para Arrays.copyOf

/**
 * Modelo de datos para representar un producto en el sistema
 */
public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private byte[] imagen;
    private int idCategoria;
    private String nombreCategoria; // Campo extra para mostrar en la UI

    // ============ CONSTRUCTORES ============

    /**
     * Constructor vacío
     */
    public Producto() {
        this.precio = 0.0;
        this.stock = 0;
        this.idCategoria = 0;
    }

    /**
     * Constructor con parámetros básicos (sin imagen)
     */
    public Producto(String nombre, String descripcion, double precio, int stock, int idCategoria) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    /**
     * Constructor completo
     */
    public Producto(int id, String nombre, String descripcion, double precio, int stock,
                    byte[] imagen, int idCategoria, String nombreCategoria) {
        this(nombre, descripcion, precio, stock, idCategoria);
        this.id = id;
        this.imagen = imagen;
        this.nombreCategoria = nombreCategoria;
    }

    // ============ GETTERS Y SETTERS ============

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public boolean tieneImagen() {
        return imagen != null && imagen.length > 0;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    // ============ MÉTODOS OVERRIDE ============

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;

        // Si ambos tienen ID válido, comparar por ID
        if (id > 0 && producto.id > 0) {
            return id == producto.id;
        }

        // Si no tienen ID válido, comparar por nombre y precio
        return Objects.equals(nombre, producto.nombre) &&
                Double.compare(precio, producto.precio) == 0;
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hash(id);
        }
        return Objects.hash(nombre, precio);
    }

    @Override
    public String toString() {
        return String.format("Producto{id=%d, nombre='%s', descripcion='%s', precio=%.2f, " +
                        "stock=%d, idCategoria=%d, nombreCategoria='%s', tieneImagen=%b}",
                id, nombre, descripcion, precio, stock, idCategoria,
                nombreCategoria, tieneImagen());
    }

    // ============ MÉTODO CLONE ============

    /**
     * Crea una copia del producto (útil para edición sin afectar el original)
     */
    public Producto clone() {
        Producto copia = new Producto();
        copia.id = this.id;
        copia.nombre = this.nombre;
        copia.descripcion = this.descripcion;
        copia.precio = this.precio;
        copia.stock = this.stock;
        // Deep copy of the image byte array
        if (this.imagen != null) {
            copia.imagen = Arrays.copyOf(this.imagen, this.imagen.length);
        } else {
            copia.imagen = null;
        }
        copia.idCategoria = this.idCategoria;
        copia.nombreCategoria = this.nombreCategoria;
        return copia;
    }
}