package invaders.entities;

import invaders.rendering.Renderable;
import javafx.scene.Node;
//import inv/au/courses/12969/lessons/aders.rendering.Renderable;

public interface EntityView {
    void update(double xViewportOffset, double yViewportOffset);

    boolean matchesEntity(Renderable entity);

    void markForDelete();

    Node getNode();

    boolean isMarkedForDelete();
}
