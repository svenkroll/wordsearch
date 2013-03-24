package net.gibberfish.games.wortsuche.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Timer;

public class CountDownClock extends Drawable{
	
	int timeLeft = 0;
	String timeString = "0 m 0 s";
	Timer clockTimer;
	WordSearch parent;
	
	public CountDownClock(WordSearch wordsearch){
		fPosX = 530;
		fPosY = 500;
		parent = wordsearch;
	}

	@Override
	public void draw(Context2d ctx) {
		
		ctx.clearRect(fPosX, fPosY, 250, 80);
		
		ctx.setTextBaseline("hanging");
		ctx.setTextAlign("left");		
		ctx.setShadowColor("rgb(190, 190, 190)");
		ctx.setShadowOffsetX(5);
		ctx.setShadowOffsetY(5);
		ctx.setShadowBlur(10);
		ctx.setFillStyle("rgb(255,255,255)");
		ctx.setGlobalAlpha(0.8);
		ctx.setFont("bold 24px Comic Sans MS");
		ctx.fillText("Time remaining:" , fPosX, fPosY);
		if (timeLeft < 10){
			ctx.setFillStyle("rgb(255,0,0)");
			ctx.setShadowColor("rgb(255, 110, 110)");
		}
		ctx.fillText(timeString, fPosX, fPosY + 30);
		ctx.setGlobalAlpha(1);

		ctx.setShadowOffsetX(0);
		ctx.setShadowOffsetY(0);
		ctx.setShadowBlur(0);
	    
	    bDraw=false;
	}

	public void setTime(int i) {
		timeLeft = i;
	}
	
	public static String getTimeAsString(int time)
    {

		int minutes = (time/60);
		int seconds = (time % 60);

        StringBuilder sb = new StringBuilder(64);
        sb.append(minutes);
        sb.append(" m ");
        sb.append(seconds);
        sb.append(" s");

        return(sb.toString());
    }
	
	public void tick(){
		if (timeLeft > 0){
			timeLeft--;
			timeString = getTimeAsString(timeLeft);
			if (timeLeft < 10){
				parent.soundBeep.play();
			}
			bDraw = true;
		}else{
			clockTimer.cancel();
			parent.timeOver();
		}
	}

	public void start() {
		bDraw = true;
		clockTimer = new Timer() {
			@Override
			public void run() {
				tick();
			}
		};
		clockTimer.scheduleRepeating(1000);
		
	}
}
