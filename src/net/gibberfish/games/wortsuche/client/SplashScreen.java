package net.gibberfish.games.wortsuche.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class SplashScreen {
	
	Canvas canvasSplash;
	Context2d ctxSplash;
	
	int canvasHeight = WordSearch.AppHeight;
	int canvasWidth = WordSearch.AppWidth;
	
	BoardButton btnEasy, btnHard, btnMiddle;
	WordSearch parent;
	
	public SplashScreen(WordSearch wortsuche, int height, int width){
		
		parent = wortsuche;
		canvasHeight = height;
		canvasWidth = width;
		
		canvasSplash = Canvas.createIfSupported();
		canvasSplash.setVisible(true);
		if (canvasSplash == null) {
			RootPanel
					.get(WordSearch.holderId)
					.add(new Label(
							"Sorry, your browser doesn't support the HTML5 Canvas element"));
			return;
		}
		canvasSplash.setStyleName("splashCanvas");
		canvasSplash.setWidth(canvasWidth+"px");
		canvasSplash.setCoordinateSpaceWidth(canvasWidth);

		canvasSplash.setHeight(canvasHeight+"px");
		canvasSplash.setCoordinateSpaceHeight(canvasHeight);

		RootPanel.get(WordSearch.holderId).add(canvasSplash);
		ctxSplash = canvasSplash.getContext2d();
		
		btnEasy = new BoardButton(450, 250, 240, 60, "beginner");
		btnMiddle = new BoardButton(450, 330, 240, 60, "amateur");
		btnHard = new BoardButton(450, 410, 240, 60, "professional");
		
		canvasSplash.addClickHandler(new ClickHandler() {
        	@Override
			public void onClick(ClickEvent event) {

	            checkButtonClicked(event);	
			}
		});   
		
		canvasSplash.addMouseMoveHandler(new MouseMoveHandler(){
            public void onMouseMove(MouseMoveEvent e) {
            	checkMouseOver(e.getX(),e.getY());
            }
		});

	}
	

	private void checkMouseOver(int x, int y) {
        if (btnEasy.checkMouseOver(x, y)){
        	if (!btnEasy.isMouseOver()){
        		btnEasy.setMouseOver(true);
        		parent.soundSelect.play();
        	}
        }else{
        	btnEasy.setMouseOver(false);
        }

        if (btnMiddle.checkMouseOver(x, y)){
        	if (!btnMiddle.isMouseOver()){
        		btnMiddle.setMouseOver(true);
        		parent.soundSelect.play();
        	}
        }else{
        	btnMiddle.setMouseOver(false);
        }
        
        if (btnHard.checkMouseOver(x, y)){
        	if (!btnHard.isMouseOver()){
        		btnHard.setMouseOver(true);
        		parent.soundSelect.play();
        	}
        }else{
        	btnHard.setMouseOver(false);
        }
        
        draw();
	}
	
	public void draw(){
		ctxSplash.clearRect(0,0, canvasWidth, canvasHeight);

		//ctxSplash.setStrokeStyle("#A0522D");
		//ctxSplash.setLineWidth(10);
		//ctxSplash.strokeRect(0,0, canvasWidth, canvasHeight);

		
		ctxSplash.setTextAlign("left");
		ctxSplash.setTextBaseline("middle");
		ctxSplash.setFont("bold 40pt Comic Sans MS"); 
		ctxSplash.setShadowColor("rgb(190, 190, 190)");
		ctxSplash.setShadowOffsetX(5);
		ctxSplash.setShadowOffsetY(5);
		ctxSplash.setShadowBlur(10);
		ctxSplash.setFillStyle("rgb(255,255,255)");
		ctxSplash.setGlobalAlpha(0.8);
		ctxSplash.fillText("Wordsearch - English", 50, 130); 
		ctxSplash.setGlobalAlpha(1);

		ctxSplash.setShadowOffsetX(0);
		ctxSplash.setShadowOffsetY(0);
		ctxSplash.setShadowBlur(0);
		
	    //Draw Buttons
		btnEasy.draw(ctxSplash);
		btnMiddle.draw(ctxSplash);
		btnHard.draw(ctxSplash);

	}

	public void hide() {
		canvasSplash.setVisible(false);
	}

	public void show() {
		canvasSplash.setVisible(true);
		draw();
	}
	
	private void checkButtonClicked(ClickEvent event) {

        if (btnEasy.isMouseOver()){
        	parent.soundClick.play();
        	start(0);
        }

        if (btnMiddle.isMouseOver()){
        	parent.soundClick.play();
        	start(1);
        }
        
        if (btnHard.isMouseOver()){
        	parent.soundClick.play();
        	start(2);
        }
	}

	private void start(int i) {
		canvasSplash.setVisible(false);
		parent.start(i);		
	}

	public void loadingFinished() {
		// TODO Auto-generated method stub
		
	}


}
