package invaders.memento;

import invaders.engine.GameEngine;

public class GameCaretaker {
    private GameStateMemento gameStateMemento;

    public void save(GameEngine gameEngine) {
        gameStateMemento = gameEngine.saveStateToMemento();
    }

    public void undo(GameEngine gameEngine) {
        if (gameStateMemento != null) {

            gameEngine.restoreStateFromMemento(gameStateMemento);
        }
    }
}
