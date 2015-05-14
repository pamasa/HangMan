package com.example.hangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String LOGININFO = "loginInfo";
	private SharedPreferences userLoginInfo;
	private EditText  usernameInput=null;
	private EditText  passwordInput=null;
	private String usernameCheck, passwordCheck;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
      setContentView(R.layout.login_activity_main);
      
      //Shared preferences for login info
      userLoginInfo = getSharedPreferences(LOGININFO, MODE_PRIVATE);
      
      usernameInput = (EditText)findViewById(R.id.usernameEditText);
      passwordInput = (EditText)findViewById(R.id.passwordEditText);
      
      //Login button listener
      Button loginButton = (Button)findViewById(R.id.loginButton);
      loginButton.setOnClickListener(loginButtonListener);
      
      //Register button listener 
      Button registerButton = (Button)findViewById(R.id.registerButton);
      registerButton.setOnClickListener(registerButtonListener);
   }

   public OnClickListener loginButtonListener = new OnClickListener() 
   {
      @Override
      public void onClick(View v)
      {
    	  //Check to see if the user input a name and password
    	  if(usernameInput.getText().toString().length() > 0 && 
    	    passwordInput.getText().toString().length() > 0)
    	  {
    		  usernameCheck = usernameInput.getText().toString();
    		  passwordCheck = passwordInput.getText().toString();
    		  
    		  //Check if they are already registered
    		  if(userLoginInfo.contains(usernameCheck) && userLoginInfo.contains(passwordCheck))
    		  {
    			  Toast toast = Toast.makeText(LoginActivity.this,
   					   "You are now logged in", 
   					   Toast.LENGTH_SHORT);
   		    		   toast.setGravity(Gravity.CENTER, 0, 0);		
   		    		   toast.show();
    			  Intent i = new Intent(getApplicationContext(), MainActivity.class);
                  startActivity(i);
    		  }
    		  //Check to see if the username is not registered
    		  else if(!userLoginInfo.contains(usernameCheck))
    		  {
    			  		Toast toast = Toast.makeText(LoginActivity.this,
    					   "Please enter a correct username", 
    					   Toast.LENGTH_SHORT);
    		    		   toast.setGravity(Gravity.CENTER, 0, 0);		
    		    		   toast.show();
    			  
    		  }//Check to see if the password is correct
    		  else if(!userLoginInfo.contains(passwordCheck))
    		  {
    			  		Toast toast = Toast.makeText(LoginActivity.this,
    					   "Please enter a correct password", 
    					   Toast.LENGTH_SHORT);
    		    		   toast.setGravity(Gravity.CENTER, 0, 0);		
    		    		   toast.show();
    		  }
    		  
    	  }	
    	  else
    	  {	
    		  //Toast stating the user need to input both the username and password
    		  Toast toast = Toast.makeText(LoginActivity.this,
			   "Please enter a user name and password!", 
			   Toast.LENGTH_SHORT);
    		  toast.setGravity(Gravity.CENTER, 0, 0);		
    		  toast.show();	


    	  }
      }
   };
   
   public OnClickListener registerButtonListener = new OnClickListener() 
   {
      @Override
      public void onClick(View v)
      {
    	  //Check to see if the user input a name and password
    	  if(usernameInput.getText().toString().length() > 0 && 
    		passwordInput.getText().toString().length() > 0)
    	  {
    		  
    		  usernameCheck = usernameInput.getText().toString();
  			  passwordCheck = passwordInput.getText().toString();
  			  
  			  //Check to see if the username and password is not registered yet, if not save to sharedprefences
  			  if(!userLoginInfo.contains(usernameCheck) && !userLoginInfo.contains(passwordCheck)){
  			
  	  			  sharedPreferences(usernameInput.getText().toString(), passwordInput.getText().toString());
  	  			  Toast toast = Toast.makeText(LoginActivity.this,
  	  					"You are now registered", 
  	  					Toast.LENGTH_SHORT);
  	  	    		  	toast.setGravity(Gravity.CENTER, 0, 0);		
  	  	    		    toast.show();
  	  			  
  	    		   Intent i = new Intent(getApplicationContext(), MainActivity.class);
  	              startActivity(i); 
  			  }
  			  else{
  				  //Toast stating that the user is already registered
  				  Toast toast = Toast.makeText(LoginActivity.this,
  						"You are already registered. Please login.", 
    	  			    Toast.LENGTH_SHORT);
    	  	    	    toast.setGravity(Gravity.CENTER, 0, 0);		
    	  	    	    toast.show();
  			  }
    	  }	
    	  else
    	  {	
    		//Toast stating the user need to input both the username and password
    		  Toast toast = Toast.makeText(LoginActivity.this,
			   "Please enter a user name and password!", 
			   Toast.LENGTH_SHORT);
    		  toast.setGravity(Gravity.CENTER, 0, 0);		
    		  toast.show();	
    	  }
      }
   };
   
   //Sharepreferences to save the user login info
   public void sharedPreferences(String name, String pw) {

	      SharedPreferences.Editor preferencesEditor = userLoginInfo.edit();
	      preferencesEditor.putString(name, pw); // store current activity
	      preferencesEditor.apply();
	}
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

}//End of Login Activity class
