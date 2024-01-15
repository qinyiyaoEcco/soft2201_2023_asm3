package invaders.factory;

import invaders.gameobject.GameObject;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public abstract class Projectile implements Renderable, GameObject {
    private int lives = 1;
    private Vector2D position;
    private final Image image;

    public Projectile(Vector2D position, Image image) {
        this.position = position;
        this.image = image;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void start() {}

    @Override
    public double getWidth() {
        return 10;
    }

    @Override
    public double getHeight() {
        return 10;
    }

    @Override
    public void takeDamage(double amount) {
        this.lives-=1;
    }

    @Override
    public double getHealth() {
        return this.lives;
    }

    @Override
    public boolean isAlive() {
        return this.lives>0;
    }

    @Override
    public Projectile clone() {
        try {
            Projectile clonedProjectile = (Projectile) super.clone();
            clonedProjectile.position = this.position.clone(); // 假设Vector2D有clone方法
            return clonedProjectile;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // 不应该发生
        }
    }


}
