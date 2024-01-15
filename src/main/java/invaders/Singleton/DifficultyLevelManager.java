package invaders.Singleton;
import invaders.configFactory.Config;

public class DifficultyLevelManager {
    private static DifficultyLevelManager instance;
    private Config selectedConfig;

    private DifficultyLevelManager() {
    }

    public static DifficultyLevelManager getInstance() {
        if (instance == null) {
            instance = new DifficultyLevelManager();
        }
        return instance;
    }

    public void setDifficulty(Config config) {
        selectedConfig = config;
    }

    public Config getDifficulty() {
        return selectedConfig;
    }
}