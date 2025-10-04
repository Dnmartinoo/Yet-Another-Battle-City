# YABC - Yet Another Battle City

## Universidad, Facultad y Materia
- **Universidad:** Universidad de Buenos Aires (UBA)
- **Facultad:** Facultad de Ingeniería (FIUBA)
- **Materia:** Paradigmas de Programación (75.07)

## Docentes y Corrector
- **Docentes:** [Diego Essaya]
- **Docente Corrector:** [Leonel Rolon]

## Integrantes del Grupo
- **Martino De Ninis [112827]**
- **Agustín Sauer [112712]**

## Nombre del Grupo
**[Miguel Borja]**

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

## Instrucciones para Ejecución

### Prerrequisitos
- Java 24 (o superior)
- Maven

### Compilación y ejecución
```bash
git clone https://github.com/paradigmas-tb025-essaya/tp1-miguel-borja.git
cd tp1-miguel-borja


mvn clean compile


mvn javafx:run
```

## Instrucciones de Juego (Comandos)
### Controles Jugador 1:
- **Movimiento:** W (arriba), A (izquierda), S (abajo), D (derecha)
- **Disparar:** ESPACIO

### Controles Jugador 2:
- **Movimiento:** Flechas del teclado (↑↓←→)
- **Disparar:** ENTER

### Menus:
- **Seleccionar Un Jugador, Dos Jugadores, Salir** ENTER
- **Moverse entre Opciones** Flechas de teclado Verticales(↑↓)
