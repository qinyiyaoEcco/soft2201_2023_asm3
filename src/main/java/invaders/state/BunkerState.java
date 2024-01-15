package invaders.state;
public interface BunkerState extends Cloneable{
    public void takeDamage();
    public BunkerState clone();

}
