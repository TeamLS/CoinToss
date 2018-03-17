package com.example.tasosxak.cointoss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String HIGH_SCORE = "highscore";
    private static final String SCORE = "score";
    private static final String HIST = "hist";

    private ImageView coinImage;
    private TextView scoreText;
    private TextView highscoreText;
    private TextView hist;

    Random r;
    int coinSide;
    int score;
    int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = new Random();
        coinImage = (ImageView) findViewById(R.id.coin);
        scoreText = (TextView) findViewById(R.id.score);
        highscoreText = (TextView) findViewById(R.id.highscore);
        hist = (TextView) findViewById(R.id.hist);

        if(savedInstanceState != null){

            highscoreText.setText(savedInstanceState.getCharSequence(HIGH_SCORE));
            scoreText.setText(savedInstanceState.getCharSequence(SCORE));
            hist.setText(savedInstanceState.getCharSequence(HIST));

            highscore = Integer.valueOf(highscoreText.getText().toString());
            score = Integer.valueOf(scoreText.getText().toString());
        }

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(HIGH_SCORE, highscoreText.getText());
        outState.putCharSequence(SCORE,scoreText.getText());
        outState.putCharSequence(HIST,hist.getText());
    }

    public void flipCoin(View v){

            int id = ((Button) v).getId();
            coinSide =r.nextInt(2);

            if(coinSide == 0){

                coinImage.setImageResource(R.drawable.tails);

                if( id == R.id.head) {

                    if( score > highscore) {

                        highscore = score;
                        highscoreText.setText(String.valueOf(highscore));

                    }

                    score = 0;
                    scoreText.setText("0");
                    hist.setText("");

                }
                else {

                    score++;
                    scoreText.setText(String.valueOf(score));
                    hist.append("T");
                }

            }
            else {

                coinImage.setImageResource(R.drawable.head);

                if( id == R.id.tails){

                    if( score > highscore) {

                        highscore = score;
                        highscoreText.setText(String.valueOf(highscore));
                    }

                    score = 0;
                    scoreText.setText("0");
                    hist.setText("");
                }
                else {

                    score++;
                    scoreText.setText(String.valueOf(score));
                    hist.append("H");
                }
            }

            RotateAnimation rotate =  new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
            rotate.setDuration(100);

            coinImage.startAnimation(rotate);

    }
}
