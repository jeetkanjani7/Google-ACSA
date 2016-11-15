package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String TAG = "GhostActivity";
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private boolean firstTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            //dictionary = new SimpleDictionary(inputStream);
            dictionary = new FastDictionary(inputStream);

            Log.i(TAG,"OnCreate() : "+dictionary);

        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }
    public void challengePlayer(View view){
        TextView ghostText  =  (TextView) findViewById(R.id.ghostText);
        String fragment = ghostText.getText().toString();
        Log.i(TAG," challengePlayer() "+" fragment : "+fragment);
        TextView label = (TextView) findViewById(R.id.gameStatus);

        Boolean flag = dictionary.isWord(fragment);
        Log.i(TAG," challenge "+fragment+" ? "+flag);
        String word = dictionary.getAnyWordStartingWith(fragment, firstTurn);
        if( flag  || word==null){
            label.setText("You Won");
            Toast.makeText(this,fragment,Toast.LENGTH_LONG).show();
            label.setTextColor(Color.GREEN);
            return;
        }
            ghostText.setText(word);
            label.setText("Computer Won");
            Toast.makeText(this,fragment,Toast.LENGTH_LONG).show();
            label.setTextColor(Color.RED);
        Button challenge = (Button)findViewById(R.id.challengePlayer);
        challenge.setEnabled(false);

      /*
        if(fragment.length()<4){
            Toast.makeText(this,"can not Challenge, fragment length : "+fragment.length(),Toast.LENGTH_SHORT).show();
            Log.i(TAG, "can not Challenge, fragment length : " + fragment.length());
            return;
        }
      * */

        //winnerHandler();
    }
    public void resetGame(View view){
        TextView ghostText  =  (TextView) findViewById(R.id.ghostText);
        ghostText.setText("");
        Log.i(TAG, "resetGame()");
        onStart(view);
    }
    @Override
    public boolean  onKeyUp(int keyCode, KeyEvent event){
        TextView ghostText  =  (TextView) findViewById(R.id.ghostText);
        String fragment = ghostText.getText().toString();

        Log.i(TAG, " fragment : " + fragment + "onKeyUp() keyCode : " + keyCode);

        //the key that the user pressed is not a letter, default to returning the value of super.onKeyUp()
        if(keyCode<KeyEvent.KEYCODE_A || KeyEvent.KEYCODE_Z<keyCode) {
            Log.i(TAG,"Invalid Character for fragment");
            return true;
        }
        char character = (char)(keyCode-KeyEvent.KEYCODE_A+'a');
        Log.i(TAG, "Pressed Keyword " + character+" fragment : "+fragment+"  --> ");
        fragment=fragment.concat(Character.toString(character));
        Log.i(TAG,fragment);
        ghostText.setText(fragment);

        computerTurn();
        return super.onKeyUp(keyCode, event);
    }
    public boolean onStart(View view) {

        userTurn = random.nextBoolean();
        firstTurn = userTurn;
        //set current fragment to empty
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");


        Button challenge = (Button)findViewById(R.id.challengePlayer);
        challenge.setEnabled(true);

        //on empty prefix, first turn of computer
        String word = dictionary.startingFragment();

        String fragment = word.substring(0, 4);
        //add character
        Log.i(TAG,"current word : "+word+ " fragment : "+fragment);

        //set current fragment
        TextView fragementText = (TextView)findViewById(R.id.ghostText);
        fragementText.setText(fragment);

        //show label
        TextView label = (TextView) findViewById(R.id.gameStatus);
        label.setTextColor(Color.BLACK);
        if (userTurn) {
            label.setText(USER_TURN);
            Log.i(TAG,"onStart() USER_TURN ");
        } else {
            label.setText(COMPUTER_TURN);
            Log.i(TAG, "onStart() COMPUTER_TURN ");
           computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView ghostText  =  (TextView) findViewById(R.id.ghostText);
        String fragment = ghostText.getText().toString();
        Log.i(TAG, "computerTurn :" + fragment);
        TextView label = (TextView) findViewById(R.id.gameStatus);

        //Check if the fragment is a valid word with at least 4 characters.
        // If so declare victory by updating the game status
        if(fragment.length()>=4 && dictionary.isWord(fragment)){
            label.setTextColor(Color.RED);
            Toast.makeText(this,fragment,Toast.LENGTH_LONG).show();
            label.setText("Computer Won");
            // winnerHandler();
            return;
        }

        String word = dictionary.getAnyWordStartingWith(fragment, firstTurn);
        if(word==null){
            Toast.makeText(this,"Don't Try Bluff Computer",Toast.LENGTH_SHORT).show();

            Log.i(TAG, "Don't Try Bluff Computer\nComputer Won :" + word + " " + fragment);
            label.setTextColor(Color.RED);
            label.setText("Computer Won");
            //winnerHandler();
            return;
        }

        //add character
        fragment = fragment+word.charAt(fragment.length());
        Log.i(TAG,"current Word :"+word+" fragment : "+fragment);

        //set current fragment
        TextView fragementText = (TextView)findViewById(R.id.ghostText);
        fragementText.setText(fragment);

        // Do computer turn stuff then make it the user's turn again

        userTurn = true;
        label.setText(USER_TURN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
