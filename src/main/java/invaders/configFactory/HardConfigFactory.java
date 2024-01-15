package invaders.configFactory;

public class HardConfigFactory implements ConfigFactory{
    @Override
    public Config createConfig() {
        return new HardConfig();
    }
    public String getConfigType(){
        return "hard";
    }

}