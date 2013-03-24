package net.gibberfish.games.wortsuche.client;

import com.google.gwt.canvas.dom.client.Context2d;

public class TileFieldObject extends Drawable {

	private int iRow;
	private int iColmn;
	private boolean drawCircle, persisted = false;
	
	private String characterToDraw = null;
	
	public String getCharacterToDraw() {
		return characterToDraw;
	}

	public void setCharacterToDraw(String characterToDraw) {
		this.characterToDraw = characterToDraw;
		bDraw = true;
	}

	private TileField tField;
	
	private float fWidth;
	private float fHeight;

	//private Image img;

	public TileFieldObject(int offsetX, int offsetY, float fieldWidth, float fieldHeight, int row, int colmn, TileField tf) {
		iRow = row;
		iColmn = colmn;
		fWidth = fieldWidth;
		fHeight = fieldHeight;
		fPosX = offsetX + fWidth * iColmn;
		fPosY = offsetY + fHeight * iRow;
		tField = tf;
	}
	
	@Override
	public void draw(Context2d ctx) {
		ctx.clearRect(fPosX, fPosY, fWidth, fHeight);
	    if (drawCircle){
	    	ctx.setGlobalAlpha(1);
	    	ctx.setShadowOffsetX(0);
			ctx.setShadowOffsetY(0);
			ctx.setShadowBlur(0);
	    	ctx.beginPath();
	    	ctx.arc(fPosX+(fWidth/2), fPosY+(fHeight/2), (fWidth/2)-3, 0, 360, false);
	    	ctx.closePath();
	    	ctx.setLineWidth(1);
	    	ctx.setFillStyle("#FFFFFF");
	    	ctx.setGlobalAlpha(0.4);
	    	ctx.fill();	      
	    	ctx.setStrokeStyle("#FFFFFF");	      
	    	ctx.stroke();
	    	ctx.setGlobalAlpha(1);
	    }
	    //Draw character
	    if (characterToDraw != null){
	    	ctx.setTextBaseline("hanging");
	    	ctx.setTextAlign("center");
	    	ctx.setShadowColor("rgb(190, 190, 190)");
			ctx.setShadowOffsetX(2);
			ctx.setShadowOffsetY(2);
			ctx.setShadowBlur(8);
			ctx.setFillStyle("rgb(255,255,255)");
			ctx.setGlobalAlpha(0.8);
			ctx.setFont("bold "+(fHeight/2)+"pt Comic Sans MS");
			ctx.fillText(characterToDraw, fPosX+(fWidth/2), fPosY-3);
			ctx.setGlobalAlpha(1);

			ctx.setShadowOffsetX(0);
			ctx.setShadowOffsetY(0);
			ctx.setShadowBlur(0);
			
	    }
	    
	    bDraw=false;
    }
	
	public int getiRow() {
		return iRow;
	}
	
	public int getiColmn() {
		return iColmn;
	}

	public TileFieldObject getTileFieldBottom() {
		return tField.getTileFieldObject(iRow + 1, iColmn);
	}
	public TileFieldObject getTileFieldTop() {
		return tField.getTileFieldObject(iRow - 1, iColmn);
	}
	public TileFieldObject getTileFieldLeft() {
		return tField.getTileFieldObject(iRow, iColmn - 1);
	}
	public TileFieldObject getTileFieldRight() {
		return tField.getTileFieldObject(iRow, iColmn + 1);
	}

	public void setPersisted(boolean b){
		persisted = b;
	}
	
	public void drawCircle(boolean b){
		if (!persisted){
			drawCircle = b;
			bDraw = true;
		}
	}
	
	
	public boolean isAt(int x, int y) {
		if (x >= this.fPosX && x <= (this.fPosX + fWidth) && y >= this.fPosY && y <= (this.fPosY + fHeight)){
			return true;
		}
		return false;
	}
}
