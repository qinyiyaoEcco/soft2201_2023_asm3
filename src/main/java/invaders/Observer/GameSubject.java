package invaders.Observer;

public interface GameSubject {
    void registerObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyTimeChanged();
    void notifyScoreChanged(int score);

    void notifyCheatPerformed();
    void notifyGameOver();
}
