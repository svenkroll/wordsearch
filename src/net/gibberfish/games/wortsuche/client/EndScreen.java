package net.gibberfish.games.wortsuche.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class EndScreen {
	
	Canvas wonCanvas;
	Context2d ctxWon;
	
	int canvasHeight = WordSearch.AppHeight;
	int canvasWidth = WordSearch.AppWidth;
	
	BoardButton btnRestart;
	WordSearch parent;
	String message = "";
	
	public EndScreen(WordSearch wortsuche, int height, int width){
		
		parent = wortsuche;
		canvasHeight = height;
		canvasWidth = width;
		
		wonCanvas = Canvas.createIfSupported();
		wonCanvas.setVisible(false);
		if (wonCanvas == null) {
			RootPanel
					.get(WordSearch.holderId)
					.add(new Label(
							"Sorry, your browser doesn't support the HTML5 Canvas element"));
			return;
		}
		wonCanvas.setStyleName("endCanvas");
		wonCanvas.setWidth(canvasWidth+"px");
		wonCanvas.setCoordinateSpaceWidth(canvasWidth);

		wonCanvas.setHeight(canvasHeight+"px");
		wonCanvas.setCoordinateSpaceHeight(canvasHeight);

		RootPanel.get(WordSearch.holderId).add(wonCanvas);
		
		ctxWon = wonCanvas.getContext2d();
		
		btnRestart = new BoardButton(450, 330, 200, 60, "Play again");

		
		wonCanvas.addClickHandler(new ClickHandler() {
        	@Override
			public void onClick(ClickEvent event) {

	            checkButtonClicked(event);	
			}
		});   
		
		wonCanvas.addMouseMoveHandler(new MouseMoveHandler(){
            public void onMouseMove(MouseMoveEvent e) {
            	checkMouseOver(e.getX(),e.getY());
            }
		});

	}
	

	private void checkMouseOver(int x, int y) {
		
		if (btnRestart.checkMouseOver(x, y)){
			if (!btnRestart.isMouseOver()){
				btnRestart.setMouseOver(true);
				parent.soundSelect.play();
			}
        }else{
        	btnRestart.setMouseOver(false);
        }

        draw();
	}
	
	public void draw(){
		ctxWon.clearRect(0,0, canvasWidth, canvasHeight);

		//ctxWon.setStrokeStyle("#A0522D");
		//ctxWon.setLineWidth(5);
		//ctxWon.strokeRect(0,0, canvasWidth, canvasHeight);

		
		ctxWon.setTextAlign("left");
		ctxWon.setTextBaseline("middle");
		ctxWon.setFont("bold 40pt Comic Sans MS"); 
		ctxWon.setShadowColor("rgb(190, 190, 190)");
		ctxWon.setShadowOffsetX(5);
		ctxWon.setShadowOffsetY(5);
		ctxWon.setShadowBlur(10);
		ctxWon.setFillStyle("rgb(255,255,255)");
		ctxWon.setGlobalAlpha(0.8);
		ctxWon.fillText(message, 100, 230); 
		ctxWon.setGlobalAlpha(1);

		ctxWon.setShadowOffsetX(0);
		ctxWon.setShadowOffsetY(0);
		ctxWon.setShadowBlur(0);
		
	    //Draw Buttons
		btnRestart.draw(ctxWon);
	}

	public void hide() {
		wonCanvas.setVisible(false);
	}

	public void setMessage(String message){
		this.message = message;
	}
	
	public void show() {
		wonCanvas.setVisible(true);
		draw();
	}
	
	private void checkButtonClicked(ClickEvent event) {

        if (btnRestart.isMouseOver()){
        	parent.soundClick.play();
        	parent.reset();
        	wonCanvas.setVisible(false);
        	parent.splash.show();
        }

	}

}
