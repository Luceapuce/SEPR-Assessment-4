package com.dicycat.kroy.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dicycat.kroy.GameObject;
import com.dicycat.kroy.GameTextures;
import com.dicycat.kroy.Kroy;
import com.dicycat.kroy.debug.DebugCircle;
import com.dicycat.kroy.debug.DebugDraw;
import com.dicycat.kroy.debug.DebugLine;
import com.dicycat.kroy.debug.DebugRect;
import com.dicycat.kroy.entities.FireTruck;
import com.dicycat.kroy.entities.UFO;
import com.dicycat.kroy.gamemap.TiledGameMap;
import com.dicycat.kroy.scenes.HUD;
import com.dicycat.kroy.scenes.PauseWindow;


public class GameScreen implements Screen{

	public static GameScreen mainGameScreen;
	public GameTextures textures;

	Boolean showDebug = true;

	Kroy game;
	private OrthographicCamera gamecam;	//m 	//follows along what the port displays
	private Viewport gameport; 	//m
	private HUD hud;	//m
	public static boolean FOLLOWCAMERA = true;
	private PauseWindow pauseWindow;
	public static TiledGameMap gameMap;

	FireTruck player; //Reference to the player
	List<GameObject> gameObjects;	//List of active game objects
	List<GameObject> toAdd;
	List<DebugDraw> debugObjects; //List of debug items

	public static enum State{
		PAUSE,
		RUN,
		RESUME
	}

	public float gameTimer; //Timer to destroy station

	public GameScreen(Kroy _game) {
		game = _game;
		gamecam = new OrthographicCamera();    //m
		gameport = new FitViewport(Kroy.width, Kroy.height, gamecam);	//m //Mic:could also use StretchViewPort to make the screen stretch instead of adapt
		hud = new HUD(game.batch, this.game);
		gameMap = new TiledGameMap();											//or FitPort to make it fit into a specific width/height ratio

		pauseWindow = new PauseWindow();
		pauseWindow.visibility(false);
		textures = new GameTextures();
		gameTimer = 60 * 15; //Set timer to 15 minutes
		if (mainGameScreen == null) {
			mainGameScreen = this;
		}
		else {
			System.err.println("Duplicate GameScreens");
		}

	}

	@Override
	public void show() {	//Screen first shown
		toAdd = new ArrayList<GameObject>();
		gameObjects = new ArrayList<GameObject>();
		debugObjects = new ArrayList<DebugDraw>();
		player = new FireTruck(new Vector2(1530, 1300));
		gamecam.translate(new Vector2(player.getX(),player.getY()));// sets initial Camera position
		gameObjects.add(player);	//Player	//Mic:modified from (100, 100) to (0, 0)
		gameObjects.add(new UFO(new Vector2(1600, 1200)));	//UFO	//Mic:modified from (480,580) to (0, 200)
		//gameObjects.add(new Bullet(this, new Vector2(10, 10), new Vector2(1,5), 50, 500));	//Bullet

	}

	//@Override

	public static State state = State.RUN;

	public void render(float delta) {		//Called every frame

		Gdx.input.setInputProcessor(pauseWindow.stage);
		pauseWindow.stage.act();

		switch (state) {
		case RUN:
		if (Gdx.input.isKeyPressed(Keys.P) || Gdx.input.isKeyPressed(Keys.O) || Gdx.input.isKeyPressed(Keys.M)|| Gdx.input.isKeyPressed(Keys.ESCAPE)){
			pauseWindow.visibility(true);
			pause();
		}
		gameMap.renderRoads(gamecam);


		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		game.batch.setProjectionMatrix(gamecam.combined);	//Mic:only renders the part of the map where the camera is
		game.batch.begin(); // Game loop Start

		gameTimer -= delta;
		if (gameTimer <= 0) {
			//Destroy station
			System.err.println("Timer!");	//Temp test
		}
		
		hud.update(delta);

		UpdateLoop();	//Update all game objects

		game.batch.end();

		gameMap.renderBuildings(gamecam);


		hud.stage.draw();
		pauseWindow.stage.draw();

		//DrawDebug(); //Draw all debug items as they have to be drawn outside the batch

		System.out.println("Render calls:" + game.batch.renderCalls + " | FPS:" + Gdx.graphics.getFramesPerSecond());

		if (showDebug) {
			DrawDebug(); //Draw all debug items as they have to be drawn outside the batch
		}

		break;
		case PAUSE:
			pauseWindow.stage.draw();
			clickCheck();
			break;
		case RESUME:
			pauseWindow.visibility(false);
			setGameState(State.RUN);
			break;
		}


	}

	//region Game Logic
	private void UpdateLoop() {
		List<GameObject> toRemove = new ArrayList<GameObject>();
		for (GameObject gObject : gameObjects) {	//Go through every game object
			gObject.Update();							//Update the game object
			if (gObject.CheckRemove()) {				//Check if game object is to be removed
				toRemove.add(gObject);					//Set it to be removed
			}else {
				gObject.Render(game.batch);
			}
		}
		for (GameObject rObject : toRemove) {	//Remove game objects set for removal
			gameObjects.remove(rObject);
		}
		for (GameObject aObject : toAdd) {		//Add game objects to be added
			gameObjects.add(aObject);
		}
		toAdd.clear();
	}

	public void AddGameObject(GameObject gameObject) {	//Add a game object next frame
		toAdd.add(gameObject);
	}

	public FireTruck GetPlayer() {
		return player;
	}

	private void DrawDebug() {		//Draws all debug objects for one frame
		for (DebugDraw dObject : debugObjects) {
			dObject.Draw(gamecam.combined);
		}
		debugObjects.clear();
	}

	public void DrawLine(Vector2 start, Vector2 end, int lineWidth, Color colour) {
		debugObjects.add(new DebugLine(start, end, lineWidth, colour));
	}

	public void DrawCircle(Vector2 position, float radius, int lineWidth, Color colour) {
		debugObjects.add(new DebugCircle(position, radius, lineWidth, colour));
	}



	public void DrawRect(Vector2 bottomLeft, Vector2 dimensions, int lineWidth, Color colour) {
		debugObjects.add(new DebugRect(bottomLeft, dimensions, lineWidth, colour));
	}

	public void updateCamera() {// updates the position of the camera to have the truck centre
		gamecam.position.lerp(new Vector3(player.getX(),player.getY(),gamecam.position.z),0.1f);
		gamecam.update();
	}



	//public void DrawRect(Vector2 centre, Vector2 dimensions, int lineWidth, Color colour) {
	//	debugObjects.add(new DebugRect(centre, dimensions, lineWidth, colour));
	//}


	@Override
	public void resize(int width, int height) {
		gameport.update(width, height);				//m
	}

	@Override
	public void pause() {
		setGameState(State.PAUSE);

	}

	@Override
	public void resume() {
		setGameState(State.RESUME);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		mainGameScreen = null;
	}

	public void setGameState(State s){
	    GameScreen.state = s;
	}

	public void clickCheck() {
		//resume button
		pauseWindow.resume.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		pauseWindow.visibility(false);
				resume();
	    	}
	    });

		//exit button
		pauseWindow.exit.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		Gdx.app.exit();
	    	}
	    });
		//menu button
			pauseWindow.menu.addListener(new ClickListener() {
		    	@Override
		    	public void clicked(InputEvent event, float x, float y) {
		    		dispose();
		    		game.setScreen(new MenuScreen(game));
		    		return;
		    		}
		    });
	}
	
}
