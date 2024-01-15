package invaders.engine;

import java.util.List;
import java.util.ArrayList;


import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;

import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import invaders.Observer.GameObserver;
import javafx.scene.control.Button;

public class GameWindow implements GameObserver{
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    // private static final double VIEWPORT_MARGIN = 280.0;

    private int minute;
    private int second;
    private int lives;
    private int score;

    private Text timeLabel;
    private Text scoreLabel;

    private Button undoButton;
    private Button removeAllEnemysButton;



	public GameWindow(GameEngine model){
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();
        model.registerObserver(this);

        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        timeLabel = new Text(10, 20, "");
        scoreLabel = new Text(10, 40, "Score: 0");
        timeLabel.setFill(Color.WHITE);
        scoreLabel.setFill(Color.WHITE);
        undoButton = new Button("Undo");
        undoButton.setFocusTraversable(false);
        undoButton.setLayoutX(10);
        undoButton.setLayoutY(height - 30);
        undoButton.setOnAction(event -> handleUndoButton());

        removeAllEnemysButton = new Button("RemoveAllSameStrategyProjectile");
        removeAllEnemysButton.setFocusTraversable(false);
        removeAllEnemysButton.setLayoutX(10);
        removeAllEnemysButton.setLayoutY(height - 60);
        removeAllEnemysButton.setOnAction(event -> handleRemoveAllEnemyButton());



        pane.getChildren().addAll(timeLabel,scoreLabel,undoButton,removeAllEnemysButton);
        timeLabel.toFront();
        scoreLabel.toFront();
        undoButton.toFront();

    }

	public void run() {
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
    }


    private void draw(){
        model.update();

        List<Renderable> renderables = model.getRenderables();


        for (int i = entityViews.size() - 1; i >= 0; i--) {
            boolean find=false;
            for (Renderable renderable : renderables) {
                if(entityViews.get(i).matchesEntity(renderable)){
                    find=true;
                }
            }
            if(!find){
                pane.getChildren().remove(entityViews.get(i).getNode());
                entityViews.remove(i);
            }
        }



        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (Renderable entity : renderables){
            if (!entity.isAlive()){
                for (EntityView entityView : entityViews){
                    if (entityView.matchesEntity(entity)){
                        entityView.markForDelete();
                    }
                }
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }


        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);

    }

	public Scene getScene() {
        return scene;
    }

    public void splitTime(int time) {
        int minutes = time/ 60;
        int seconds = time % 60;
        minute = minutes;
        second = seconds;
    }
    public void updateTime(int time) {
        splitTime(time);
        String timeText = "Time: " + String.format("%02d:%02d", minute, second);
        timeLabel.setText(timeText);
    }
    public void updateScore(int score) {
        String scoreText =  "Score: " + score ;
        scoreLabel.setText(scoreText);

    }

    private void handleUndoButton() {
        model.undo();
    }

    private void handleRemoveAllEnemyButton() {
        model.notifyCheatPerformed(); // 调用 GameEngine 中的方法来移除所有外星人抛射物
    }


    @Override
    public void performCheat() {

            model.removeAllEnemy();
    }

    public void gameOver() {

        System.out.println("Game Over!");

    }






}
