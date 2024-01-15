package invaders.configFactory;


public class MediumConfigFactory implements ConfigFactory{
    @Override
    public Config createConfig() {
        return new MediumConfig();
    }
    public String getConfigType(){
        return "medium";
    }

}
