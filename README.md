# Manual de Usuario: Aplicación de Administración "Alma Dorada"

**Versión:** 1.0

**Fecha de Publicación:** 12 de Junio de 2025

---

## 1. Introducción

Bienvenido al manual de usuario de la aplicación de gestión administrativa para "Alma Dorada", su tienda de bolsos de crochet artesanales. Esta aplicación de escritorio ha sido diseñada para digitalizar y optimizar los procesos internos de gestión de inventario, reduciendo errores y mejorando la eficiencia.

La aplicación está desarrollada en Java utilizando Swing para la interfaz gráfica de usuario y MySQL como sistema de gestión de base de datos. Permite realizar operaciones completas de creación, lectura, actualización y eliminación (CRUD) para productos, categorías e inventario, e incluye un sistema de autenticación seguro y la generación de informes básicos de stock.

Este manual le guiará a través de la configuración inicial del proyecto, la ejecución de la aplicación y el uso de sus funcionalidades principales, desde el inicio de sesión hasta la gestión de sus productos y el inventario.

## 2. Requisitos del Sistema

Para un funcionamiento óptimo de la aplicación, asegúrese de que su sistema cumple con los siguientes requisitos:

* **Sistema Operativo:** Windows, macOS, o Linux.
* **Java Development Kit (JDK):** Versión 8 o superior (necesario para compilar y ejecutar aplicaciones Java).
* **Entorno de Desarrollo Integrado (IDE):** Se recomienda un IDE como IntelliJ IDEA, Eclipse o Apache NetBeans para importar y gestionar el proyecto.
* **Base de Datos:** MySQL Server (la aplicación se conectará a una base de datos MySQL existente).
* **Memoria RAM:** Mínimo 4 GB (se recomiendan 8 GB para un rendimiento óptimo).
* **Espacio en Disco:** Mínimo 200 MB para el proyecto y dependencias.

## 3. Configuración Inicial del Proyecto y Base de Datos

Esta sección detalla los pasos para configurar el proyecto en su entorno de desarrollo y preparar la base de datos.

### 3.1. Configurar la Base de Datos MySQL

1.  **Instalar y Ejecutar MySQL Server:** Si no lo tiene, instale MySQL Server en su sistema.
2.  **Crear la Base de Datos y Tablas:**
    * Localice el archivo `alma_dorada.sql` proporcionado junto con el código fuente del proyecto.
    * Utilice una herramienta de gestión de bases de datos (como MySQL Workbench, phpMyAdmin o la línea de comandos de MySQL) para ejecutar este script.
    * Este script creará la base de datos `alma_dorada` (o la que se configure en el proyecto) y todas las tablas necesarias (`productos`, `categorias`, `usuarios`, `pedidos`, etc.), además de insertar datos iniciales si los hay.
3.  **Configurar Credenciales de Conexión:**
    * La aplicación necesitará las credenciales para conectarse a su base de datos MySQL. Revise los archivos de configuración del proyecto (comúnmente en una clase de utilidad o un archivo de propiedades, por ejemplo, `DatabaseConnection.java` si existe) para establecer el **nombre de usuario**, la **contraseña** y la **URL de la base de datos** (host, puerto y nombre de la base de datos) de su instalación de MySQL.

### 3.2. Importar el Proyecto en un IDE

1.  **Instalar JDK:** Asegúrese de tener un JDK (Java Development Kit) versión 8 o superior instalado en su sistema.
2.  **Instalar un IDE:** Descargue e instale un IDE como IntelliJ IDEA (Community Edition es gratuita), Eclipse o Apache NetBeans.
3.  **Importar el Proyecto:**
    * **En IntelliJ IDEA:** Seleccione `File > Open...` y navegue hasta la carpeta raíz del proyecto `AlmaDorada`. Seleccione la carpeta y haga clic en `Open`. IntelliJ debería detectar automáticamente que es un proyecto Java/Maven/Gradle (dependiendo de cómo esté configurado) e importar las dependencias.
    * **En Eclipse:** Seleccione `File > Import... > Maven > Existing Maven Projects` (si es un proyecto Maven) o `General > Existing Projects into Workspace`. Navegue hasta la carpeta raíz del proyecto `AlmaDorada`.
    * **En NetBeans:** Seleccione `File > Open Project...` y navegue hasta la carpeta raíz del proyecto `AlmaDorada`.
4.  **Configurar las Librerías (Dependencias):**
    * La aplicación requiere librerías externas, principalmente el **conector JDBC para MySQL** (para conectar Java con MySQL).
    * **Si el proyecto usa Maven o Gradle (recomendado):**
        * El IDE debería descargar automáticamente las dependencias definidas en el archivo `pom.xml` (Maven) o `build.gradle` (Gradle). Asegúrese de que su IDE tiene configurado el auto-import para dependencias.
        * Verifique que la dependencia del conector MySQL esté presente en su `pom.xml` o `build.gradle`, algo similar a:
            ```xml
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.28</version> </dependency>
            ```
            o
            ```gradle
            // Para Gradle en build.gradle
            implementation 'mysql:mysql-connector-java:8.0.28' // Use la versión adecuada y actualizada
            ```
    * **Si el proyecto no usa Maven/Gradle (menos común para proyectos grandes):**
        * Deberá descargar el archivo `.jar` del conector JDBC de MySQL (por ejemplo, `mysql-connector-java-8.0.28.jar`) desde el sitio web oficial de MySQL.
        * En su IDE, vaya a la configuración del módulo/proyecto y añada este `.jar` al **Classpath** o a la sección de **Libraries**. Por ejemplo, en IntelliJ IDEA: `Project Structure (Ctrl+Alt+Shift+S) > Modules > Dependencies > + > JARs or directories...`.

## 4. Ejecutar la Aplicación

Una vez que el proyecto esté importado y las dependencias resueltas en su IDE:

1.  **Localice la Clase Principal:** La clase principal con el método `main` que inicia la aplicación (probablemente `Main.java` o similar, ubicada en la carpeta `src/main/java`).
2.  **Ejecutar:** Haga clic derecho en la clase principal y seleccione `Run 'ClasePrincipal.main()'` o haga clic en el botón de "Play" (ejecutar) en la barra de herramientas del IDE.

## 5. Inicio de Sesión

Al iniciar la aplicación, se le presentará la pantalla de inicio de sesión.

1.  **Introduzca su Nombre de Usuario:** Escriba su nombre de usuario en el campo "Usuario".
2.  **Introduzca su Contraseña:** Escriba su contraseña en el campo "Contraseña".
3.  **Haga clic en "Iniciar Sesión":** Si las credenciales son correctas, accederá a la interfaz principal de la aplicación. Si son incorrectas, se mostrará un mensaje de error.
    * *(Nota: Por defecto, podría haber un usuario de prueba como "admin" con una contraseña predefinida, o deberá crearlo directamente en la base de datos si es la primera vez que inicia la aplicación).*

## 6. Interfaz Principal de la Aplicación

Una vez iniciada la sesión, verá la interfaz principal de la aplicación, que generalmente incluirá un menú o pestañas para navegar entre las diferentes secciones:

* **Gestión de Productos:** Para añadir, editar, eliminar y visualizar productos.
* **Gestión de Categorías:** Para administrar las categorías de los productos.
* **Gestión de Pedidos:** Para ver el listado de pedidos (mencionado en `PedidoService.java`).
* **Gestión de Stock/Inventario:** Para consultar y gestionar el inventario de productos.
* **Usuarios:** (Posiblemente para administrar usuarios si tiene los permisos adecuados, relacionado con `AuthService.java` y `Usuario.java`).
* **Cerrar Sesión:** Para salir de su cuenta.

## 7. Gestión de Productos

Esta sección le permite administrar todos los productos disponibles en su tienda. Las operaciones son manejadas por `ProductoService.java`.

### 7.1. Ver Productos

1.  En la interfaz principal, haga clic en la opción "Productos" o similar.
2.  Se mostrará una tabla con el listado de todos los productos, incluyendo información como nombre, descripción, precio, categoría y stock actual.
3.  Puede utilizar los campos de búsqueda o filtros (si están disponibles) para encontrar productos específicos.

### 7.2. Añadir un Nuevo Producto

1.  En la sección de Productos, haga clic en el botón "Nuevo Producto" o "Añadir".
2.  Se abrirá un formulario donde deberá introducir la siguiente información:
    * **Nombre:** Nombre del producto (ej. "Bolso de mano de crochet").
    * **Descripción:** Detalles del producto (ej. "Hecho a mano con hilo de algodón orgánico").
    * **Precio:** Precio de venta del producto.
    * **Categoría:** Seleccione la categoría a la que pertenece el producto (ej. "Bolsos de mano", "Mochilas").
    * **Stock Inicial:** Cantidad de unidades disponibles en el inventario al añadir el producto.
3.  Haga clic en "Guardar" para añadir el producto a la base de datos.

### 7.3. Editar un Producto Existente

1.  En la tabla de Productos, seleccione el producto que desea editar.
2.  Haga clic en el botón "Editar" o "Modificar".
3.  Se abrirá un formulario precargado con la información actual del producto.
4.  Realice los cambios deseados en cualquiera de los campos.
5.  Haga clic en "Guardar" para actualizar la información del producto.

### 7.4. Eliminar un Producto

1.  En la tabla de Productos, seleccione el producto que desea eliminar.
2.  Haga clic en el botón "Eliminar".
3.  Se le pedirá una confirmación. Confirme la eliminación para borrar el producto de la base de datos.
    * **Advertencia:** La eliminación de un producto es irreversible. Asegúrese de que desea eliminarlo antes de confirmar.

## 8. Gestión de Categorías

Esta sección le permite organizar sus productos mediante categorías. Las operaciones son manejadas por `CategoriaService.java`.

### 8.1. Ver Categorías

1.  En la interfaz principal, haga clic en la opción "Categorías" o similar.
2.  Se mostrará una tabla con el listado de todas las categorías existentes.

### 8.2. Añadir una Nueva Categoría

1.  En la sección de Categorías, haga clic en el botón "Nueva Categoría" o "Añadir".
2.  Introduzca el nombre de la nueva categoría (ej. "Carteras", "Accesorios").
3.  Haga clic en "Guardar".

### 8.3. Editar una Categoría Existente

1.  En la tabla de Categorías, seleccione la categoría que desea editar.
2.  Haga clic en el botón "Editar" o "Modificar".
3.  Modifique el nombre de la categoría.
4.  Haga clic en "Guardar" para actualizarla.

### 8.4. Eliminar una Categoría

1.  En la tabla de Categorías, seleccione la categoría que desea eliminar.
2.  Haga clic en el botón "Eliminar".
3.  Se le pedirá una confirmación. Confirme la eliminación para borrar la categoría.
    * **Advertencia:** Si elimina una categoría, los productos asociados a ella podrían quedar sin categoría o deberá reasignarlos.

## 9. Gestión de Stock/Inventario

Esta sección se enfoca en el seguimiento y la consulta del inventario de productos. La lógica es manejada por `StockService.java`.

### 9.1. Consultar Stock de Productos

1.  En la interfaz principal, haga clic en la opción "Stock" o "Inventario".
2.  Verá un listado de productos con su stock actual.
3.  Puede que existan opciones para filtrar por productos con bajo stock o por categoría.

### 9.2. Actualizar Stock

* Aunque la aplicación se centra en la gestión y consulta, es probable que la actualización del stock se realice a través de la edición de productos (como se explica en la sección 7.3) o a través de módulos de entrada/salida de stock (si implementados).
* Si existe un módulo específico para "Entrada de Stock" o "Salida de Stock", utilícelo para registrar nuevas adquisiciones de productos o ventas.
    * **Entrada de Stock:** Para aumentar la cantidad de un producto en el inventario.
    * **Salida de Stock:** Para disminuir la cantidad de un producto (por ejemplo, después de una venta).

## 10. Generación de Informes de Stock (Básico)

La aplicación permite generar informes básicos de stock.

1.  En la sección de "Stock" o "Informes", busque la opción "Generar Informe" o "Exportar".
2.  Es posible que pueda seleccionar el tipo de informe (ej. listado de todo el stock, stock bajo mínimos).
3.  El informe se generará, probablemente en un formato legible dentro de la aplicación o exportable a un archivo (CSV, PDF, etc.).

## 11. Gestión de Pedidos (Visualización)

La aplicación incluye la lógica para la gestión de pedidos (`PedidoService.java`). Dependiendo de la implementación, puede:

1.  **Ver Pedidos:** Acceda a la sección "Pedidos" para ver el listado de pedidos registrados.
2.  **Detalles del Pedido:** Haga clic en un pedido para ver los productos incluidos y el estado del mismo.

## 12. Cerrar Sesión

Para salir de su sesión de usuario de forma segura:

1.  Haga clic en el botón "Cerrar Sesión" o "Salir" en la interfaz principal.
2.  Esto le devolverá a la pantalla de inicio de sesión.

## 13. Solución de Problemas (Troubleshooting)

* **"No puedo iniciar sesión":**
    * Verifique que su nombre de usuario y contraseña sean correctos (distingue entre mayúsculas y minúsculas).
    * Asegúrese de que la base de datos MySQL está funcionando y que la aplicación está conectada correctamente con las credenciales correctas.
    * Compruebe si el usuario de prueba está insertado en la tabla `usuarios` de su base de datos.
* **"El proyecto no compila o la aplicación no se inicia":**
    * Compruebe que tiene el **Java Development Kit (JDK)** versión 8 o superior instalado y configurado correctamente en su IDE.
    * Asegúrese de que todas las librerías necesarias, especialmente el **conector JDBC para MySQL**, estén correctamente añadidas al `classpath` del proyecto en su IDE. Si usa Maven/Gradle, asegúrese de que las dependencias se han descargado correctamente.
    * Revise la ventana de "Problemas" o "Errores" de su IDE para ver mensajes de error específicos durante la compilación.
* **"Error al conectar con la base de datos":**
    * Verifique que el servidor MySQL está en ejecución.
    * Compruebe que las credenciales de conexión a la base de datos (usuario, contraseña, URL) en el código fuente de la aplicación son correctas y coinciden con su configuración de MySQL.
    * Asegúrese de que el puerto de MySQL (por defecto 3306) no esté bloqueado por un firewall.
    * Confirme que el script `alma_dorada.sql` se ha ejecutado correctamente y la base de datos y tablas existen.
* **"No se muestran datos en las tablas":**
    * Asegúrese de que hay datos en las tablas correspondientes de su base de datos MySQL. Puede consultarlos directamente con una herramienta como MySQL Workbench.

## 14. Contacto y Soporte

Para cualquier pregunta, problema o sugerencia no cubierta en este manual, por favor, póngase en contacto con el soporte técnico o el desarrollador de la aplicación.
