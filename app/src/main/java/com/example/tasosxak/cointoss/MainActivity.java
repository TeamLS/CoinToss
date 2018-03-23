package com.example.tasosxak.cointoss;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String HIGH_SCORE = "high score";
    private static final String COIN_SIDE = "coin side";
    private static final String SCORE = "score";
    private static final String HIST = "hist";

    private ImageView coinImage;
    private TextView scoreText;
    private TextView highscoreText;
    private TextView hist;
    private Button headsButton;
    private Button tailsButton;

    Random r;
    int coinSide;
    int score;
    int highscore;

    int curSide = R.drawable.heads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = new Random();
        coinImage = (ImageView) findViewById(R.id.coin);
        scoreText = (TextView) findViewById(R.id.score);
        highscoreText = (TextView) findViewById(R.id.highScore);
        hist = (TextView) findViewById(R.id.hist);

        headsButton = (Button) findViewById(R.id.heads);
        tailsButton = (Button) findViewById(R.id.tails);

        if (savedInstanceState != null) {

            coinImage.setImageResource(Integer.parseInt(savedInstanceState.getCharSequence(COIN_SIDE).toString()));
            highscoreText.setText(savedInstanceState.getCharSequence(HIGH_SCORE));
            scoreText.setText(savedInstanceState.getCharSequence(SCORE));
            hist.setText(savedInstanceState.getCharSequence(HIST));

            highscore = Integer.valueOf(highscoreText.getText().toString());
            score = Integer.valueOf(scoreText.getText().toString());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(COIN_SIDE, String.valueOf(curSide));
        outState.putCharSequence(HIGH_SCORE, highscoreText.getText());
        outState.putCharSequence(SCORE, scoreText.getText());
        outState.putCharSequence(HIST, hist.getText());
    }

    private void newHighscore() {


        highscore = score;
        highscoreText.setText(String.valueOf(highscore));

        String text = getResources().getString(R.string.new_highscore);
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();


    }

    private void setButtonsEnabled(boolean enabled) {
        headsButton.setEnabled(enabled);
        tailsButton.setEnabled(enabled);
    }

    private long animateCoin(boolean stayTheSame) {

        Rotate3dAnimation animation;

        if (curSide == R.drawable.heads) {
            animation = new Rotate3dAnimation(coinImage, R.drawable.heads, R.drawable.tails, 0, 180, 0, 0, 0, 0);
        } else {
            animation = new Rotate3dAnimation(coinImage, R.drawable.tails, R.drawable.heads, 0, 180, 0, 0, 0, 0);
        }
        if (stayTheSame) {
            animation.setRepeatCount(7);
        } else {
            animation.setRepeatCount(8);
        }

        animation.setDuration(130);
        animation.setInterpolator(new LinearInterpolator());

        coinImage.startAnimation(animation);

        setButtonsEnabled(false);

        return animation.getDuration() * (animation.getRepeatCount()+1);
    }

    public void flipCoin(View v) {


        final int buttonId = ((Button) v).getId();
        coinSide = r.nextInt(2);

        if (coinSide == 0) {  // We have Tails

            boolean stayTheSame = (curSide == R.drawable.tails);
            long timeOfAnimation = animateCoin(stayTheSame);
            curSide = R.drawable.tails;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (buttonId == R.id.heads) {  // User guessed Heads

                        if (score > highscore) {
                            newHighscore();
                        }

                        score = 0;
                        scoreText.setText("0");
                        hist.setText("");

                    } else {  // User guessed Tails

                        score++;
                        scoreText.setText(String.valueOf(score));
                        hist.append(getResources().getString(R.string.tails_first_letter));

                    }

                    setButtonsEnabled(true);

                }


            }, timeOfAnimation + 100);


        } else {  // We have Heads

            boolean stayTheSame = (curSide == R.drawable.heads);
            long timeOfAnimation = animateCoin(stayTheSame);
            curSide = R.drawable.heads;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (buttonId == R.id.tails) {  // User guessed Tails

                        if (score > highscore) {
                            newHighscore();
                        }

                        score = 0;
                        scoreText.setText("0");
                        hist.setText("");

                    } else {  // User guessed Heads

                        score++;
                        scoreText.setText(String.valueOf(score));
                        hist.append(getResources().getString(R.string.heads_first_letter));

                    }


                    setButtonsEnabled(true);

                }

            }, timeOfAnimation + 100);

        }

    }
}
