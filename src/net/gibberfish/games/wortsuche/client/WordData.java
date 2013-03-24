package net.gibberfish.games.wortsuche.client;

import java.util.ArrayList;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;

public class WordData {
	
	private WordObject wo[];
	
	private int WordCount = 0;
	
	public int getWordCount() {
		return WordCount;
	}

	public boolean isReady = false;

	public void loadDataFile(String JSON_URL_ABS){
		
		try { 
			RequestBuilder rb = new RequestBuilder( RequestBuilder.GET, JSON_URL_ABS); 
			rb.setCallback(new RequestCallback() { 
				@Override 
				public void onResponseReceived(Request request, Response response) { 
					parseJsonData(response.getText()); 
				}
				@Override 
				public void onError(Request request, Throwable exception) {
					Window.alert("Error occurred" + exception.getMessage()); 
				} 
			}); 
			rb.send(); 
		} 
		catch (RequestException e) { 
			Window.alert("Error occurred" + e.getMessage()); 
		} 
		
	}
	
	private void parseJsonData(String json) {

        JSONValue value = JSONParser.parseLenient(json);
        JSONArray WordsArray = value.isArray();
        
        wo = new WordObject[WordsArray.size()];
        
        WordCount = WordsArray.size();
        
        for (int i = 0; i < WordsArray.size(); i++){
        	JSONValue v = WordsArray.get(i);
        	JSONObject o = v.isObject();
        	
        	wo[i] = new WordObject();
        	
        	wo[i].setOriginal(o.get("original").isString().stringValue());
        	wo[i].setTranslation(o.get("uebersetzung").isString().stringValue());
        	wo[i].setId( o.get("id").isString().stringValue());
        }
        
        isReady = true;
    }

	public WordObject getRandomWord() {
		boolean found = false;
		WordObject returnObject = null;
		while (!found){
			int rand = Random.nextInt(WordCount);
			if (!wo[rand].isSet() && !wo[rand].isNotEnoughSpaceToSet()){
				returnObject = wo[rand];
				found = true;
			}
		}
		return returnObject;
	}

	public WordObject getWo(int i) {
		return wo[i];
	}

	public WordObject isWord(ArrayList<TileFieldObject> dragPath) {
		for (WordObject w : wo){
			boolean found = true;
			int i = 0;
			for (TileFieldObject o : dragPath){
				i++;
				if (!w.hasField(o.getiRow(), o.getiColmn())){
					found = false;
					break;
				}
			}
			if (found && i == w.getTranslation().length()){
				return w;
			}
		}
		return null;
	}
}

