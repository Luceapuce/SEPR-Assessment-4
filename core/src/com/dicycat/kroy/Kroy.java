package com.dicycat.kroy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dicycat.kroy.screens.GameScreen;
import com.dicycat.kroy.screens.MenuScreen;
import com.dicycat.kroy.screens.MinigameScreen;

/**
 * Main game class
 * 
 * @author Riju De
 * @author Sam Hutchings
 *
 */

public class Kroy extends Game {
	public static final int width = 1080;
	public static final int height = 720;
	public static boolean debug = false;
	
	public static GameScreen mainGameScreen;
	public static MenuScreen mainMenuScreen;
	public static MinigameScreen mainMinigameScreen;
	public SpriteBatch batch;
	
	private Integer highScore;
	
	@Override
	public void create () {
		highScore = 0;		 
		batch = new SpriteBatch();
		mainMenuScreen = new MenuScreen(this);
		this.setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		// WARPING_FIX - START OF MODIFICATION  - NP STUDIOS - LUCY IVATT
		// The previous group limited the camera from reaching the edge of the map instead of fixing the warping issue.
		// Because of this, if you use the zoom out function when near the edge of the map the warping was still visible.
		// We added the two lines of code needed to clear the frame before the next frame is written which is what
		// was causing the issue.
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		// WARPING_FIX - END OF MODIFICATION  - NP STUDIOS
		super.render();
	}
	
	@Override
	public void dispose () {}
	
	/**
	 * Call to generate a brand new GameScreen which runs a new game
	 * @param truckNum  Selected truck
	 */
	public void newGame(int truckNum) {
		mainGameScreen = new GameScreen(this,truckNum);// Initialise new game
		setScreen(mainGameScreen);// Display new game
	}

	//MINIGAME_INTEGRATION_1 - START OF MODIFICATION - NPSTUDIOS - BETHANY GILMORE
	public void newMinigame(boolean flag) {
		mainMinigameScreen = new MinigameScreen(this, flag);// Initialise new minigame
		setScreen(mainMinigameScreen);// Display new minigame
	}
	//MINIGAME_INTEGRATION_1 - END OF MODIFICATION - NPSTUDIOS

	/**
	 * Return back to the menu screen
	 */
	public void backToMenu() {
		mainMenuScreen.state = MenuScreen.MenuScreenState.MAINMENU; // sets menu screen back to the original state
		mainMenuScreen.setCurrentlyRunningGame(false); //Tells the screen not to block any button pushes which would initialise a new game again
		setScreen(mainMenuScreen); // displays the menu screen
	}

	//MINIGAME_INTEGRATION_2 - START OF MODIFICATION - NPSTUDIOS - BETHANY GILMORE
	public void backToGame(){
		mainGameScreen.setGameState(GameScreen.GameScreenState.RUN);
		setScreen(mainGameScreen);
	}
	//MINIGAME_INTEGRATION_2 - END OF MODIFICATION - NPSTUDIOS
	
	/**
	 * Centre of the screen width
	 * @return centre of the screen width
	 */
	public static int CentreWidth() {
		return width / 3;
	}
	
	/** 
	 * Set the high score
	 * @param highScore The new high score
	 */
	public void setHighScore(Integer highScore) {
		this.highScore = highScore;
	}
	
	/**
	 * Get the current high score
	 * @return highScore
	 */
	public Integer getHighScore() {
		return highScore;
	}
}
