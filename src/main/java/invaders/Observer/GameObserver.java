package invaders.Observer;

public interface GameObserver {
    void updateTime(int time);
    void updateScore(int score);
    void performCheat();
    void gameOver(); // 添加游戏结束时的回调方法
}
