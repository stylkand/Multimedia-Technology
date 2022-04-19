package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class finalehangman {
	
	private String secretWord;       // the chosen secret word
	private ArrayList<Character> correctLetters;   // correct guesses by now ( put _ if not guessed)
	private ArrayList<Boolean> boolist;            // which have been printed by now 
	
	private String[] words;  
	static int badGuesses = 0;
	static int totalPoints = 0;
	static int lettersfound = 0;  //how many letters are completed
				    
	private Scanner stdin = new Scanner(System.in);       // for user input
				
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
	public finalehangman(String[] sarray) {                            
	
		this.words = sarray;
		
		//Randomly choose a word from list of words
		Random randIndex = new Random();
		int index = randIndex.nextInt(words.length);
		
		//this.secretWord = words[index];       //insert from lexicon file
		this.secretWord = "truck";

		
		//erase the secret world from lexicon
		List<String> list = new ArrayList<String>(Arrays.asList(words));
		list.remove(index);
		words = list.toArray(new String[0]);
		
		// create lists
		this.correctLetters = new ArrayList<Character>();       //correctletters = [_ _ _ _ _ _]
		for (int i = 0; i < this.secretWord.length(); i++)
			this.correctLetters.add('_');  

		this.boolist = new ArrayList<Boolean>();
		for(int i=0; i<secretWord.length(); i++) {    //initialize boolean
			boolist.add(i, false);
		}
		
	}
    	
    	    	
	//////////////////////////////////////////////////////////////////////
	// PRIVATE INSTANCE METHODS (HELPERS)
	//////////////////////////////////////////////////////////////////////

	/**
	 * playGame
	 *
	 * Play one game of Hangman until the user guesses all of the letters (win) in the secret word 
	 * or guesses 7 incorrect letters (lose) :
	 */
	private void playGame() {     //play until all characters guessed or guess 7 incorrect letters
		
		while (!gameOver()) {
					
			//Print the correct guesses in the secret word
			for (int i = 0; i < this.correctLetters.size(); i++)
				System.out.print(this.correctLetters.get(i) + " ");     //get(i) -> return element with index i
			
			System.out.println();
			printsuggestions();
			
			//Prompt and read the next guess
			System.out.print("\nEnter a lower-case letter: ");
			String guess = stdin.nextLine();

			System.out.print("\nEnter the index to put it: ");
		    String guessindex = stdin.nextLine();
			
		   		
		    //Process the next guess
		    
		    handleGuess(guess.charAt(0) , Character.getNumericValue(guessindex.charAt(0)));
		    
		    System.out.print("\n");
		    System.out.print("Points:  "+ totalPoints);
		    System.out.print("\n\n");
		    
		}
		
		System.out.println("The secret word was: " + secretWord);
		if (gameWon()) {
			System.out.println("Congratulations, you won!");
		} else {
			System.out.println("Sorry, you lost.");			                          
		}
	}
		    
	
	/**
	 * handleGuess
	 *
	 * If the guessed letter is in the secret word
	 * then it must appear and points added
	 * otherwise, decrease point
	 *
	 * @param ch the guessed letter
	 * @param index the position to put the character ch
	 */
	private void handleGuess(char ch , int index) {
		 
		//create a list of booleans were the character is found
		if((secretWord.charAt(index) == ch) && (boolist.get(index) != true)){
               lettersfound++;
               correctLetters.set( index , ch );             //replace the _ with the correct guessed ch: correctLetters = [ _ g _ _ ]    
		       boolist.set(index , true);
		       processwordsfound(ch, index);
		       totalPoints = totalPoints + points(ch,index) ; // dependency 
				
		}
		else {   	                  	                     
			badGuesses++;
			printHangman(badGuesses);
			processwordsnotfound(ch, index);
		    totalPoints = totalPoints - 15; 
			if(totalPoints < 0 ) { totalPoints = 0; }
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
	  
	  truevalues = truebool(boolist);   //counts how many true values
	  
	  //tuples = [ ('a' , 0 ) , ('b' , 1) , .... ]
	  Character[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
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
	  	
	  	for(String word: words) {
	  		count = 0;
	          if( (word.length() == secretWord.length()) && ( word.charAt(index) == c ) ) {                    //  same length && same index letter
	          	for(int i=0; i< secretWord.length(); i++) {                                                    //continue iterating the possible word
	          		if( (boolist.get(i) == true) && (word.charAt(i) == secretWord.charAt(i) )) { 
	          			count++;                                                                               //count common #letters per word
	          		}    
	            }
	          	
	            if(count == truevalues) { wordcount++;}
	  	
	         }
	  	}
	     float percentvalue = ((float) wordcount) / ((float) words.length);
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
	 * if letter found correctly
	 * then remove the words from lexicon that don't have the same letter in same position
	 * 
	 * @param index of letter found successfully
	 * @param letter found successfully
	 */
	private void processwordsfound(char ch, int index) {
		
		//char c = secretWord.charAt(index);

		List<String> wordslist = new ArrayList<String>(Arrays.asList(words));
		
	    	for(String word: words) {
	            if( (word.length() == secretWord.length()) && ( word.charAt(index) !=  ch )) { //  same length && same index letter
	            	
	            	System.out.println("the word:   " +word + "   was removed");
	            	wordslist.remove(word);
	                   
	            }
	    	}   	  
	            
	    			
	    	words = wordslist.toArray(new String[0]);		
	    	
	    	System.out.println();
	    	System.out.println("words remaining in lexicon: ");
	    	
	    	for(String word: words) {
	    	System.out.println(word);
	
	    	}
	    	System.out.println();	    	
	}	
	
	
	/**
	 * processwordsnotfound
	 * if letter isn't found correctly
	 * then remove the words from lexicon that have the same letter in same position
	 * 
	 * @param ch letter not found successfully
	 * @param index position of letter not found successfully
	 */
	
	private void processwordsnotfound(char ch, int index) {
		
		//char c = secretWord.charAt(index);

		List<String> wordslist = new ArrayList<String>(Arrays.asList(words));
		
	    	for(String word: words) {
	            if( (word.length() == secretWord.length()) && ( word.charAt(index) ==  ch )) { //  same length && same index letter
	            	
	            	System.out.println("the word:   " +word + "   was removed");
	            	wordslist.remove(word);
	                   
	            }
	    	}   	  
	            
	    			
	    	words = wordslist.toArray(new String[0]);		
		
	    	System.out.println();
	    	System.out.println("words remaining in lexicon: ");
	    	for(String word: words) {
	    	System.out.println(word);
	
	    	}
	
	    	System.out.println();    	
	}	
	
	
	/**
	 * printsuggestions
	 * 
	 * every time the user is about to put the next letter 
	 * print lists of suggested letters for every empty space
	 * 
	 */
	public void printsuggestions() {
	  	
	  	for(int i=0; i< secretWord.length(); i++) {
	  		if( boolist.get(i) == false) {
	  		System.out.println("letters to choose for "+i +"-th space " + wordpercent(i) );
	  		}  		
	  	}
	 	
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
		
		for(String word: words) {
    		count = 0;
            if( (word.length() == secretWord.length()) && ( word.charAt(index) == ch ) ) { //  same length && same index letter
            	for(int i=0; i< secretWord.length(); i++) {                                                    //continue iterating the possible word
            		if( (boolist.get(i) == true) && (word.charAt(i) == secretWord.charAt(i) )) { 
            			count++;                          //count common #letters per word
            		}    
                }
            	
              if(count == truebool(boolist)) {
                	wordcount++;
              }
           }
    	}
		
		float percent = ((float) wordcount) / ((float) words.length);
		
		
		if(percent >0.6f || percent == 0.6f ) {return 5;}
		
		else if( percent == 0.4f || (percent > 0.4f && percent < 0.6f) ) {return 10;}
		
		else if( percent == 0.25 || (percent >0.25f || percent < 0.4f ) ) {return 15;}
		
		else {return 30;}

	}
	 
	/**
	 * printsuggestions
	 * 
	 * compute how many letters are completed 
	 */
	private int truebool(ArrayList<Boolean> arr) {
		int count=0;
		for(Boolean bool: arr) {
			if(bool == true) {count++;}
		}
		
		return count;
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
 
		if(badGuesses == 6  ) {return true;}
		
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
	 * printHangman
	 *
	 * Print the Hangman that corresponds to the given number of
	 * wrong guesses so far.
	 *
	 * @param numWrong number of wrong guesses so far
	 */
	private void printHangman(int badGuesses) {
		int poleLines = 6;   // number of lines for hanging pole
		System.out.println("  ____");
		System.out.println("  |  |");
		
		//int badGuesses = this.incorrectLetters.size();
		if (badGuesses == 7) {
			System.out.println("  |  |");
			System.out.println("  |  |");
		}
		
		if (badGuesses > 0) {	    	   
			System.out.println("  |  O");
			poleLines = 5;
		}
		if (badGuesses > 1) {
			poleLines = 4;
			if (badGuesses == 2) {
				System.out.println("  |  |");
			} else if (badGuesses == 3) {
				System.out.println("  | \\|");
			} else if (badGuesses >= 4) {
				System.out.println("  | \\|/");
			}
		}
		if (badGuesses > 4) {
			poleLines = 3;
			if (badGuesses == 5) {
				System.out.println("  | /");
			} else if (badGuesses >= 6) {
				System.out.println("  | / \\");
			}
		}
		if (badGuesses == 7) {
			poleLines = 1;
		}

		for (int k = 0; k < poleLines; k++) {
			System.out.println("  |");
		}
		System.out.println("__|__");
		System.out.println();
	}
	
	
	//////////////////////////////////////////////////////////////////////
	// PUBLIC CLASS METHOD - MAIN
	//////////////////////////////////////////////////////////////////////
	
	public static void main(String [] args) {

		//DICTIONARY-ID.txt
		//txt ->  String[]
		
		String [] words =   //choose secret word from these -> should be lexicon file
		{"geography", "cat", "yesterday", "java", "truck", "opportunity",
			"fish", "token", "transportation", "bottom", "apple", "cake",
			"remote", "pocket", "terminology", "arm", "cranberry", "tool",
			"caterpillar", "spoon", "watermelon", "laptop", "toe", "toad",
			"fundamental", "capitol", "garbage", "anticipate", "apple"};
		
		
		//String[] words = { "angle" , "angst", "aoley" , "randy" , "outer" , "job", "man"};
	   
		// secretWord=anger;  index = 2 ->g and already found 0,1   -> percent =  2/8 = 0.250
		//                    index = 2 ->g and already found 3,4   -> percent =  1/8  = 0.125

		
		//let's play!	
		
		finalehangman game = new finalehangman(words);
		
		game.playGame();
	
		System.out.println("\n");
		System.out.println("The CONSTRUCTED game is:\n" + game);
       	System.out.println("\n======== END CONSTRUCTOR TEST  ========\n");
        
       	     
	}
	
}