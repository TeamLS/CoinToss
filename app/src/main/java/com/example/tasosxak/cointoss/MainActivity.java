package com.example.tasosxak.cointoss;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String HIGH_SCORE = "high score";
    private static final String COIN_SIDE = "coin side";
    private static final String SCORE = "score";
    private static final String HIST = "hist";

    private ImageView coinImage;
    private TextView scoreText;
    private TextView highScoreText;
    private TextView hist;
    private Button headsButton;
    private Button tailsButton;

    private Random r;
    private int coinSide;
    private int score;
    private int highScore;
    private MediaPlayer mp;
    private int curSide = R.drawable.heads;

    private String filename = "highScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r = new Random();
        coinImage = (ImageView) findViewById(R.id.coin);
        scoreText = (TextView) findViewById(R.id.score);
        highScoreText = (TextView) findViewById(R.id.highScore);
        hist = (TextView) findViewById(R.id.hist);

        headsButton = (Button) findViewById(R.id.heads);
        tailsButton = (Button) findViewById(R.id.tails);


        try {

            // Try to read from the highScore file

            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            inputStream.close();

            highScore = Integer.parseInt(total.toString());
            highScoreText.setText(String.valueOf(highScore));


        } catch (Exception e) {

            // The highScore file doesn't exist, or doesn't contain an integer, so write a new highScore file that has 0.

            System.err.println(e.toString());
            highScore = 0;

            try {
                FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write("0".getBytes());
                outputStream.close();
            } catch (Exception e2) {
                e.printStackTrace();
            }

        }


        // Restore all values and images after rotate

        if (savedInstanceState != null) {

            coinImage.setImageResource(Integer.parseInt(savedInstanceState.getCharSequence(COIN_SIDE).toString()));
            highScoreText.setText(savedInstanceState.getCharSequence(HIGH_SCORE));
            scoreText.setText(savedInstanceState.getCharSequence(SCORE));
            hist.setText(savedInstanceState.getCharSequence(HIST));

            highScore = Integer.valueOf(highScoreText.getText().toString());
            score = Integer.valueOf(scoreText.getText().toString());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(COIN_SIDE, String.valueOf(curSide));
        outState.putCharSequence(HIGH_SCORE, highScoreText.getText());
        outState.putCharSequence(SCORE, scoreText.getText());
        outState.putCharSequence(HIST, hist.getText());
    }



    @Override
    public void onDestroy() {

        // Write the new high score to the highScore file

        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(highScore).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();

    }

    private void newHighScore() {


        highScore = score;
        highScoreText.setText(String.valueOf(highScore));

        // Show Toast for New High Score

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

        animation.setDuration(110);
        animation.setInterpolator(new LinearInterpolator());


        coinImage.startAnimation(animation);


        setButtonsEnabled(false);

        return animation.getDuration() * (animation.getRepeatCount() + 1);
    }

    public void flipCoin(View v) {


        final int buttonId = ((Button) v).getId();
        coinSide = r.nextInt(2);

        stopPlaying();
        mp = MediaPlayer.create(this, R.raw.coin_flip);
        mp.start();

        if (coinSide == 0) {  // We have Tails

            boolean stayTheSame = (curSide == R.drawable.tails);
            long timeOfAnimation = animateCoin(stayTheSame);
            curSide = R.drawable.tails;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (buttonId == R.id.heads) {  // User guessed Heads (WRONG)

                        if (score > highScore) {
                            newHighScore();
                        }

                        score = 0;
                        scoreText.setText("0");
                        hist.setText("");

                    } else {  // User guessed Tails (CORRECT)

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


                    if (buttonId == R.id.tails) {  // User guessed Tails (WRONG)

                        if (score > highScore) {
                            newHighScore();
                        }

                        score = 0;
                        scoreText.setText("0");
                        hist.setText("");

                    } else {  // User guessed Heads (CORRECT)

                        score++;
                        scoreText.setText(String.valueOf(score));
                        hist.append(getResources().getString(R.string.heads_first_letter));

                    }


                    setButtonsEnabled(true);

                }

            }, timeOfAnimation + 100);

        }

    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
