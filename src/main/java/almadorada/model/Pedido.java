package main.java.almadorada.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Pedido {

    private int idPedido;
    private LocalDateTime fecha;
    private String nombreCliente;
    private String correo;
    private String telefono;
    private String direccion;
    private String productos;
    private double subtotal;
    private double total;
    private String notasAdicionales;
    private String estado;

    public Pedido() {
    }

    public Pedido(LocalDateTime fecha, String nombreCliente, String correo, String telefono,
                  String direccion, String productos, double subtotal, double total,
                  String notasAdicionales, String estado) {

        this.fecha = fecha;
        this.nombreCliente = nombreCliente;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.productos = productos;
        this.subtotal = subtotal;
        this.total = total;
        this.notasAdicionales = notasAdicionales;
        this.estado = estado;
    }



    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNotasAdicionales() {
        return notasAdicionales;
    }

    public void setNotasAdicionales(String notasAdicionales) {
        this.notasAdicionales = notasAdicionales;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido #" + idPedido + " - " + nombreCliente + " (" + estado + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return idPedido == pedido.idPedido;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido);
    }
}
