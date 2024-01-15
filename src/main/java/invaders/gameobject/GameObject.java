package invaders.gameobject;

import invaders.engine.GameEngine;
import java.io.Serializable;

// contains basic methods that all GameObjects must implement
public interface GameObject extends Cloneable{

    public void start();
    public void update(GameEngine model);
    public GameObject clone();

    public String getObjectName();

}
