package invaders.strategy;

import invaders.factory.Projectile;

public class SlowProjectileStrategy implements ProjectileStrategy{
    @Override
    public void update(Projectile p) {
        double newYPos = p.getPosition().getY() + 1;
        p.getPosition().setY(newYPos);
    }
    public String getType(){return "slow";}
}
