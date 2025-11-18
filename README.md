#  Sistema de Gestión de Ventas - JavaFX

Aplicación de escritorio desarrollada con **JavaFX** que permite gestionar clientes, productos y ventas de forma integrada.

##  Características

- **Dashboard principal** con acceso a clientes, productos y ventas
- **Formularios dinámicos** para crear y modificar registros
- **Cálculo automático** del total en las ventas
- **Actualización automática** del inventario de productos
- **Interfaz moderna y proporcional** con diseño en `GridPane` y `AnchorPane`
- **Arquitectura MVC** con separación clara entre modelo, vista y controlador

---

##  Requisitos

- **Java 25** o superior
- **JavaFX 24 SDK**
- **Maven 3.6** o superior
- Sistema operativo: Windows, Linux o macOS

---

## Instalación y Ejecución

```bash
https://github.com/JuanMiguel02/Taller5-POO-Ventas.git
```


### Compilar el proyecto

```bash
mvn clean compile
```

### Ejecutar la aplicación

```bash
mvn javafx:run
```

### Alternativamente (IDE)

Ejecutar la clase principal:

```bash
org.demo.Launcher
```

Con las siguientes opciones VM si es necesario:

```
--module-path "ruta/a/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
```

---

## Estructura del Proyecto

```
sistema-gestion/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/demo/
│       │       ├── MainApp.java                     # Clase principal
│       │       ├── Controllers/                     # Controladores FXML
│       │       │   ├── ClienteController.java
│       │       │   ├── ProductoController.java
│       │       │   └── VentaController.java
│       │       ├── Models/                          # Clases del modelo
│       │       │   ├── Cliente.java
│       │       │   ├── Producto.java
│       │       │   └── Venta.java
│       │       └── Repositories/                    # Gestión de datos
│       │           ├── ClienteRepository.java
│       │           ├── ProductoRepository.java
│       │           └── VentaRepository.java
│       │       └── Utils/                          #Clases auxiliares
│       │           └── AlertHelper.java            
│       └── resources/
│           └── org/demo/
│               └── org/demo/Images/                #Imagenes del proyecto
│               ├── Dashboard.fxml
│               ├── Clientes.fxml
│               ├── Productos.fxml
│               └── Ventas.fxml
└── pom.xml                                           # Configuración Maven
```

---

##  Uso

### Módulo de Clientes
- Registrar nuevos clientes
- Editar y eliminar información existente
- Visualizar la lista completa de clientes

### Módulo de Productos
- Crear, modificar o eliminar productos
- Consultar inventario en tiempo real
- Actualizar cantidad automáticamente tras cada cita

### Módulo de Ventas
- Registrar una cita seleccionando cliente y producto
- Calcular total automáticamente
- Reducir existencias del producto vendido

---

## Arquitectura

### Patrón MVC
El proyecto implementa el patrón **Modelo-Vista-Controlador** para separar la lógica de negocio de la interfaz gráfica.

- **Modelo**: Clases `Cliente`, `Producto`, `Venta`
- **Vista**: Archivos FXML
- **Controlador**: Clases Java que gestionan la interacción con la vista

### Repositorios en Memoria
Los repositorios gestionan las listas de datos y permiten agregar, eliminar y actualizar registros sin necesidad de base de datos externa.

---

##  Interfaz

- Diseño proporcional mediante `GridPane` y `AnchorPane`
- Formularios distribuidos y alineados uniformemente
- Botones estilizados con sombras suaves y bordes redondeados
- Tablas responsivas con ancho adaptable

---

## Datos de Ejemplo

El sistema incluye datos de ejemplo precargados para cada módulo:
- **Clientes:** Simón Bolivar, Armando Casas, Chino Moreno
- **Productos:** Coca Cola, Detodito, Chocorramo
- **Ventas:** Asociadas a los clientes y productos anteriores

---

##  Tecnologías Utilizadas

- **Java 25**: Lenguaje principal
- **JavaFX 24**: Framework de interfaz gráfica
- **FXML**: Definición estructurada de vistas
- **Maven**: Gestión de dependencias
- **Scene Builder**: Diseño visual de las interfaces

---

## ️ Atribución de Imágenes

Las imágenes utilizadas provienen de [Flaticon](https://www.flaticon.com) bajo licencia libre con atribución:

|      Imagen       | Descripción        | Fuente                                                                                                | Licencia                                              |
|:-----------------:|--------------------|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| **clientes.png**  | Icono de clientes  | [Gente de negocios – Kornkun (Flaticon)](https://www.flaticon.es/iconos-gratis/gente-de-negocios)     | [Licencia Flaticon](https://www.flaticon.com/license) |
| **productos.png** | Icono de productos | [Producto iconos creados por HANIS - Flaticon](https://www.flaticon.es/iconos-gratis/producto)        | [Licencia Flaticon](https://www.flaticon.com/license) |
|   **cita.png**   | Icono de cita     | [Garrapata iconos creados por Roundicons - Flaticon](https://www.flaticon.es/iconos-gratis/garrapata) | [Licencia Flaticon](https://www.flaticon.com/license) |



> Las imágenes son utilizadas únicamente con fines educativos.

---

##  Autor

Proyecto desarrollado por **Juan Miguel Henao Gaviria**  
Desarrollado para el curso de Programación I - Universidad del Quindío

---

##  Licencia

Este proyecto se distribuye bajo la licencia **MIT**, lo que permite su uso, modificación y distribución libre siempre que se mantenga la atribución al autor original.
