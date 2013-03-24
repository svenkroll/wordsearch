package net.gibberfish.games.wortsuche.client;

import java.util.ArrayList;


public class WordObject {

	private String original;
	private String translation;
	private String id;
	private boolean isSet = false;
	private boolean notEnoughSpaceToSet = false;
	private boolean isFound = false;
	private ArrayList<int[]> fields;
	
	public WordObject(){
		fields = new ArrayList<int[]>();
	}
	
	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

	public boolean isNotEnoughSpaceToSet() {
		return notEnoughSpaceToSet;
	}
	public void setNotEnoughSpaceToSet(boolean notEnoughSpaceToSet) {
		this.notEnoughSpaceToSet = notEnoughSpaceToSet;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isSet() {
		return isSet;
	}
	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}
	public int getWordSize(){
		return translation.length();
	}
	public void addField(int x, int y) {
		fields.add(new int[]{x,y});		
	}
	public boolean hasField(int x, int y){
		for (int[] i : fields){
			if (i[0] == x && i[1] == y){
				return true;
			}
		}
		return false;
	}
	

	
}
