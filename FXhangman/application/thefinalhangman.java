package application;

// Stylianos Kandylakis 03117088
// 30//12/2021

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class thefinalhangman {
	
	private String secretWord;       			   // the chosen secret word
	private ArrayList<Character> correctLetters;   // correct guesses by now ( put _ if not guessed)
	private ArrayList<Boolean> boolist;            // which have been guessed correctly by now 
	private String[] words;   					   // lexicon
	
	static int badGuesses;
	static int totalPoints;
	static int lettersfound; 					   //how many letters are completed
				
	///////TUPLE CLASS////////////////////////////////
	public class tuple<S,T> {

		private S data;
		private T index;

		public tuple(S s, T t) 
		{
		    this.data = s;
		    this.index = t;
		}

		public S getData()
		{
		    return data;
		}

		public void setData(S data)
		{
		    this.data = data;
		}

		public T getIndex()
		{
		    return index;
		}

		public void setIndex(T index)
		{
		    this.index = index;
		}

		
	}
	
	//////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////
	public thefinalhangman(Path file)  {                          

		badGuesses = 0;
		totalPoints = 0;
		lettersfound = 0;
		
		List<String> lines;
		try {
			lines = Files.readAllLines(file, StandardCharsets.UTF_8);
			this.words = lines.toArray(new String[lines.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Randomly choose a word from lexicon of words
		Random randIndex = new Random();
		int index = randIndex.nextInt(words.length);
		
		this.secretWord = words[index];       //insert from lexicon file
		//this.secretWord = "GREECE";
		
		//erase the secret word from lexicon
		List<String> list = new ArrayList<String>(Arrays.asList(words));
		list.remove(index);
		this.words = list.toArray(new String[0]);
		
		// create lists
		this.correctLetters = new ArrayList<Character>();       //correctletters = [_ _ _ _ _ _]
		for (int i = 0; i < this.secretWord.length(); i++)
			this.correctLetters.add('_');  

		this.boolist = new ArrayList<Boolean>();
		for(int i=0; i<this.secretWord.length(); i++) {    //initialize boolean list = true where letters found
			this.boolist.add(i, false);
		}

	}
	
	
	
	//////////////////////////////////////////////////////////////////////
	// PRIVATE INSTANCE METHODS (HELPERS)
	//////////////////////////////////////////////////////////////////////

	/**
	 * playRound
	 *
	 * The Controller calls the method through playMove  
	 * so the instance get's updated
	 *  
	 * @param letter the guessed letter
	 * @param index the position to PUT the character ch
	 */
	private void playRound(String letter , String index) {

		handleGuess(letter.charAt(0) , Integer.parseInt(index));
	}
	
	/**
	 * handleGuess
	 *
	 * If the guessed letter is in the secret word
	 * then it must appear and points added
	 * otherwise, decrease points 
	 *
	 * @param ch the guessed letter
	 * @param index the position to PUT the character ch
	 */
	private void handleGuess(char ch , int index) {
		 
		//number of letters found
		//update boolean array
		//remove the common words in lexicon
		//update points
		if((this.secretWord.charAt(index) == ch) && (this.boolist.get(index) != true)){
               this.lettersfound++;
               this.correctLetters.set( index , ch );      //replace the _ with the correct guessed ch: correctLetters = [ _ g _ _ ]    
		       this.boolist.set(index , true);
		       processwordsfound(ch, index);
		       this.totalPoints = this.totalPoints + points(ch,index) ; // dependency 
				
		}
		//number of bad guesses
		//remove the common words in lexicon
		//update points
		else {   	                  	                     
			this.badGuesses++;
			//printHangman(this.badGuesses);
			processwordsnotfound(ch, index);
		    this.totalPoints = this.totalPoints - 15; 
			if(this.totalPoints < 0 ) { this.totalPoints = 0; }
		}
		
	}
	
     ///////FIND WORDPERCENT FOR GIVEN INDEX//////////////////////////////////
	/**
	 * wordpercent
	 *
	 * for given index-position (different from the playGame index variable)
	 * return the list of sorted suggested letters to choose from 
	 * 
	 * @param index the position in the word
	 */
	private ArrayList<Character> wordpercent(int index) {     // for the index letter, not the main index 
	      
		  	//find the words that contain the same index letter in the index position
		  int count;	
		  int truevalues;	
		  int wordcount;
		  
		  truevalues = this.lettersfound;
		  
		  //tuples = [ ('a' , 0 ) , ('b' , 1) , .... ]
		  //Character[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		  Character[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		  
		  List<tuple<Character,Float>> tuples = new ArrayList<tuple<Character,Float>>();   
		
		  //need this to sort the tuples
		  Comparator<tuple<Character, Float>> comparator = new Comparator<tuple<Character,Float>>(){
			  public int compare(tuple<Character,Float> tupleA, tuple<Character,Float> tupleB){ 
				  return tupleB.getIndex().compareTo(tupleA.getIndex());
			  }
		  };
		
		  //find the suggestion list
		  for(Character c:alphabet ) {     //per possible character 
		  	wordcount = 0;
		  	
		  	for(String word: this.words) {
		  		count = 0;
		          if( (word.length() == this.secretWord.length()) && ( word.charAt(index) == c ) ) {                    //  same length && same index letter
		          	for(int i=0; i< this.secretWord.length(); i++) {                                                    //continue iterating the possible word
		          		if( (this.boolist.get(i) == true) && (word.charAt(i) == this.secretWord.charAt(i) )) { 
		          			count++;                                                                               //count common #letters per word
		          		}    
		            }
		          	
		            if(count == truevalues) { wordcount++;}
		  	
		         }
		  	}
		     float percentvalue = ((float) wordcount) / ((float) this.words.length);
		     tuples.add(new tuple<Character,Float>(c , percentvalue));
		  }	
		   
		  //tuples sorted now
		  Collections.sort(tuples, comparator);
		  
		  ArrayList<Character> sortedlist = new ArrayList<Character>();
		  
		  //sort
		  for (tuple<Character, Float> t : tuples)
		  {  
		  	sortedlist.add(t.getData());
		  }
		  
		  return sortedlist;                
	  } 
	
	
	/**
	 * processwordsfound
	 * 
	 * if letter found correctly
	 * then remove the words from lexicon that don't have the same letter in same position
	 * 
	 * @param index of letter found successfully
	 * @param letter found successfully
	 */
	private void processwordsfound(char ch, int index) {
		
		List<String> wordslist = new ArrayList<String>(Arrays.asList(this.words));
		
	    	for(String word: this.words) {
	            if( (word.length() == this.secretWord.length()) && ( word.charAt(index) !=  ch )) { //  same length && same index letter
	            	
	            	//System.out.println("the:   " +word);
	            	wordslist.remove(word);
	                   
	            }
	    	}   	  
	            	    			
	    	this.words = wordslist.toArray(new String[0]);		
		/*
	    	for(String word: this.words) {
	    	System.out.println("the:   " + word);
	
	    	}
	*/
	}	
	
	
	/**
	 * processwordsnotfound
	 * 
	 * if letter isn't found correctly
	 * then remove the words from lexicon that have the same letter in same position
	 * 
	 * @param ch letter not found successfully
	 * @param index position of letter not found successfully
	 */
	private void processwordsnotfound(char ch, int index) {
		
		List<String> wordslist = new ArrayList<String>(Arrays.asList(this.words));
		
	    	for(String word: this.words) {
	            if( (word.length() == this.secretWord.length()) && ( word.charAt(index) ==  ch )) { //  same length && different index letter
	            	
	            	//System.out.println("the:   " +word);    //word to drop from vocabulary
	            	wordslist.remove(word);	                   
	            }
	    	}   	  
	            		
	    	this.words = wordslist.toArray(new String[0]);		

	}	
	
	
	/**
	 * suggestions
	 * 
	 * every time the user is about to put the next letter 
	 * print lists of suggested letters for every empty space
	 * 
	 */
	private String suggestions() {
	  	ArrayList<String> arr = new ArrayList<String>();
	  	
	  	for(int i=0; i< this.secretWord.length(); i++) {
	  		if( this.boolist.get(i) == false) {
	 
	  			//ArrayList<Char> -> String[]
	  			StringBuilder sb = new StringBuilder();
	  			 
		        for(Character ch : wordpercent(i)) {
		            sb.append(ch);
		            sb.append(" ");
		        }
		        String string = sb.toString();  		 
	  			arr.add(string);
	  		}  		
	  	}
	  	//String[] -> String
	  	StringBuffer st = new StringBuffer();
	    
	  	ArrayList<Integer> array = new ArrayList<Integer>();
	  	
	  	for(int i=0; i<boolist.size(); i++){ if(boolist.get(i)== false) { array.add(i); }}	
	  	
	  	st.append("proposed letters for empty slot number "+ "\n\n");
	  	
	  	int pos=0;
	  	for(String s: arr) {
	       st.append( Integer.toString(array.get(pos)) + ":   " + s+"\n");
	       pos++;
	      }
	      
	  	String finalstr = st.toString();
	  	
	 	return finalstr;      
	}
	
	/**
	 * points
	 * 
	 * when the user finds successfully the letter
	 * compute the points won 
	 * 
	 * @param ch letter found successfully
	 * @param index position of letter successfully found
	 */
	private int points(char ch, int index) {
		int count;
		int wordcount=0;
		
		//for char ch find the percentage of words having same length and same letters in already found positions
		for(String word: this.words) {
    		count = 0;
            if( (word.length() == this.secretWord.length()) && ( word.charAt(index) == ch ) ) { //  same length && same index letter
            	for(int i=0; i< this.secretWord.length(); i++) {                                //continue iterating the possible word
            		if( (this.boolist.get(i) == true) && (word.charAt(i) == this.secretWord.charAt(i) )) { 
            			count++;                          //count #common-letters per word
            		}    
                }

              if(count == this.lettersfound) { wordcount++;}
           }
    	}
		
		float percent = ((float) wordcount) / ((float) this.words.length);
		
		//probabilities
		if(percent >0.6f || percent == 0.6f ) {return 5;}
		
		else if( percent == 0.4f || (percent > 0.4f && percent < 0.6f) ) {return 10;}
		
		else if( percent == 0.25 || (percent >0.25f || percent < 0.4f ) ) {return 15;}
		
		else {return 30;}

	}


	/**
	 * gameWon
	 *
	 * Return true if the user has won the game;
	 * otherwise, return false.
	 *
	 * @return true if the user has won, false otherwise
	 */
	private boolean gameWon() {
		
		if(lettersfound == secretWord.length()) { return true;}
		
		return false;
	}
	
	/**
	 * gameLost
	 *
	 * Return true if the user has lost the game;
	 * otherwise, return false.
	 *
	 * @return true if the user has lost, false otherwise
	 */
	private boolean gameLost() {
 
		if(this.badGuesses == 6  ) {return true;}
		
		return false;
	}
	
	/**
     * gameOver
     *
     * Return true if the user has either won or lost the game;
     * otherwise, return false.
     *
     * @return true if the user has won or lost, false otherwise
     */
    private boolean gameOver() {

    	if(gameLost() || gameWon()) {return true;}
    	
    	return false;
    }
	
	/**
     * percentlexicon
     *
     * count percent of words with certain length
     *      
     * @return percent percent for every word type
     */ 
    private ArrayList<Float> percentlexicon(){
    	
    	int sixletters = 0;
    	int seventonine = 0;
    	int tenatleast = 0;
    	
    	ArrayList<Float> percent = new ArrayList<Float>();
    	
    	for(String word: this.words) {
    		
    		if(word.length() == 6  ) {
    			sixletters ++ ;
    		}
    		else if( word.length() >6 && word.length() <10) {
    			seventonine++;
    		}
    		else if( word.length() >9) {
    			tenatleast++;
    		}
    		
    	}
    	
    	percent.add((float)100*(float) sixletters / (float) this.words.length );
    	percent.add((float)100*(float) seventonine / (float) this.words.length);
    	percent.add((float)100*(float) tenatleast / (float) this.words.length);
    	
    	
    	return percent;
    	
    }
	
	//////////////////////////////////////////////////////////////////////
	// PUBLIC CLASSES FOR CONTROLLER
	//////////////////////////////////////////////////////////////////////
    
    public ArrayList<Float> printPercentLex(){ return percentlexicon(); }
    
	public void playMove(String letter, String index) { this.playRound(letter,index);}
	
	public String SecretWord() { return this.secretWord;}
	
	public int SecretwordSize() { return this.secretWord.length();}
	
	public ArrayList<Boolean> positions() { return this.boolist; }
	
	public String printLettersfound() {
		float per = (float) this.lettersfound / (float) this.secretWord.length();
		return String.valueOf(per);
	}
	
	public String printPoints() { return String.valueOf(this.totalPoints); }
	
	public String printNumWords() { return String.valueOf(this.words.length); }
	
	public String printCorrectLetters() {
		 StringBuilder sb = new StringBuilder();
		 
	        // Appends characters one by one
	        for (Character ch : this.correctLetters) {
	            sb.append(ch);
	            sb.append(" ");
	        }
	 
	        // convert to string
	        String string = sb.toString();
		
		return string;
	}
    
	public int numlettersfound() {
		return this.lettersfound;
	}
	
	public int printNumLexiconLetters() {
		return this.words.length;
	}
	
	public boolean isGameOver() {
		return this.gameOver();
	}
	
	public int numBadGuesses() {
		return this.badGuesses;
	}
	
	public String printSuggestions() {
		return this.suggestions();
	}
	
	//////////////////////////////////////////////////////////////////////
	// PUBLIC CLASS METHOD - MAIN
	//////////////////////////////////////////////////////////////////////
	
	public static void main(String [] args) {}
	
}