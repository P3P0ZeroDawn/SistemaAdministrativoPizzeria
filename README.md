# Sistema Administrativo de Pizzería "Italia Pizza"

Sistema de escritorio desarrollado en Java para la administración de la pizzería Italia pizza. Permite gestionar usuarios, productos, pedidos e inventario mediante una interfaz gráfica moderna basada en JavaFX y una base de datos MySQL.
Este proyecto fue realizado para la materia de *Programación para Aplicaciones de Escritorio* para la carrera de *Ingeniería de Software* de la *Universidad Veracruzana*.

## Descripción

El objetivo del sistema es automatizar los procesos administrativos y operativos de una pizzería, facilitando el control de productos, pedidos, usuarios y validaciones de inventario.

La aplicación fue desarrollada utilizando JavaFX para la interfaz gráfica y MySQL para el almacenamiento persistente de la información.

---

## Características

### Gestión de Usuarios

- Inicio de sesión.
- Registro de usuarios.
- Consulta de usuarios.
- Edición de usuarios.
- Eliminación de usuarios.
- Encriptación de contraseñas.

### Gestión de Productos

- Registro de productos.
- Consulta de productos.
- Modificación de productos.
- Eliminación de productos.
- Asociación de componentes de elaboración.

### Gestión de Pedidos

- Registro de pedidos.
- Consulta de pedidos.
- Actualización de estatus.
- Relación entre pedidos y productos.

### Inventario

- Validaciones de inventario.
- Historial de movimientos.
- Control de existencias.
- Verificación de disponibilidad de insumos.

### Reportes y Exportación

- Exportación de información.
- Generación de reportes.
- Soporte para formatos CSV y PDF.

---

## Tecnologías Utilizadas

| Tecnología | Uso |
|------------|-----|
| Java | Lógica de negocio |
| JavaFX | Interfaz gráfica |
| Maven | Gestión de dependencias |
| MySQL | Base de datos |
| JDBC | Conexión a la base de datos |
| OpenCSV | Exportación CSV |
| iText | Generación de PDF |
| Ikonli | Iconografía para JavaFX |

---

## Arquitectura

El proyecto sigue una arquitectura organizada por capas:

```text
src/main/java
│
├── controladores
│   ├── Pantallas JavaFX
│   └── Componentes reutilizables
│
├── modelo
│   ├── beans
│   ├── dao
│   └── conexión MySQL
│
└── excepciones
```

### Componentes principales

#### Modelo

Representa las entidades del sistema:

- Usuario
- Producto
- Pedido
- HistorialInventario
- ComponenteElaboracion
- ProductoPedido

#### DAO

Encargados del acceso a datos:

- UsuarioDAO
- ProductoDAO
- PedidoDAO
- HistorialInventarioDAO
- ProductoPedidoDAO
- ComponenteElaboracionDAO

#### Controladores

Administran la interacción entre la interfaz y la lógica del sistema.

Ejemplos:

- InicioSesionController
- ConsultaProductosController
- ConsultaPedidosController
- ConsultaUsuariosController
- RealizarValidacionInventarioController

---

## Requisitos

- JDK 11 o superior
- Maven 3.8+
- MySQL Server
- JavaFX

---

## Configuración de Base de Datos

1. Crear una base de datos en MySQL.
2. Ejecutar los scripts SQL incluidos en el proyecto.
3. Configurar las credenciales de conexión en:

```java
MySQLConnectionManager.java
```

Ejemplo:

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/pizzeria";

private static final String USER =
    "root";

private static final String PASSWORD =
    "password";
```

---

## Instalación

Clonar el repositorio:

```bash
git clone https://github.com/P3P0ZeroDawn/SistemaAdministrativoPizzeria.git
```

Entrar al proyecto:

```bash
cd SistemaAdministrativoPizzeria
```

Compilar:

```bash
mvn clean install
```

Ejecutar:

```bash
mvn javafx:run
```

---

## Dependencias Principales

- JavaFX Controls
- JavaFX FXML
- JavaFX Swing
- MySQL Connector/J
- OpenCSV
- iText PDF
- Ikonli JavaFX
- JUnit 5
- Mockito

---

## Seguridad

El sistema implementa:

- Encriptación de contraseñas.
- Validación de datos de entrada.
- Manejo de excepciones personalizadas.
- Control de acceso mediante autenticación.

---

## Autores

Pedro Enrique Sánchez Rodríguez
Emiliano Morales Baizabal
César Daniel Ortega Castillejos

---

## Licencia

Proyecto desarrollado con fines académicos y educativos.
