package net.gibberfish.games.wortsuche.client;

import com.google.gwt.canvas.dom.client.Context2d;

public class MenueDrawable {

	protected boolean bDraw = false;
	protected int MenueSlot = -1;
	protected int MenueSlotHeight = 25;
	protected int MenuePosX = 530;
	protected int MenuePosY = 30;
	protected String Text = null;
	protected boolean strikeThrough = false;
	
	public MenueDrawable(String text) {
		Text = text;
		bDraw = true;
	}

	public void draw(Context2d ctx_m) {
		ctx_m.setFillStyle("rgb(0,0,0)");
		ctx_m.setFont("12pt Arial");
		ctx_m.fillText(Text, 5, MenueSlot * MenueSlotHeight);   
    }
	
	public void setText(String text){
		Text = text;
		bDraw = true;
	}
}

