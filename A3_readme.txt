
How to run the code:
gradle build

To run the project, run:
gradle run

Implemented functions:
1. Select difficulty level
2. Calculate time and scores
3 Undo function
4. Cheat function: Eliminate all rapid projectiles

Design Patterns Implemented：
1. Factory pattern + singleton pattern
  `Config` is an abstract class with an abstract method `getConfigPath`.
`EasyConfig`, `MediumConfig`, and `HardConfig` are concrete subclasses of `Config`, which respectively provide implementation of `getConfigPath`.
`ConfigFactory` is an interface with `createConfig` and `getConfigType` methods.
`EasyConfigFactory`, `MediumConfigFactory`, and `HardConfigFactory` implement the `ConfigFactory` interface.
`DifficultyLevelManager` uses the singleton pattern and has a property of type `Config`.
The `App` class contains the main game logic, which uses `ConfigFactory` and `DifficultyLevelManager`.


2. Observer pattern
GameEngine: Acts as a Subject, which contains the main logic of the game and notifies observers when the game state is updated.
GameWindow: Acts as an Observer, which displays game status and responds to changes in status.
A GameSubject interface
A GameObserver interface

3.Memo mode
The GameCaretaker class is responsible for saving and restoring game state.
The GameStateMemento class contains deep copies of game objects and render objects so that the game can be restored to a previous state.
In the GameEngine class, the saveStateToMemento method is used to create and save the current state of the memo object, and restoreStateFromMemento is used to restore the state saved in the memo.
In the shootPressed method, each shot saves the game state so the user can undo the shot.


How to choose a difficulty level:
Before starting the game, select the difficulty level by clicking the button with your mouse.

How to undo:
Click the "undo" button.

How to cheat in code:
Click the "RemoveAllSameStrategyProjectile" button


other:
Undo functionality is currently not fully implemented and the existing image remains on the screen when undo occurs