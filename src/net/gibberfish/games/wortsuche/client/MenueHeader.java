package net.gibberfish.games.wortsuche.client;

import com.google.gwt.canvas.dom.client.Context2d;

public class MenueHeader extends MenueDrawable{

	public MenueHeader(String text) {
		super(text);
	}

	@Override
	public void draw(Context2d ctx_m) {
		
		ctx_m.setTextBaseline("middle");
    	ctx_m.setTextAlign("left");		
		ctx_m.setShadowColor("rgb(190, 190, 190)");
		ctx_m.setShadowOffsetX(5);
		ctx_m.setShadowOffsetY(5);
		ctx_m.setShadowBlur(10);
		ctx_m.setFillStyle("rgb(255,255,255)");
		ctx_m.setGlobalAlpha(0.8);
		ctx_m.setFont("bold 26px Comic Sans MS");
		ctx_m.fillText(Text, MenuePosX, MenuePosY + 30);   		
		ctx_m.setGlobalAlpha(1);

		ctx_m.setShadowOffsetX(0);
		ctx_m.setShadowOffsetY(0);
		ctx_m.setShadowBlur(0);
	    
	    bDraw=false;
    }

}
