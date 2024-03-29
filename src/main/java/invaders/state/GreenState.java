package invaders.state;

import invaders.gameobject.Bunker;
import javafx.scene.image.Image;
import java.io.File;

public class GreenState implements BunkerState {
    private Bunker bunker;

    public GreenState(Bunker bunker){
        this.bunker = bunker;
    }

    @Override
    public void takeDamage() {
        bunker.setImage(new Image(new File("src/main/resources/bunkerYellow.png").toURI().toString()));
        bunker.setState(new YellowState(bunker));
    }

    @Override
    public GreenState clone() {
        try {
            return (GreenState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // 不应该发生
        }
    }
}
