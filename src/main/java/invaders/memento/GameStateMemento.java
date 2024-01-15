package invaders.memento;

import java.util.ArrayList;
import java.util.List;

import invaders.engine.GameEngine;
import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;
import org.apache.commons.lang3.SerializationUtils;

public class GameStateMemento {
    private List<GameObject> gameObjects;
    private List<Renderable> renderables;


    public GameStateMemento(List<GameObject> gameObjects, List<Renderable> renderables) {
        this.gameObjects = new ArrayList<>();
        for (GameObject go : gameObjects) {
            this.gameObjects.add(go.clone());
        }

        this.renderables = new ArrayList<>();
        for (Renderable r : renderables) {
            this.renderables.add(r.clone());
        }

    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }


}
