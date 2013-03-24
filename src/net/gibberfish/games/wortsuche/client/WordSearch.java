package net.gibberfish.games.wortsuche.client;

import java.util.ArrayList;
import java.util.Arrays;

import net.gibberfish.games.wortsuche.client.Analytics;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WordSearch implements EntryPoint {
	
	//How much words to place on the field
	int WordCountToShow;
	int WordsFound = 0;
	
	private String JSON_URL;

	Canvas canvas;
	Context2d ctx;

	static final int AppHeight = 600;
	static final int AppWidth = 800;
	
	static final int fieldHeight = 500;
	static final int fieldWidth = 500;

	int fieldRows = 10;
	int fieldColums = 10;

	float tileWidth;
	float tileHeight;
	
	boolean isDragging = false;
	TileFieldObject tfoDragStart, tfoDragStop = null;
	private HandlerRegistration mouseMove;
	
	ArrayList<TileFieldObject> dragPath = new ArrayList<TileFieldObject>();
	
	String randomChars = "abcdefghijklmnopqrstuvwxyz";
	ArrayList<String> randomColors = new ArrayList<String>(Arrays.asList("#00FF00","#8ED6FF","#FF99FF"));
	
	ArrayList<MenueItem> menueItems = new ArrayList<MenueItem>();
 	
	WordData d; 

	TileField tField;

	TileWords[] words; 

	Timer loopTimer, loadTimer;

	ArrayList<Drawable> FieldDrawStack = new ArrayList<Drawable>();
	ArrayList<MenueDrawable> menueDrawStack = new ArrayList<MenueDrawable>();

	
	SplashScreen splash;
	EndScreen endScreen;
	
	SoundController soundController;
	Sound soundFound, soundWrong, soundClick, soundSelect, soundBeep;
	
	Image bgImg;

	static final String holderId = "canvasholder";
	boolean bShowEndScreen,bReset = false;
	
	CountDownClock cdClock = new CountDownClock(this);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {		
		Analytics.gaEventTracker("WordSearch", "AppStart", "onModuleLoad");
		Analytics.gaPageTracker("Wortsuche.html");
		
		splash = new SplashScreen(this, AppHeight, AppWidth);
		endScreen = new EndScreen(this, AppHeight, AppWidth);
		splash.show();
		bReset = true;
		initializeSounds();
		initializeMainCanvas();
	}

	private void initializeSounds() {
		soundController = new SoundController();
		soundFound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_PCM,
	        "./sounds/found.wav");	
		soundWrong = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_PCM,
		        "./sounds/wrong.wav");	
		soundSelect = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_PCM,
		        "./sounds/select.wav");	
		soundBeep = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_PCM,
		        "./sounds/beep.wav");	
		soundClick = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG_MP3,
		        "./sounds/click.mp3");	
	}

	public void reset(){
		WordsFound = 0;
		bShowEndScreen = false;
		menueItems.clear();
		menueDrawStack.clear();
		bReset = true;
	}
	
	private void initializeMainCanvas() {
		canvas = Canvas.createIfSupported();
		canvas.setVisible(false);
		if (canvas == null) {
			RootPanel
					.get(holderId)
					.add(new Label(
							"Sorry, your browser doesn't support the HTML5 Canvas element"));
			return;
		}
		canvas.setStyleName("mainCanvas");
		canvas.setWidth(AppWidth + "px");
		canvas.setCoordinateSpaceWidth(AppWidth);

		canvas.setHeight(AppHeight + "px");
		canvas.setCoordinateSpaceHeight(AppHeight);

		RootPanel.get(holderId).add(canvas);
		ctx = canvas.getContext2d();
		
		canvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
			   
			   tfoDragStart = tField.findTileFieldObject(event.getX(),event.getY());
			   tfoDragStop = tfoDragStart;
			   if (tfoDragStart != null){
				   isDragging = true;
				   mouseMove = canvas.addMouseMoveHandler(new MouseMoveHandler(){
			            public void onMouseMove(MouseMoveEvent e) {
			       			tfoDragStop = tField.findTileFieldObject(e.getX(),e.getY());
			            	if (tfoDragStop == null){
			            		tfoDragStop = tfoDragStart;
			            	}
			            }
			      });
			   }
			}
		});

		canvas.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				mouseMove.removeHandler();
				isDragging = false;	
				hitCheck();
			}

		});		
	}

	private void hitCheck() {
		// TODO Auto-generated method stub
		WordObject wo = d.isWord(dragPath);
		if (wo != null){
			//only delete dragpath and dont clear it if the word is already found before
			if (wo.isFound()){
				dragPath.clear();
			}else{
				//hit
				soundFound.play();
				wo.setFound(true);
				WordsFound++;
				for (MenueItem m : menueItems){
					if (m.isText(wo.getOriginal())){
						m.isFound(true);
						break;
					}
				}
				for (TileFieldObject t : dragPath){
					t.setPersisted(true);
				}
				dragPath.clear();
				checkWon();
			}
		}else{
			soundWrong.play();
			clearPath();
		}
		
	}
	
	private void checkWon() {
		if (WordsFound == WordCountToShow){
			bShowEndScreen = true;
			canvas.setVisible(false);
			endScreen.setMessage("Won!");
			Analytics.gaEventTracker("WordSearch", "LevelEnd", "Won");
			endScreen.show();
		}
	}
	
	public void timeOver(){
		bShowEndScreen = true;
		canvas.setVisible(false);
		endScreen.setMessage("Not all words found!");
		Analytics.gaEventTracker("WordSearch", "LevelEnd", "TimeOver");
		endScreen.show();
	}

	public void finalizeIinitialize(WordData d){
		
		loadTimer.cancel();
		loadTimer = null;
		
		int i = 0;
		while (i < WordCountToShow){
			WordObject wo = d.getRandomWord();
			if (searchPlaceForWord(wo)){
				wo.setSet(true);
				MenueItem m = new MenueItem(wo.getOriginal(), i+1);
				menueItems.add(m);
				menueDrawStack.add(m);
				i++;
			}
		}
		
		fillUpEmptyTiles();
		
		splash.loadingFinished();
		cdClock.start();
	}
	
	public void start(int level){
		
		switch (level) {
		case 1:
			//regular
			WordCountToShow = 10;
			fieldColums = 18;
			fieldRows = 18;
			JSON_URL = "model/words_regular.json";
			cdClock.setTime(360);
			Analytics.gaEventTracker("WordSearch", "LevelStart", "regular");
			break;
		case 2:
			//hard
			WordCountToShow = 15;
			fieldColums = 24;
			fieldRows = 24;
			JSON_URL = "model/words_hard.json";
			cdClock.setTime(360);
			Analytics.gaEventTracker("WordSearch", "LevelStart", "hard");
			break;
			
		default:
			//easy
			WordCountToShow = 5;
			fieldColums = 10;
			fieldRows = 10;
			JSON_URL = "model/words_easy.json";
			Analytics.gaEventTracker("WordSearch", "LevelStart", "easy");
			cdClock.setTime(360);
			break;
		}
		
		tileWidth = fieldWidth / fieldColums;
		tileHeight = fieldHeight / fieldRows;
		
		initialize();
		
		canvas.setVisible(true);
		splash.hide();

		loopTimer = new Timer() {
			@Override
			public void run() {
				loop();
			}
		};
		loopTimer.scheduleRepeating(10);
	}
	
	public void initialize() {		
		
		tField = new TileField(15, 35, fieldRows, fieldColums, tileWidth, tileHeight, FieldDrawStack);
		menueDrawStack.add(new MenueHeader("Hidden words:"));

		d = new WordData();
		d.loadDataFile(JSON_URL);
		loadTimer = new Timer() {
			@Override
			public void run() {
				if (d.isReady)
				{
					finalizeIinitialize(d);
				}
			}
		};
		loadTimer.scheduleRepeating(100);
		
	}
	
	private boolean searchPlaceForWord(WordObject wo) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		for (int x = 0; x < fieldRows; x++){
			for (int y = 0; y < fieldColums; y++){
				int rand = Random.nextInt(2);
				if ( checkIfWordFits(wo.getTranslation(), x, y, rand)){
					positions.add(new int[]{x,y,rand});
				}
				else if ( checkIfWordFits(wo.getTranslation(), x, y, rand+1)){
					positions.add(new int[]{x,y,rand+1});
					
				}
			}
		}
		//get random position from possible locations
		if (!positions.isEmpty()){
			int[] i = positions.get(Random.nextInt(positions.size()));
			placeWord(wo, wo.getTranslation(), i[0], i[1], i[2]);
			return true;
		}else{
			return false;
		}
	}
	

	private void placeWord(WordObject wo, String translation, int x, int y, int d) {
		
		int xNew, yNew;
		if (d == 1){
			xNew = x + 1;
			yNew = y;
		}
		else
		{
			xNew = x;
			yNew = y + 1;
		}
		
		if (translation.length() > 1){
			placeWord(wo, translation.substring(1), xNew, yNew, d);	
		}		
		tField.getTileFieldObject(x, y).setCharacterToDraw(translation.substring(0,1));
		wo.addField(x,y);

	}
	

	private boolean checkIfWordFits(String translation, int x, int y, int d) {
		
		int xNew, yNew;
		if (d == 1){
			xNew = x + 1;
			yNew = y;
		}
		else
		{
			xNew = x;
			yNew = y + 1;
		}
		if (translation.length() > 1){
			if (!checkIfWordFits(translation.substring(1), xNew, yNew, d)){
				return false;
			}
		}
		if (fieldRows-1 >= x && fieldColums-1 >= y){
			if ( tField.getTileFieldObject(x, y).getCharacterToDraw() == null || tField.getTileFieldObject(x, y).getCharacterToDraw().equalsIgnoreCase(translation.substring(0, 1)) ){
				return true;
			}
		}

		return false;

	}

	private void fillUpEmptyTiles() {
		for (int x = 0; x < fieldRows; x++){
			for (int y = 0; y < fieldColums; y++){
				if (tField.getTileFieldObject(x, y).getCharacterToDraw() == null){
					tField.getTileFieldObject(x, y).setCharacterToDraw("" + randomChars.charAt(Random.nextInt(randomChars.length())));
				}
			}
		}	
	}
	
	public void draw() {
		if (bReset){
			ctx.clearRect(0, 0, AppWidth, AppHeight);
			//ctx.setStrokeStyle("#A0522D");
			//ctx.setLineWidth(10);
			//ctx.strokeRect(0,0, AppWidth, AppWidth);
			bReset = false;
		}
		if (!bShowEndScreen){
			for (Drawable obj : FieldDrawStack) {
				if (obj.bDraw){
					obj.draw(ctx);
				}
			}
			for (MenueDrawable obj : menueDrawStack) {
				if (obj.bDraw){
					obj.draw(ctx);
				}
			}
			if (cdClock.bDraw){
				cdClock.draw(ctx);
			}
		}
	}
	
	public void loop() {
		if (isDragging){
			updateDraggPath();
		}
		draw();
	}

	private void updateDraggPath() {
		clearPath();
		//calculate new Path
		dragPath.add(tfoDragStart);
		dragPath.add(tfoDragStop);		
		if (tfoDragStop.getiRow() == tfoDragStart.getiRow() || tfoDragStart.getiColmn() == tfoDragStop.getiColmn()){
			//dont allow cross words, not right now :)
			
			if (tfoDragStop.getiRow() == tfoDragStart.getiRow()){
				int i = (tfoDragStart.getiColmn() > tfoDragStop.getiColmn()) ? tfoDragStop.getiColmn() : tfoDragStart.getiColmn();
				int j = (tfoDragStart.getiColmn() < tfoDragStop.getiColmn()) ? tfoDragStop.getiColmn() : tfoDragStart.getiColmn();
				i++;
				while (i < j){
					dragPath.add(tField.getTileFieldObject(tfoDragStop.getiRow(), i));
					i++;
				}
			}
			else{
				int i = (tfoDragStart.getiRow() > tfoDragStop.getiRow()) ? tfoDragStop.getiRow() : tfoDragStart.getiRow();
				int j = (tfoDragStart.getiRow() < tfoDragStop.getiRow()) ? tfoDragStop.getiRow() : tfoDragStart.getiRow();
				i++;
				while (i < j){
					dragPath.add(tField.getTileFieldObject(i, tfoDragStop.getiColmn()));
					i++;
				}
			}
		}
		//mark new path
		for (TileFieldObject o : dragPath){
			o.drawCircle(true);
		}
		
	}

	private void clearPath() {
		if (!dragPath.isEmpty()){
			for (TileFieldObject o : dragPath){
				o.drawCircle(false);
			}
			dragPath.clear();
		}
	}
}

	