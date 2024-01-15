package invaders.configFactory;


public class EasyConfigFactory implements ConfigFactory{
    @Override
    public Config createConfig() {
        return new EasyConfig();
    }
    public String getConfigType(){
        return "easy";
    }

}
