package com.example.ramesh_chandra.scarnedice;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Integer userOverAllScore = 0;
    private Integer userTurnScore = 0;
    private Integer computerOverAllScore = 0;
    private Integer computerTurnScore = 0;
    private Boolean currentTurn = false; //user turn
    private Handler handler = new Handler();
    private Boolean killMe = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i("onCreate", "Start playing Game");
        resetUI();
    }
    public void rollDice(View view){
        Log.i("rollDice()", "Roll Dice");
        if(userOverAllScore>=100 || computerOverAllScore>=100){
            declareWinner();
            return;
        }
        playTurn();//flip dice
    }
    public  void holdTurn(View view){
        Log.i("holdTurn()", "holdTurn");
        updateScore();//update score to UI
        Toast.makeText(this,"Computer",Toast.LENGTH_SHORT).show();
        TextView currentPlayer = (TextView ) findViewById(R.id.currentPlayer);
        TextView currentScore = (TextView )findViewById(R.id.currentTurnScore);
        currentPlayer.setText("Computer");
        currentScore.setText("0");
        startRepeatingTask();//computer play its turn
    }
    public void resetGame(View view){
        resetUI();//reset all variables
    }
    private void resetUI(){
        userTurnScore = 0;
        userOverAllScore = 0;
        computerTurnScore = 0;
        computerOverAllScore = 0;
        currentTurn = false; //user turn
        killMe = false;
        TextView winner = (TextView)findViewById(R.id.winner);
        winner.setVisibility(View.GONE);
        Button rollDice = (Button)findViewById(R.id.rollDice);
        Button holdDice = (Button)findViewById(R.id.holdTurn);
        rollDice.setEnabled(true);
        holdDice.setEnabled(true);
        updateScoreUI(userOverAllScore, computerOverAllScore);//update score ui
        displayDiceImage(1);//display default image
        stopRepeatingTask();//stop computer turn
    }
    public Runnable computerTurn = new Runnable() {
        @Override
        public void run() {

            if(killMe) return;
            if(userOverAllScore>=100 || computerOverAllScore>=100){
                declareWinner();
                return;
            }
            playTurn();
            handler.postDelayed(computerTurn,2000); // 1 seconds
        }
    };
    void startRepeatingTask() {
        Log.i("startRepeatingTask()","Action : startRepeatingTask()");
        killMe = false;
        Button rollDice = (Button)findViewById(R.id.rollDice);
        Button holdDice = (Button)findViewById(R.id.holdTurn);
        rollDice.setEnabled(false);
        holdDice.setEnabled(false);
        computerTurn.run();
    }
    void stopRepeatingTask(){
        Log.i("stopRepeatingTask()","Action : stopRepeatingTask()");
        killMe = true;
        Button rollDice = (Button)findViewById(R.id.rollDice);
        Button holdDice = (Button)findViewById(R.id.holdTurn);
        rollDice.setEnabled(true);
        holdDice.setEnabled(true);
    }
    private void declareWinner(){
        Log.i("declareWinner","Declaring winner");
        if(userOverAllScore>=100){
            Button rollDice = (Button)findViewById(R.id.rollDice);
            Button holdDice = (Button)findViewById(R.id.holdTurn);
            rollDice.setEnabled(false);
            holdDice.setEnabled(false);
            TextView winner = (TextView) findViewById(R.id.winner);
            winner.setText("Winner : You");
            winner.setVisibility(View.VISIBLE);
        }else if(computerOverAllScore>=100){

            Button rollDice = (Button)findViewById(R.id.rollDice);
            Button holdDice = (Button)findViewById(R.id.holdTurn);
            rollDice.setEnabled(false);
            holdDice.setEnabled(false);
            TextView winner = (TextView) findViewById(R.id.winner);
            winner.setText("Winner : Computer");
            winner.setEnabled(true);
        }

    }
    private void playTurn(){
        Integer currTurnScore = randInt(1, 6);
        displayDiceImage(currTurnScore);
        if (currTurnScore.intValue() == 1) {
            if (!currentTurn) { //player
                Toast.makeText(this,"Computer",Toast.LENGTH_SHORT).show();
                TextView currentPlayer = (TextView ) findViewById(R.id.currentPlayer);
                TextView currentScore = (TextView )findViewById(R.id.currentTurnScore);
                currentPlayer.setText("Computer");
                currentScore.setText("0");
                userTurnScore = 0;
                currentTurn = true;
                startRepeatingTask();
            } else { //computer
                Toast.makeText(this,"You",Toast.LENGTH_SHORT).show();
                TextView currentPlayer = (TextView ) findViewById(R.id.currentPlayer);
                TextView currentScore = (TextView )findViewById(R.id.currentTurnScore);
                currentPlayer.setText("You");
                currentScore.setText("0");
                computerTurnScore = 0;
                currentTurn = false;
                stopRepeatingTask();
            }
        } else {
            if (!currentTurn) { //player
                userTurnScore += currTurnScore;
                TextView currentScore = (TextView )findViewById(R.id.currentTurnScore);
                currentScore.setText(userTurnScore.toString());
            } else { //computer
                computerTurnScore += currTurnScore;
                TextView currentScore = (TextView )findViewById(R.id.currentTurnScore);
                currentScore.setText(computerTurnScore.toString());
                if(computerTurnScore>=20){
                    updateScore();
                    Toast.makeText(this,"You",Toast.LENGTH_SHORT).show();
                    TextView currentPlayer = (TextView ) findViewById(R.id.currentPlayer);
                    currentPlayer.setText("You");
                    currentScore.setText("0");

                    stopRepeatingTask();

                }
            }
        }
    }
    private void updateScore(){
        if(!currentTurn){
            userOverAllScore += userTurnScore;
            currentTurn = true;
        }else{
            computerOverAllScore += computerTurnScore;
            currentTurn  = false;
        }
        userTurnScore = 0;
        computerTurnScore = 0;
        updateScoreUI(userOverAllScore, computerOverAllScore);
    }
    private int randInt(int min, int max) {
        Random rand  = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    private void updateScoreUI(Integer userScore, Integer computerScore){
        Log.i("updateScore()", "User : " + userScore.toString() + "\t computer : " + computerScore.toString());

        TextView userScoreText = (TextView)findViewById(R.id.userScore);
        userScoreText.setText(userScore.toString());

        TextView computerScoreText = (TextView)findViewById(R.id.computerScore);
        computerScoreText.setText(computerScore.toString());
    }

    private void displayDiceImage(Integer value){
        Log.i("displayDiceImage()","Value : "+value.toString());
        ImageView diceImage = (ImageView)findViewById(R.id.diceImage);
        switch (value){
            case 1:               diceImage.setImageResource(R.drawable.dice1);
                break;
            case 2:                diceImage.setImageResource(R.drawable.dice2);
                break;
            case 3:                diceImage.setImageResource(R.drawable.dice3);
                break;
            case 4:                diceImage.setImageResource(R.drawable.dice4);
                break;
            case 5:                diceImage.setImageResource(R.drawable.dice5);
                break;
            case 6:                diceImage.setImageResource(R.drawable.dice6);
                break;
            default:                diceImage.setImageResource(R.drawable.dice1);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }
}
