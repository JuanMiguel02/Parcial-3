# Sistema de Gestión de Citas Médicas – JavaFX

Aplicación de escritorio desarrollada con **JavaFX** para gestionar pacientes, médicos y citas médicas dentro de una clínica.
El sistema permite registrar, consultar, modificar y eliminar información relacionada con la asignación de citas.
##  Características

- **Dashboard principal** que carga tres módulos en FXML: Pacientes, Médicos, Citas
- **Formularios dinámicos** para registro y actualización de datos
- **Validación automatica** de horarios para evitar conflictos entre citas
- Interfaz construida con JavaFX usando FXML
- Uso de Programación Orientada a Objetos (herencia, abstracción, encapsulamiento)
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
https://github.com/JuanMiguel02/Parcial-3/tree/main
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
sistema-citas/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/demo/
│       │       ├── Launcher.java
│       │       ├── Controllers/
│       │       │   ├── PacienteController.java
│       │       │   ├── MedicoController.java
│       │       │   └── CitasController.java
│       │       ├── Models/
│       │       │   ├── Persona.java
│       │       │   ├── Paciente.java
│       │       │   ├── Medico.java
│       │       │   └── Cita.java
│       │       └── Repositories/
│       │           ├── PacienteRepository.java
│       │           ├── MedicoRepository.java
│       │           └── CitaRepository.java
│       └── resources/
│           └── org/demo/
│               ├── Dashboard.fxml
│               ├── Pacientes.fxml
│               ├── Medicos.fxml
│               └── Citas.fxml
└── pom.xml

```
---

##  Uso

### Módulo de Pacientes
- Registrar pacientes
- Editar y eliminar información existente
- Visualizar la lista completa de pacientes

### Módulo de Médicos
- Registrar médicos con especialidad y consultorio
- Editar y eliminar médicos
- Consultar listado completo

### Módulo de Citas
- Registrar citas asignando paciente y médico
- Agregar fecha, hora y precio
- Verificar horarios para evitar duplicados
- Editar o cancelar citas existentes

---

## Arquitectura

### Patrón MVC
El proyecto implementa el patrón **Modelo-Vista-Controlador** para separar la lógica de negocio de la interfaz gráfica.

- **Modelo**: Clases `Cita`, `Paciente`, `Medico`, `Persona`
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

El sistema incluye datos de ejemplo pre cargados para cada módulo:
- Pacientes de ejemplo
- Médicos de ejemplo
- Citas con fecha y hora

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

|      Imagen       | Descripción       | Fuente                                                                                               | Licencia                                              |
|:-----------------:|-------------------|------------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| **pacientes.png** | Icono de clientes | [Gente de negocios – Kornkun (Flaticon)](https://www.flaticon.es/iconos-gratis/gente-de-negocios)    | [Licencia Flaticon](https://www.flaticon.com/license) |
|  **doctor.png**   | Icono de doctor   | [Doctor icons created by Freepik - Flaticon](https://www.flaticon.com/free-icons/doctor "doctor icons")   | [Licencia Flaticon](https://www.flaticon.com/license) |
|   **citas.png**   | Icono de cita     | [Doctor icons created by srip - Flaticon](https://www.flaticon.com/free-icons/doctor "doctor icons")| [Licencia Flaticon](https://www.flaticon.com/license) |



> Las imágenes son utilizadas únicamente con fines educativos.

---

##  Autor

Proyecto desarrollado por **Juan Miguel Henao Gaviria y Jerónimo Delgado Estrada**  
Desarrollado para el curso de Programación I - Universidad del Quindío

---

##  Licencia

Este proyecto se distribuye bajo la licencia **MIT**, lo que permite su uso, modificación y distribución libre siempre que se mantenga la atribución al autor original.
