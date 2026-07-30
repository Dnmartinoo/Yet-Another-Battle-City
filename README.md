# Yet Another Battle City

![Java](https://img.shields.io/badge/Java-24-ED8B00?logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-20-2F74C0)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success)

A JavaFX recreation of the classic **Battle City**, featuring local multiplayer, XML-based levels, multiple enemy behaviors, power-ups, collision handling, sound effects, and a layered architecture that separates the game domain from its graphical interface.

This project was co-developed by **Martino De Ninis** and **Agustín Sauer** as part of the Programming Paradigms course at the Faculty of Engineering of the University of Buenos Aires (FIUBA).

## Features

- Single-player and local two-player game modes
- Three levels loaded from XML files
- Four enemy types with different attributes and behaviors
- Player lives and respawn system
- Projectile, movement, and collision handling
- Destructible and indestructible environment blocks
- Power-up system
- Sound effects and background music
- Victory and defeat states
- Separation between game logic and JavaFX presentation

## Gameplay

The players control tanks and must protect their base while defeating all enemy tanks in each level.

The game includes different types of terrain:

- **Brick:** destructible after receiving multiple impacts
- **Steel:** indestructible and blocks projectiles
- **Water:** blocks tank movement but allows projectiles to pass
- **Forest:** allows movement and projectiles while visually covering entities
- **Base:** must be protected from enemy attacks

### Enemy types

The game includes four enemy variants with different characteristics:

- **Basic:** standard movement and durability
- **Fast:** increased movement speed
- **Powerful:** higher firing frequency
- **Armored:** requires multiple hits to be destroyed

### Power-ups

- **Grenade:** destroys all active enemies
- **Helmet:** grants temporary invulnerability
- **Star:** improves the player's projectile power

## Controls

### Player 1

| Action | Key |
|---|---|
| Move up | `W` |
| Move left | `A` |
| Move down | `S` |
| Move right | `D` |
| Shoot | `Space` |

### Player 2

| Action | Key |
|---|---|
| Move | Arrow keys |
| Shoot | `Enter` |

### Menus

| Action | Key |
|---|---|
| Navigate options | Up / Down arrows |
| Confirm selection | `Enter` |

## Tech Stack

- **Java 24**
- **JavaFX 20**
- **Maven**
- **XML**
- **Object-Oriented Programming**
- **Git and GitHub**

## Architecture

The application follows a layered architecture that separates the game domain from the JavaFX user interface.

### Domain model

The model contains the core game rules and entities:

- Players and enemies
- Projectiles
- Power-ups
- Environment blocks
- Collision detection
- Physics and movement
- Level progression
- Victory and defeat conditions
- Enemy spawning and behavior

### Presentation layer

The JavaFX layer is responsible for:

- Rendering entities and sprites
- Handling keyboard input
- Displaying menus and game states
- Playing music and sound effects
- Coordinating the graphical game loop

### Architectural decisions

Some of the main design decisions include:

- Separation between model and presentation
- Polymorphic implementations for blocks, enemies, and power-ups
- Factory-based creation of blocks, levels, and game components
- XML-based level configuration
- Dedicated managers for projectiles, power-ups, spawning, and rendering
- A port-and-adapter boundary for sound integration
- Game-state objects used to communicate model information to the presentation layer

The game model does not directly depend on JavaFX rendering components, reducing coupling between the business logic and the graphical interface.

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── org/example/
│   │       ├── modelo/
│   │       │   ├── disparo/
│   │       │   ├── entorno/
│   │       │   ├── fisica/
│   │       │   ├── juego/
│   │       │   ├── niveles/
│   │       │   ├── personajes/
│   │       │   ├── powerup/
│   │       │   └── puertos/
│   │       └── vista/
│   │           ├── assets/
│   │           ├── campania/
│   │           ├── config/
│   │           ├── core/
│   │           ├── menu/
│   │           └── render/
│   └── resources/
│       ├── audio/
│       ├── sprites/
│       └── levels/
└── pom.xml
```

## Requirements

Before running the project, make sure the following tools are installed:

- Java Development Kit 24 or newer
- Apache Maven
- Git

Verify the installation with:

```bash
java --version
mvn --version
git --version
```

## Installation

Clone the repository:

```bash
git clone https://github.com/Dnmartinoo/Yet-Another-Battle-City.git
cd Yet-Another-Battle-City
```

Compile the project:

```bash
mvn clean compile
```

Run the game:

```bash
mvn javafx:run
```

## Level Configuration

Levels are defined through XML files.

This allows the game maps, player positions, enemy positions, blocks, and other level information to be modified without changing the main game engine.

The level loader transforms the XML configuration into domain objects used by the game model.

## Design Highlights

### Model-view separation

The game rules are implemented independently from the JavaFX presentation layer. Rendering and input handling consume the state exposed by the model without defining the domain behavior.

### Collision system

Entities use hitboxes and vector-based positions to manage:

- Tank movement
- Projectile movement
- Collisions with solid blocks
- Projectile impacts
- Environment interactions

### Extensible entities

Different blocks, enemies, and power-ups implement specific behaviors through polymorphism. This allows new variants to be incorporated without centralizing every rule in a single class.

### Level loading

The XML level loader allows levels to be created from external configuration files, separating map definition from game execution.

### Audio decoupling

The game logic communicates with the sound implementation through a dedicated port, preventing the domain model from depending directly on JavaFX audio classes.

## Development Process

The project was developed collaboratively by a two-person team.

Both contributors shared responsibilities across:

- Domain modeling
- Java implementation
- JavaFX integration
- Game mechanics
- Debugging
- Refactoring
- Merge conflict resolution
- Documentation

Git and GitHub were used for version control, branch integration, and collaborative development.


### Martino De Ninis

Computer Engineering student at the University of Buenos Aires.

- GitHub: [Dnmartinoo](https://github.com/Dnmartinoo)

## Project Status

The academic version of the project is complete and playable.

Possible future improvements include:

- Automated unit and integration tests
- Continuous integration with GitHub Actions
- A downloadable packaged release
- Configurable controls
- Additional levels and enemy behaviors
- Improved menu and settings system
- Persistence of scores and game progress

## Academic Context

This project was originally developed in a private GitHub organization for the Programming Paradigms course at FIUBA.

The public repository was created with authorization for portfolio and professional presentation purposes.

## Disclaimer

This is a non-commercial educational project inspired by **Battle City**.

Battle City and any original intellectual property associated with it belong to their respective owners. This project was created exclusively for academic and portfolio purposes.
