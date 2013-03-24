package net.gibberfish.games.wortsuche.client;

import java.util.ArrayList;

public class TileField {

	private int rows;
	private int colums;
	private int offX, offY;
	
	private TileFieldObject[][] field;
	
	public TileField(int offsetX, int offsetY, int fieldrows, int fieldcolums,float fieldWidth ,float fieldHeight, ArrayList<Drawable> DrawStack) {
		offX = offsetX;
		offY = offsetY;
		rows = fieldrows;
		colums = fieldcolums;
		
		field = new TileFieldObject[rows][colums];
		initializeField(fieldWidth, fieldHeight, DrawStack);
	}
	
	public TileFieldObject getTileFieldObject(int iRow, int jColumn){
		return field[iRow][jColumn];
	}
	
	public TileFieldObject findTileFieldObject(int x, int y){
		for( int i = 0; i < field.length; i++){
			for (int j = 0; j < field[i].length; j++){
				if (field[i][j].isAt(x,y)){
					return field[i][j];
				}
			}
		}
		return null;
	}
	
	private void initializeField(float fieldWidth ,float fieldHeight, ArrayList<Drawable> DrawStack){
		for( int i = 0; i < field.length; i++){
			for (int j = 0; j < field[i].length; j++){
				field[i][j] = new TileFieldObject(offX, offY, fieldWidth, fieldHeight, i, j, this);
				DrawStack.add(field[i][j]);
				field[i][j].bDraw = true;
			}
		}
	}

}
