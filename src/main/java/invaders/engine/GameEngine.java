package invaders.engine;

import invaders.ConfigReader;
import invaders.Observer.GameObserver;
import invaders.Observer.GameSubject;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.configFactory.Config;
import invaders.entities.Player;
import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.memento.GameCaretaker;
import invaders.memento.GameStateMemento;
import invaders.rendering.Renderable;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine implements GameSubject {
	private List<GameObserver> observers = new ArrayList<>();
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();

	private List<Renderable> renderables =  new ArrayList<>();

	private Player player;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;

	private long startTime;

	private int time = 0;

	private int score = 0;


	private GameCaretaker caretaker;

	private boolean isGameOver = false; // 用于跟踪游戏结束状态的字段


	public GameEngine(Config config){
		// Read the config here
		ConfigReader.parse(config.getConfigPath());

		// Get game width and height
		gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		//Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);


		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}

		this.startTime = System.currentTimeMillis();

		this.caretaker = new GameCaretaker();

	}

	/**
	 * Updates the game/simulation
	 */
	public void update(){
		if (isGameOver) {
			return;
		}
		if (!player.isAlive() || getAllEnemies().isEmpty()) {
			isGameOver = true;
			notifyGameOver(); 
		}

		timer+=1;
		long currentTime = System.currentTimeMillis();
		time = (int) ((currentTime - startTime) / 1000);  // 将毫秒转换为秒
		notifyTimeChanged();

		movePlayer();

		for(GameObject go: gameObjects){
			go.update(this);
		}

		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i+1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);

				if((renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						||(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("Enemy"))||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))){
				}else{
					if(renderableA.isColliding(renderableB) && (renderableA.getHealth()>0 && renderableB.getHealth()>0)) {
						renderableA.takeDamage(1);
						renderableB.takeDamage(1);

						if (renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("PlayerProjectile")) {
							Enemy enemy = (Enemy) renderableA;
							if (enemy.getProjectileType().equals("fast")) {

								addScore(4);
							} else if (enemy.getProjectileType().equals("slow")){

								addScore(3);
							}
						}else if(renderableB.getRenderableObjectName().equals("Enemy") && renderableA.getRenderableObjectName().equals("PlayerProjectile")){
							Enemy enemy = (Enemy) renderableB;
							if (enemy.getProjectileType().equals("fast")) {

								addScore(4);
							} else if (enemy.getProjectileType().equals("slow")){

								addScore(3);
							}
						}else if(renderableB.getRenderableObjectName().equals("EnemyProjectile") && renderableA.getRenderableObjectName().equals("PlayerProjectile")){
							EnemyProjectile enemyProjectile = (EnemyProjectile) renderableB;
							if (enemyProjectile.GetType().equals("fast")) {

								addScore(2);
							} else if (enemyProjectile.GetType().equals("slow")){

								addScore(1);
							}
						}else if(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("PlayerProjectile")){
							EnemyProjectile enemyProjectile = (EnemyProjectile) renderableA;
							if (enemyProjectile.GetType().equals("fast")) {

								addScore(2);
							} else if (enemyProjectile.GetType().equals("slow")){

								addScore(1);
							}
						}

					}
				}

			}
		}


		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}

	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	public List<GameObject> getPendingToAddGameObject() {
		return pendingToAddGameObject;
	}

	public List<GameObject> getPendingToRemoveGameObject() {
		return pendingToRemoveGameObject;
	}

	public List<Renderable> getPendingToAddRenderable() {
		return pendingToAddRenderable;
	}

	public List<Renderable> getPendingToRemoveRenderable() {
		return pendingToRemoveRenderable;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		if(timer>45 && player.isAlive()){
			caretaker.save(this);
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			timer=0;
			return true;
		}
		return false;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}
	public void registerObserver(GameObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(GameObserver observer) {
		observers.remove(observer);
	}
	public void notifyTimeChanged() {
		for (GameObserver observer : observers) {
			observer.updateTime(time);
		}
	}

	public void notifyScoreChanged(int score) {
		for (GameObserver observer : observers) {
			observer.updateScore(score);
		}
	}

	public void addScore(int value) {
		this.score += value;
		notifyScoreChanged(this.score);
	}

	public GameStateMemento saveStateToMemento() {

		return new GameStateMemento(this.gameObjects, this.renderables);
	}

	public void restoreStateFromMemento(GameStateMemento memento) {
		this.gameObjects.clear();
		this.gameObjects.addAll(memento.getGameObjects());

		this.renderables.clear();
		this.renderables.addAll(memento.getRenderables());
	}

	public void undo() {

		caretaker.undo(this);
	}
	@Override
	public void notifyCheatPerformed() {
		for (GameObserver observer : observers) {
			observer.performCheat();
		}
	}

public void removeAllEnemy() {

	List<EnemyProjectile> enemiesToRemove = new ArrayList<>();
	for (GameObject go : gameObjects) {
		if (go instanceof EnemyProjectile) {
			EnemyProjectile enemy = (EnemyProjectile) go;
			if (enemy.GetType().equals("fast")) {
				enemiesToRemove.add(enemy);
				addScore(4);
			}
		}
	}
	gameObjects.removeAll(enemiesToRemove);
	renderables.removeAll(enemiesToRemove);


}

	private List<Enemy> getAllEnemies() {
		List<Enemy> enemies = new ArrayList<>();
		for (Renderable go : renderables) {
			if (go instanceof Enemy) {
				if  (go.isAlive()) {
					enemies.add((Enemy) go);
				}
			}
		}
		return enemies;
	}

	public void notifyGameOver() {
		for (GameObserver observer : observers) {
			observer.gameOver();
		}
	}


}
