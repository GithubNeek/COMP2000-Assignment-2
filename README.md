COMP2000  
Assignment 2: Grid Game with Weather and Design Patterns
-----------------------------------------------------------------------------------------------------------------------------
Overview
This project expands the fundamental Grid Game by including real-time weather data from an external HTTP 
server. It also employs key software design ideas, notably the Observer and Decorator design patterns,
as well as Java Streams and Lambda expressions.
The weather data (rain, windX, windY, etc.) dynamically alters the game environment, 
influencing the cost of character movement (fuel consumption) on the red cell.
-----------------------------------------------------------------------------------------------------------------------------
How to Compile and Run-
This project assumes you have Java 17 or later and a typical Java compiler/IDE (such as VS Code or IntelliJ).

1. Compile: Compile all .java files in the source directory:
 javac src/*.java
2. Run: Execute the Main class:
   java src/Main
   
   **Note:** The game initiates two concurrent threads:
1.  **`WeatherClient`**: Connects to the HTTP weather data stream (Assignment 2 requirement)
   and affects the movement cost.
2. **`WeatherStream`**: Runs the old, random `SUNNY`/`RAIN`/`SNOW` events every 5 seconds,
   which is used solely to provide the screen tint aesthetic (visual feedback).

------------------------------------------------------------------------------------------------------------------------------
Design Patterns and their implementations-
**The Observer Pattern**
The Observer Pattern is used to handle concurrent updates from weather sources.  It guarantees that when weather data changes, various dependents (Grid and GamePanel) are alerted, without requiring the weather sources to understand how those objects handle the data.

**Components of the Observer Pattern**

Subject: 'WeatherClient and 'WeatherStream'
Purpose: These are the fundamental causes of change.  They run on different threads, collect and create data, and alert all registered observers.

Observer Interface: 'WeatherObserver'
Purpose: Defines the communication contract.  It provides methods for both fresh numerical data and legacy events.

Concrete Observer: 'Grid' and 'Gamepanel'
Purpose: These objects react to updates.  The **'Grid'** receives numerical data for movement cost effects, whereas the **'GamePanel'** receives updates for screen repainting and aesthetic tint changes.

**The Decorator Pattern**
The Decorator Pattern is used to dynamically assign new responsibilities (e.g., higher mobility cost, custom graphics) to specific Cell objects without changing the fundamental terrain classes.  This enables weather effects to be applied and deleted properly.

**Decorator Pattern Components**

Component Interface: 'Cell' (Abstract Class)
Purpose: Defines the foundational contract that all terrain types and decorators must adhere to ( e.g getMovementCost(), paint() ).

Concrete Decorators: 'SnowDecorator' and 'WindDecorator'
Purpose: These classes cover an existing Cell object and change or enhance its behaviour.  They override getMovementCost() and paint() methods to add specialised weather effects (such as fuel cost or a visual overlay) to the base cell's functionality.

