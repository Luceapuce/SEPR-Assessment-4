package com.dicycat.kroy.screens;
  
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dicycat.kroy.Kroy;
import com.dicycat.kroy.scenes.FireTruckSelectionScene;
import com.dicycat.kroy.scenes.OptionsWindow;
  
 /**
  * 
 * @author 
 *
 */
public class MenuScreen implements Screen{
  
  private Kroy game; 
  private OrthographicCamera gamecam;	//m
  private Viewport gameport; 	//m
  private Texture playBTN, playBTN_ACTIVE, optionsBTN, optionsBTN_ACTIVE, exitBTN, exitBTN_ACTIVE, minigameBTN, minigameBTN_ACTIVE, background;
  private Stage stage;
  
  private OptionsWindow optionsWindow;
  
  public static Music music = Gdx.audio.newMusic(Gdx.files.internal("gamemusic.mp3"));
  public static float musicVolume = 0.4f;

  //coordinates for Play and Exit buttons 
  private int BTN_WIDTH = 250;
  private int BTN_HEIGHT = 50;
  private int x_axis_centered = (Kroy.width/2) - (BTN_WIDTH/2);
  private int playBTN_y = (Kroy.height/2)+75;
  private int optionsBTN_y = (Kroy.height/2);
  private int minigameBTN_y = (Kroy.height/2)-75;
  private int exitBTN_y = (Kroy.height/2)-150;
  
  Pixmap pm = new Pixmap(Gdx.files.internal("handHD2.png")); //cursor
  int xHotSpot = pm.getWidth() / 3;	//where the cursor's aim is 
  int yHotSpot = 0;
  
  FireTruckSelectionScene fireTruckSelector;
  boolean currentlyRunningGame = false;

  /**
   *  Used to define the current state of the screen, 
   *  MAINMENU is used mostly but then TRUCKSELECT used when the "NewGame" button has been pressed  
   * 
   * @author 
   *
   */
  public static enum State {
	  MAINMENU,
	  TRUCKSELECT,
	  OPTIONS
  }
  
  public State state = State.MAINMENU;
  
  /**
   * @param game
   */
  public MenuScreen(Kroy game) { 
	  this.game = game; 
	  exitBTN = new Texture("exit.png"); 	//in later stages we could also have buttonActive and buttonInactive
	  exitBTN_ACTIVE = new Texture("exitActive.png");
	  optionsBTN = new Texture("options.png");
	  optionsBTN_ACTIVE = new Texture("optionsActive.png");
	  playBTN = new Texture("newgame.png");
	  playBTN_ACTIVE = new Texture("newActive.png");
	  minigameBTN = new Texture("minigame.png");
	  minigameBTN_ACTIVE = new Texture("minigameActive.png");
	  background = new Texture ("fireforce.png");
	  
	  gamecam = new OrthographicCamera();    //m
	  gameport = new FitViewport(Kroy.width, Kroy.height, gamecam);
	  stage = new Stage(gameport);
	  
	  fireTruckSelector = new FireTruckSelectionScene(game);
	  fireTruckSelector.visibility(false);
	  
	  music.play();
	  music.setLooping(true);  
	  music.setVolume((float)musicVolume);  
	  
	  optionsWindow = new OptionsWindow(game);
	  optionsWindow.visibility(false);
	  
  }
  
  @Override 
  public void show() {}
  
  /**
   *
   */
  @Override 
  public void render(float delta) { 
	  
	  switch(state) {
		  case MAINMENU:	// Display all buttons and the main menu		  
			  stage.act();	//allows the stage to interact with user input
			  
			  game.batch.setProjectionMatrix(gamecam.combined);
			  game.batch.begin();
			  
			  Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, xHotSpot, yHotSpot));
			  game.batch.draw(background, 0, 0);
			 
			  game.batch.draw(minigameBTN, x_axis_centered, minigameBTN_y, BTN_WIDTH, BTN_HEIGHT);
			
			
			  //for play button: checks if the position of the cursor is inside the coordinates of the button
			  if(( (Gdx.input.getX() < (x_axis_centered + BTN_WIDTH)) && (Gdx.input.getX() > x_axis_centered) ) && ( (Kroy.height - Gdx.input.getY() > playBTN_y ) && (Kroy.height - Gdx.input.getY() < (playBTN_y + BTN_HEIGHT)) ) ){
				  game.batch.draw(playBTN_ACTIVE, x_axis_centered, playBTN_y, BTN_WIDTH, BTN_HEIGHT);
				  if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					  this.dispose();
					  game.batch.end();
					  fireTruckSelector.visibility(true);// display the truck selection window
					  setGameState(State.TRUCKSELECT);// set the game state to run and run the selection screen code
					  return;
				  }
			  } else {
				  game.batch.draw(playBTN, x_axis_centered, playBTN_y, BTN_WIDTH, BTN_HEIGHT);
			  }
			  
			//for exit button
			  if(( (Gdx.input.getX() < (x_axis_centered + BTN_WIDTH)) && (Gdx.input.getX() > x_axis_centered) ) && ( (Kroy.height - Gdx.input.getY() > exitBTN_y ) && (Kroy.height - Gdx.input.getY() < (exitBTN_y + BTN_HEIGHT)) ) ){
				  game.batch.draw(exitBTN_ACTIVE, x_axis_centered, exitBTN_y, BTN_WIDTH, BTN_HEIGHT);
				  if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					  Gdx.app.exit();
				  }
			  } else {
				  game.batch.draw(exitBTN, x_axis_centered, exitBTN_y, BTN_WIDTH, BTN_HEIGHT);
			  }
				
			  //for minigame button
			  if(( (Gdx.input.getX() < (x_axis_centered + BTN_WIDTH)) && (Gdx.input.getX() > x_axis_centered) ) && ( (Kroy.height - Gdx.input.getY() > minigameBTN_y ) && (Kroy.height - Gdx.input.getY() < (minigameBTN_y + BTN_HEIGHT)) ) ){
				  game.batch.draw(minigameBTN_ACTIVE, x_axis_centered, minigameBTN_y, BTN_WIDTH, BTN_HEIGHT);
				  if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					  //what shall we put?
						  }
					  } else {
						  game.batch.draw(minigameBTN, x_axis_centered, minigameBTN_y, BTN_WIDTH, BTN_HEIGHT);
					  }
	
						  //for options button
			  if(( (Gdx.input.getX() < (x_axis_centered + BTN_WIDTH)) && (Gdx.input.getX() > x_axis_centered) ) && ( (Kroy.height - Gdx.input.getY() > optionsBTN_y ) && (Kroy.height - Gdx.input.getY() < (optionsBTN_y + BTN_HEIGHT)) ) ){
				  game.batch.draw(optionsBTN_ACTIVE, x_axis_centered, optionsBTN_y, BTN_WIDTH, BTN_HEIGHT);
				  if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
					  //game.batch.end();
					  optionsWindow.visibility(true);
					  setGameState(State.OPTIONS);
				  }
			  } else {
				  game.batch.draw(optionsBTN, x_axis_centered, optionsBTN_y, BTN_WIDTH, BTN_HEIGHT);
			  }
			  game.batch.end();
				  
			  break;
		  case TRUCKSELECT: // Ran when the new game button pressed
			  Gdx.input.setInputProcessor(fireTruckSelector.stage);
			  fireTruckSelector.stage.act();
			  fireTruckSelector.stage.draw();
			  clickCheck();//Checks for any button presses
			  break;
		  case OPTIONS:
			  Gdx.input.setInputProcessor(optionsWindow.stage);
			  optionsWindow.stage.act();
			  optionsWindow.stage.draw();
			  optionsWindow.clickCheck(true);
			  break;
		  }
  	}
  
	/**
	 * @param s
	 */
	public void setGameState(State s){
	    this.state = s;
	}
  
	/**
	 * Checks if any of the buttons have been pressed
	 */
	public void clickCheck() {
		//Truck 1 Selected
		fireTruckSelector.truckButton1.addListener(new ClickListener() {
			@Override
	    	public void clicked(InputEvent event, float x, float y) {
				startGame(0);//Game begun with 0 (Speed) as the truck selected
	    	}
	    });
		//Truck 2 Selected
		fireTruckSelector.truckButton2.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		startGame(1);//Game begun with 1 (Damage) as the truck selected
	    	}
	    });
		//Truck 3 Selected
		fireTruckSelector.truckButton3.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		startGame(2);//Game begun with 2 (Capacity) as the truck selected
	    	}
	    });
		//Truck 4 Selected
		fireTruckSelector.truckButton4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startGame(3);//Game begun with 3 (Range) as the truck selected
				
			}
	    });
	}


	/**
	 * 
	 * @param truckNum Type of truck selected
 	 */
	public void startGame(int truckNum) {
		 if (!currentlyRunningGame) {	// Checks if a new GameScren is currently running and either makes one or ignores the commands
			 currentlyRunningGame = true; // Makes sure that only one GameScreen is opened at once
			 game.newGame(truckNum); // Calls the function in Kroy to start a new game
		 }
	} 
  
  
  /**
   * @param state
   */
  public void setCurrentlyRunningGame(boolean state) {
	  currentlyRunningGame = state;
  }
  
  /**
   *
   */
  @Override 
  public void resize(int width, int height) {
	  gameport.update(width, height);
  }
  
  @Override 
  public void pause() {}
  
  @Override 
  public void resume() {}
  
  @Override 
  public void hide() {}
  
  @Override 
  public void dispose() {}
 }
 