
package application;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

//https://openlibrary.org/works/OL107192W.json       -> Try again, not enough words!
//https://openlibrary.org/works/OL31390631M.json	 -> Good choice, file created!
//https://openlibrary.org/books/OL33891821M.json	 -> Try again, very small words!


public class Parsecode { 
   
  
  private String description;
  private List<String> clean_description;
  private String warnings;
   
  //////////////////////////////////////////////////////////////////////
  // CONSTRUCTOR
  //////////////////////////////////////////////////////////////////////
  public Parsecode(String book_id){
	
	//1.get description text  
	this.description = jsonGetRequest("https://openlibrary.org/works/"+book_id+".json");
	
    //2.list with cleaned text
    this.clean_description = words(this.description);
    
    //3.check exceptions
	this.warnings = check(this.clean_description);
	
  }

  
  // Stream -> String
  private String streamToString(InputStream inputStream) {    
    String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
    return text;
  }

  /**
   * jsonGetRequest
   *
   * processes the url, returns the text of value field
   *  
   * @param urlQueryString the link of the book
   */  
  private String jsonGetRequest(String urlQueryString) {
	String json = null;
	String pageName = null;
	  
	try {
      URL url = new URL(urlQueryString);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      InputStream inStream = connection.getInputStream();
      json = streamToString(inStream); // input stream to string
    } 
    catch (IOException ex) { ex.printStackTrace();}
    
    try{
    JSONObject obj = new JSONObject(json); 
	pageName = obj.getJSONObject("description").getString("value");   
    } 
    catch (JSONException e){;}
    
    return pageName;
  }
  
  /**
   * words
   *
   * text pre-processing(removing punctuations etc)
   *  
   * @param text text of the value field
   */   
  private List<String> words(String text) {
	 String result = text.replaceAll("\\p{Punct}", ""); //erase punctuation
	 
	 String capital = result.toUpperCase();  // convert to UpperCase

	 String[] results= capital.split("\\s"); // split strings
	 
	 //processing
	 
	 List<String> wordlist = new ArrayList<String>();
	 
	  for(String word:results){  
		  if(word.length() >5  && !(wordlist.contains(word)) )  {
		    
			  wordlist.add(word);
		  }
      }
	  		 
	 return wordlist;
  }

  /**
   * check
   *
   * check if requirements hold, else print exceptions
   *  
   * @param list processed text of the value field
   */ 
  private String check( List<String> list) {
	  int count = 0;
	  float threshold = 0.2f*((float)list.size()) ;
	  
	  if( list.size() < 20 ) {return "Try Again - Not enough words!";}
	  
	  else { 
	   for(String word:list){  if(word.length() > 8){ count++; } }  
	   
	   if((float)count < threshold) {return "Try Again - very small words!";} 
	   else { return "Good choice - let's play!";}
	  } 
  } 
  
  /**
   * savefile
   *
   * @param arr vocabulary to save  
   * @param dit_filename name of the file to save it to 
   */
  public boolean savefile(List<String> arr,String dic_filename) throws IOException {
	boolean bool = false;  
	String path = "C:\\Users\\42\\Desktop\\medialab\\hangman_"+dic_filename+".txt";
	File file = new File(path);
	if (file.createNewFile()) { bool = true;}
	
	FileWriter fw;
	try {
		fw = new FileWriter(file.getAbsoluteFile());
		
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(String word: arr ) { 
			bw.write(word); 
			bw.write('\n');}

	bw.close();
	}
	catch (IOException e) { e.printStackTrace();}
  
	return bool;
  }  
  
  //////////////////////////////////////////////////
  // HELP FUNCTIONS FOR CONTROLLER
  //////////////////////////////////////////////////
  public String getText() {
	  return this.description;
  }
  
  public List<String> getcleanText() {
	  return this.clean_description;
  }
  
  public String getWarnings() {
	  return this.warnings;
  }

  //////////////////////////////////////////////////
  // PUBLIC MAIN
  //////////////////////////////////////////////////
  public static void main(String[] args) {}
}
  
