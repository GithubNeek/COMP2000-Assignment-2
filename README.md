COMP2000  
Assignment 2: Grid Game with Weather and Design Patterns

Overview
This project expands the fundamental Grid Game by including real-time weather data from an external HTTP 
server. It also employs key software design ideas, notably the Observer and Decorator design patterns,
as well as Java Streams and Lambda expressions.
The weather data (rain, windX, windY, etc.) dynamically alters the game environment, 
influencing the cost of character movement (fuel consumption) on the red cell.

How to Compile and Run-
This project assumes you have Java 17+ and a typical Java compiler/IDE (such as VS Code or IntelliJ).

1. Compile: Compile all .java files in the source directory:
 javac src/*.java
2. Run: Execute the Main class:
   java src/Main
   
   **Note:** The game initiates two concurrent threads:
1.  **`WeatherClient`**: Connects to the HTTP weather data stream (Assignment 2 requirement)
   and affects the movement cost.
2. **`WeatherStream`**: Runs the old, random `SUNNY`/`RAIN`/`SNOW` events every 5 seconds,
   which is used solely to provide the screen tint aesthetic (visual feedback).
