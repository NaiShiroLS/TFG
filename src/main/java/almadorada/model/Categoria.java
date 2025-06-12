package main.java.almadorada.model;

import java.util.Objects; // Importar Objects

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion;

    public Categoria() {
    }

    public Categoria(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Categoria(String nombre, String descripcion) {
        this(0, nombre, descripcion);
    }

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

    // ============ MÉTODOS AÑADIDOS PARA JComboBox ============
    @Override
    public String toString() {
        return nombre; // Importante para que el JComboBox muestre el nombre de la categoría
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id == categoria.id; // Comparar por ID es suficiente para la igualdad en un JComboBox
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}