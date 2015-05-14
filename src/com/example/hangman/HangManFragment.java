package com.example.hangman;


import java.util.Random;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class HangManFragment extends Fragment 
{
//Sharedpreferences used for number of letters saved
private static final String NUMBER_LETTERS = "numberLetters";

private SharedPreferences savedLetters;
private ImageView hangManImageView; //ImageView for the hangman images
private EditText guessWordEditText; //EditText to enter the letter being guessed
private TextView alreadyGuessedTextView; //TextView to display all the guessed letters
private TextView numberGuessLeftTextView; //TextView displaying how many guesses left
private Animation shakeAnimation; //Shake the hangman image if letter gussed is incorrect
private TextView[] charViews; //TextView array to display the secret word
private String[] words; //List array for the secret word
private Random rand; //Random used to randomize the secret words list
private String currWord; //Variable to store the current secret word being used
private LinearLayout wordLayout; //Layout used to hold the secret word
private String guessedLetter; //Variable for the guessed letters
private String numberLetters; //Variable for amount of letters in the word
private int numberCorrect; //Variable to hold the number of correct guesses
private int numberChars; //Variable to hold the amount of characters in the secret word
private int mistakes; //Variable to hold the amount of guesses that are incorrect
private int wins = 0;
private int loses = 0;


// configures the HangManFragment when its View is created
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
   Bundle savedInstanceState)
{
   super.onCreateView(inflater, container, savedInstanceState);    
   View view = 
      inflater.inflate(R.layout.fragment_hangman, container, false);

   currWord = "";
   rand = new Random();
   
   //Retrieving the words list from the array
   Resources res = getResources(); 
   words = res.getStringArray(R.array.six_letter_words);
   
   // get references to GUI components
   wordLayout = (LinearLayout)view.findViewById(R.id.word);
   hangManImageView = (ImageView) view.findViewById(R.id.hangManImageView);
   numberGuessLeftTextView = (TextView) view.findViewById(R.id.numberGuessLeftTextView);
   guessWordEditText = (EditText) view.findViewById(R.id.guessWordEditText);
   alreadyGuessedTextView = (TextView) view.findViewById(R.id.alreadyGuessedTextView);
   
// get the SharedPreferences containing the letters guessed 
   savedLetters = getSharedPreferences(NUMBER_LETTERS, Context.MODE_PRIVATE);
   
   // load the shake animation that's used for incorrect answers
   shakeAnimation = AnimationUtils.loadAnimation(getActivity(), 
      R.anim.incorrect_shake); 
   shakeAnimation.setRepeatCount(3); // animation repeats 3 times 
   
   //Button used to start a new game
   Button newGameButton = (Button) view.findViewById(R.id.newGameButton);
   newGameButton.setOnClickListener(newGameButtonListener);
   
   //Button used to guess a letter of the secret word
   Button guessButton = (Button) view.findViewById(R.id.guessWordButton);
   guessButton.setOnClickListener(guessWordButtonListener);
   
   playGame();//Start a new game once the main game activity inflate
   return view; // returns the fragment's view for display
} // end method onCreateView

private SharedPreferences getSharedPreferences(String letters2,
		int modePrivate) {
	// TODO Auto-generated method stub
	return null;
}

//Method that prepares the new game
public void playGame(){
	
	//This will retrieve a random word, make sure its not the same back to back and
	//store it in the variable currWord
	String newWord = words[rand.nextInt(words.length)];
	while(newWord.equals(currWord)) newWord = words[rand.nextInt(words.length)];
	currWord = newWord;
	
	//This will create the linearlayout to display the secret word and display the
	//background as an empty gallow
	charViews = new TextView[currWord.length()];
	wordLayout.removeAllViews();
	hangManImageView.setBackgroundResource(R.drawable.hangman6);
	
	//Loop that will go through the length of the word to create enough textview to display them individually
	for (int i = 0; i < currWord.length(); i++) {
		  charViews[i] = new TextView(this.getActivity());
		  charViews[i].setText("" + currWord.charAt(i));
		 
		  charViews[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		  charViews[i].setGravity(Gravity.CENTER);
		  charViews[i].setTextColor(Color.WHITE);
		  charViews[i].setBackgroundResource(R.drawable.letter_bg);
		  
		  wordLayout.addView(charViews[i]);//add to layout
		}
			mistakes = 0; //Starting incorrect number of guesses
			numberCorrect = 0; //Starting correct number of guesses
			numberChars = currWord.length(); //Used to determine if the player guessed all the correct letters
			numberGuessLeftTextView.setText("6"); //Resets the Guesses Left to 6 when a new game starts
}


//When user press New Game button to start a new game
public OnClickListener newGameButtonListener = new OnClickListener(){
   @Override
   public void onClick(View v) 
   {

	   playGame(); //Start a new Hang Man game
	   
	   //New game toast notification stating a new game is set
	   Toast toast = Toast.makeText(getActivity(),
			   "Starting a new game. A new word is set!", 
			   Toast.LENGTH_SHORT);
	   	toast.setGravity(Gravity.CENTER, 0, 0);		
    	toast.show();
	   
   } // end method onClick
}; // end OnClickListener New Game Button

//When user press Guess button to guess a letter of the secret word
public OnClickListener guessWordButtonListener = new OnClickListener() 
{
   @Override
   public void onClick(View v) 
   {	
	   
	   if(guessWordEditText.getText().length() > 0){
		   
		   guessedLetter = guessWordEditText.getText().toString();
		   char letterChar = guessedLetter.charAt(0);
		   boolean correct = false;

			   	for(int i = 0; i < currWord.length(); i++){
			   		if(currWord.charAt(i)==letterChar){
			   			correct = true;
			   			numberCorrect++;
			   			guessWordEditText.setText(""); //Reset EditText to empty
			   			charViews[i].setTextColor(Color.BLACK); //Show correct letter				
			   }
		    
		   }//end of for statement to check if the letter is in the word
		   
		   //Checking to see if we solved the word
		   if(correct){
			   //If the word is solved, show they won
			   if (numberCorrect == numberChars ) {
				   
				   wins++;
				   AlertDialog.Builder winnerAlert = new AlertDialog.Builder(getActivity());
				   winnerAlert.setTitle("Congratulations!");
				   winnerAlert.setMessage("You solved the word: " + currWord.toUpperCase() + 
						    "\n\nWon: " + wins + "\n\nLost: " + loses + "\n\nPress OK to play again.");
				   winnerAlert.setPositiveButton("OK",
				   new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
						   HangManFragment.this.playGame();
					   	   }});
		  
				   winnerAlert.show(); //Display alert dialog when the player wins
		   
			   }
			   if (currWord == guessWordEditText.getText().toString() ) {
					
				   wins++;
				   AlertDialog.Builder winnerAlert = new AlertDialog.Builder(getActivity());
				   winnerAlert.setTitle("Congratulations!");
				   winnerAlert.setMessage("You win!\n\nYou solved the word: " + currWord.toUpperCase() + "\n\nPress OK to play again!");
				   winnerAlert.setPositiveButton("OK",
				   new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
						   HangManFragment.this.playGame();
					   	   }});
		  
				   winnerAlert.show(); //Display alert dialog when the player wins
		   
			   }
		  }
		   //If user guessed the wrong letter
		   else if(!correct){
			   mistakes++;
			   guessWordEditText.setText(""); //Clear letter entered in guessWordEditText
			   changeImage(mistakes); //Changes the hangman image reflecting how many guesses left
			   int count = 6 - mistakes; //Used to count home many guesses left 
			   String guessLeft = Integer.toString(count); //Convert mistakes to string for view
			   numberGuessLeftTextView.setText(guessLeft); // Change the amount guesses left
			   
		   }//Statement displaying when the player loses the game and used up all the guesses
		   if(mistakes == 6){
			   
			   loses++;
			   AlertDialog.Builder winnerAlert = new AlertDialog.Builder(getActivity());
			   winnerAlert.setTitle("You lose!");
			   winnerAlert.setMessage("You could not guess the word: " + currWord.toUpperCase() + 
					   "\n\nWon: " + wins + "\n\nLost: " + loses + "\n\nPress OK to play again.");
			   winnerAlert.setPositiveButton("OK",
			   new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
					   HangManFragment.this.playGame();
				   	   }});
	  
			   winnerAlert.show(); //Display alert dialog when the player loses
			   }
		   
		   
	   }//End of if statement to make sure there's a letter/word in guessWordEditText
	   
	   //Toast to inform user to enter a letter in guessWordEditText
	   else{
		   
		   Toast toast = Toast.makeText(getActivity(),
				   "Please type a letter in the text box.", 
				   Toast.LENGTH_SHORT);
		   	toast.setGravity(Gravity.CENTER, 0, 0);		
	    	toast.show();
		   	   
	   }
	            
   } // end method onClick
}; // end OnClickListener Guess Button 

//method to get the new image for a wrong guess
private void changeImage(int count) {
	 switch (count) {
	 case 1:
		 hangManImageView.setBackgroundResource(R.drawable.hangman5); //Head img
		 hangManImageView.startAnimation(shakeAnimation);
	 break;
	 case 2:
		 hangManImageView.setBackgroundResource(R.drawable.hangman4); //Body img
		 hangManImageView.startAnimation(shakeAnimation);
	 break;
	 case 3:
		 hangManImageView.setBackgroundResource(R.drawable.hangman3); //Left arm img
		 hangManImageView.startAnimation(shakeAnimation);
	 break;
	 case 4:
		 hangManImageView.setBackgroundResource(R.drawable.hangman2); //Right arm img
		 hangManImageView.startAnimation(shakeAnimation);
	 break;
	 case 5:
		 hangManImageView.setBackgroundResource(R.drawable.hangman1); //Left leg img
		 hangManImageView.startAnimation(shakeAnimation);
	 break;
	 case 6:
		 hangManImageView.setBackgroundResource(R.drawable.hangman0); //Right leg img
		 hangManImageView.startAnimation(shakeAnimation);
	 break;
	 }
	 } 

// update how many letters in the word to use based on value in SharedPreferences
public void updateLetterAmount(SharedPreferences sharedPreferences)
{	
	numberLetters = 
	         sharedPreferences.getString(MainActivity.LETTERS, null);

	}

public void sharedPreferences(String letter, String letter2) {

    SharedPreferences.Editor preferencesEditor = savedLetters.edit();
    preferencesEditor.putString(letter, letter2); // store current activity
    preferencesEditor.apply();
}
}





