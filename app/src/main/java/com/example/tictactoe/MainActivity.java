package com.example.tictactoe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void playAgain (View view)
    {
        Button resetGame = (Button) findViewById(R.id.playButton);
        TextView winnerView = (TextView) findViewById(R.id.winerView);

        resetGame.setVisibility(View.INVISIBLE);
        winnerView.setVisibility(View.INVISIBLE);

        androidx.gridlayout.widget.GridLayout grids = findViewById(R.id.gridLayout);

        for(int i = 0; i< grids.getChildCount(); i++)
        {
            ImageView resetDice = (ImageView) grids.getChildAt(i);
            resetDice.setImageDrawable(null);
        }

        activePlayer = 0;

        for(int i=0; i<9;i++)
        {
            gameState[i] = 2;
        }

        gameActive = true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 0 : red, 1 : yellow, 2 : blank
    int activePlayer = 0;

    int gameState[] = {2,2,2,2,2,2,2,2,2};

    int winningPosition [][] = {{0,1,2},{3,4,5},{6,7,8},{0,4,8},{2,4,6},{0,3,6},{1,4,7},{2,5,8}};

    boolean gameActive = true;
    boolean filled = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void diceOn(View view)
    {
        ImageView dice = (ImageView) view;
        MediaPlayer diceSound = MediaPlayer.create(this, R.raw.dice);
        MediaPlayer endSound = MediaPlayer.create(this, R.raw.end);
        MediaPlayer winSound = MediaPlayer.create(this, R.raw.win);

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Button playAgain = (Button) findViewById(R.id.playButton);
        TextView winnerView = (TextView) findViewById(R.id.winerView);

        int tappedPosition = Integer.parseInt(dice.getTag().toString());

        if (gameState[tappedPosition] == 2 && gameActive) {

            dice.setTranslationY(-1500);

            gameState[tappedPosition] = activePlayer;

            if (activePlayer == 0) {
                dice.setImageResource(R.drawable.red);
                activePlayer = 1;
            } else {
                dice.setImageResource(R.drawable.blue);
                activePlayer = 0;
            }


            dice.animate().translationYBy(1500).rotation(900).setDuration(500);
            diceSound.setVolume(2000, 2000);
            diceSound.start();
            vibe.vibrate(70);


            for (int winning[] : winningPosition) {
                if (gameState[winning[0]] == gameState[winning[1]] && gameState[winning[1]] == gameState[winning[2]] && gameState[winning[0]] != 2) {

                    gameActive = false;
                    String winner = "";

                    if (activePlayer == 1) {
                        winner = "Red";
                    } else {
                        winner = "Blue";
                    }



                    winnerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    winnerView.setText(winner + " has won!");
                    winSound.setVolume(100, 100);
                    winSound.start();

                    playAgain.setVisibility(View.VISIBLE);
                    winnerView.setVisibility(View.VISIBLE);

                }
            }

        } else if (gameActive == false) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show();
            endSound.setVolume(100, 100);
            endSound.start();
            vibe.vibrate(100);
        }

        //----Check if all the slots are filled.
        for (int j=0; j<gameState.length; j++)
        {
            if (gameState[j]==2)
            {
                filled = false;
                Log.i("empty location ", ""+gameState[j]);
                break;
            }

            else filled = true;
        }

        if(filled && gameActive)
        {
            Toast.makeText(this, "No More Moves.", Toast.LENGTH_LONG).show();
            endSound.start();
            playAgain(view);
        }

    }
}