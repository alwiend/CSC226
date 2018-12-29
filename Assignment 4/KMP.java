/*
* 	Alwien Dippenaar
* 	V00849850
* 	CSC 226 Assignment 4
* 	November 23 2018
* */

/*
*	Input: A text file (the genome) and the pattern (the gene), both entirely consisting
*	       of the characters from the alphabet {A, C, G, T}.
*	Output: If the text contains the pattern, the index of the first occurrence of the
		pattern in the text. Otherwise, the length of the text.
* */

/*
   (originally from R.I. Nishat - 08/02/2014)
   (revised by N. Mehta - 11/7/2018)
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class KMP{
    private String pattern;
    private char[] alphabet = {'A','C','G','T'};
    private int[][] dfa;
    
    public KMP(String pattern){
	      this.pattern = pattern;
        // Build the DFA
        int m = pattern.length();	// Length of pattern
        int n = alphabet.length;	// Size of alphabet {A,C,G,T}
		    dfa = new int[n][m];
		    int x = (getcharIndex(pattern.charAt(0)));
		    dfa[x][0] = 1;
		    for (int i = 0, j = 1; j < m; j++) {
			      for (int k = 0; k < n; k++) {
				        dfa[k][j] = dfa[k][i];
			      }
			      int y = getcharIndex(pattern.charAt(j));
			      if (y == -1) {
				        System.out.println("Character: " + pattern.charAt(j) + " does not exist in alphabet.");
			      }
			      dfa[y][j] = j+1;
			      i = dfa[y][i];
		    }
    }

    /*
    * 	Function to return the index of a char
    * 	@param char c - The char to return the index of from the alphabet array
    * 	@return i - The index of the char
    * 	@return -1 - The index was not found in the alphabet
    * */
    public int getcharIndex(char c) {
        for (int i = 0; i < alphabet.length; i++) {
            if (c == alphabet[i]) {
                return i;
            }
        }
        return -1;
    }

    /*
    * 	Function to search for the pattern in a string of text
    * 	@param String txt - The text to search for the pattern in
    * 	@return (i - m) - The index of where the pattern starts
    * 	@return n - length of string, therefore pattern is not in the string
    * */
    public int search(String txt){
    	  int m = pattern.length();	// Length of the pattern to search for
    	  int n = txt.length();		// Length of the string to search in
		    int i, j;
		    for (i = 0, j = 0; i < n && j < m; i++){
			      j = dfa[getcharIndex(txt.charAt(i))][j];
		    }
		    if (j == m) {
			      return i - m;
		    }
		    return n;
    }
  
  /*
  *   Main function used for testing the code.
  * */
  public static void main(String[] args) throws FileNotFoundException{
	    Scanner s;
	    if (args.length > 0){
	        try{
		          s = new Scanner(new File(args[0]));
	        }
	        catch(java.io.FileNotFoundException e){
		          System.out.println("Unable to open "+args[0]+ ".");
		          return;
	        }
	        System.out.println("Opened file "+args[0] + ".");
	        String text = "";
	        while(s.hasNext()){
		          text += s.next() + " ";
	        }
	        for(int i = 1; i < args.length; i++){
		          KMP k = new KMP(args[i]);
		          int index = k.search(text);
		          if(index >= text.length()){
		              System.out.println(args[i] + " was not found.");
		          }
		          else System.out.println("The string \"" + args[i] + "\" was found at index " + index + ".");
	        }
	    } else {
	        System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");
	    }
   }
}
