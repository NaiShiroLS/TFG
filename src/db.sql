-- Crear base de datos
CREATE DATABASE IF NOT EXISTS alma_dorada DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE alma_dorada;

-- Tabla: categorias
CREATE TABLE categorias (
    id_categoria INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- Tabla: productos
CREATE TABLE productos (
    id_producto INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(8,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    imagen LONGBLOB,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- Tabla: pedidos
CREATE TABLE pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nombre_cliente VARCHAR(100) NOT NULL,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    direccion TEXT,
    productos TEXT NOT NULL, -- Puede ser texto plano o JSON (según cómo lo manejes en Java)
    subtotal DECIMAL(8,2) NOT NULL,
    total DECIMAL(8,2) NOT NULL,
    notas_adicionales TEXT,
    estado VARCHAR(50) DEFAULT 'Pendiente'
);

-- Tabla: usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL
);
