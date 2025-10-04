# YABC - Yet Another Battle City

## Universidad, Facultad y Materia
- **Universidad:** Universidad de Buenos Aires (UBA)
- **Facultad:** Facultad de Ingeniería (FIUBA)
- **Materia:** Paradigmas de Programación (75.07)

## Docentes y Corrector
- **Docentes:** [Completar con los nombres de los docentes]
- **Docente Corrector:** [Completar con el nombre del corrector asignado]

## Integrantes del Grupo
- **Miguel Borja** - [Completar con datos completos]
- **[Nombre del segundo integrante]** - [Completar con datos completos]

## Nombre del Grupo
**[Completar con el nombre del grupo]**

## Descripción del Proyecto
YABC (Yet Another Battle City) es una implementación en Java del clásico juego Battle City. El juego consiste en controlar tanques para defender una base mientras se eliminan tanques enemigos en diferentes niveles.

### Características principales:
- Juego para 1 o 2 jugadores simultáneos
- 3 niveles diferentes cargados desde archivos XML
- 4 tipos de tanques enemigos con diferentes características
- Sistema de power-ups (Granada, Casco, Estrella)
- Física de colisiones y movimiento continuo
- Sistema de sonidos y efectos visuales
- Arquitectura separada en capas (Modelo y Vista)

### Tipos de bloques del entorno:
- **Ladrillos:** Destructibles (3 impactos)
- **Acero:** Indestructibles
- **Agua:** Bloquea movimiento pero no disparos
- **Bosque:** No afecta movimiento ni disparos, solo oculta visualmente

### Tipos de enemigos:
- **Básico:** Lento y débil (marrón)
- **Rápido:** Se mueve velozmente (azul)
- **Potente:** Dispara más rápido (rojo)
- **Blindado:** Requiere 3 disparos para destruirse (anaranjado)

## Instrucciones para Ejecución
### Prerrequisitos
- Java 24 (o superior)
- Maven
- JavaFX

### Compilación y ejecución
```bash
# Clonar el repositorio
git clone [URL_DEL_REPOSITORIO]
cd tp1-miguel-borja

# Compilar el proyecto
mvn clean compile

# Ejecutar el juego
mvn javafx:run
```

### Ejecución desde IDE
1. Abrir el proyecto en un IDE compatible (IntelliJ IDEA, Eclipse, VS Code)
2. Configurar JavaFX en el classpath
3. Ejecutar la clase principal `org.example.modelo.App`

## Instrucciones de Juego (Comandos)
### Controles Jugador 1:
- **Movimiento:** W (arriba), A (izquierda), S (abajo), D (derecha)
- **Disparar:** ESPACIO

### Controles Jugador 2:
- **Movimiento:** Flechas del teclado (↑↓←→)
- **Disparar:** ENTER

### Objetivo:
- Defender la base (águila) de los ataques enemigos
- Eliminar todos los tanques enemigos del nivel
- Completar los 3 niveles para ganar
- Evitar que destruyan la base o que pierdas todas las vidas

### Power-ups disponibles:
- **Granada:** Destruye todos los enemigos en pantalla
- **Casco:** Otorga invulnerabilidad temporal por 10 segundos
- **Estrella:** Mejora el disparo (mata cualquier tanque con un solo disparo)

## Diagrama de Clases UML
El diagrama de clases completo del modelo se encuentra en el archivo `diagrama-clases-modelo.puml` en formato PlantUML.

### Principales paquetes del modelo:
- **Física:** Manejo de vectores, rectángulos, colisiones y mundo físico
- **Personajes:** Tanques (jugadores y enemigos) con diferentes comportamientos
- **Entorno:** Bloques del mapa (ladrillos, acero, agua, bosque, base)
- **Disparo:** Proyectiles y equipos
- **PowerUps:** Sistema de mejoras temporales
- **Juego Core:** Motor del juego, niveles, spawning y gestores
- **Niveles:** Carga de configuración desde XML

### Principios de diseño aplicados:
- **Separación de responsabilidades:** Modelo independiente de la vista
- **Polimorfismo:** Interfaces Cuerpo, Bloque, PowerUp, Spriteeable
- **Factory Pattern:** BloqueFactory para creación de bloques
- **Strategy Pattern:** Diferentes tipos de tanques y power-ups
- **Command Pattern:** ComandoPowerUp para efectos de power-ups

## Arquitectura
El proyecto sigue una arquitectura en capas con separación clara entre:
- **Modelo:** Lógica del juego, entidades, física (paquete `org.example.modelo`)
- **Vista:** Interfaz gráfica, renderizado, eventos de usuario (paquete `org.example.vista`)
- **Control:** Coordinación entre modelo y vista

**Importante:** Las clases del modelo no tienen dependencias hacia JavaFX ni clases de la vista, cumpliendo con los requerimientos de arquitectura limpia.

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Sn8wv7lZ)
