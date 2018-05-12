package cn.edu.sicnu.coolweather.game2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.sicnu.coolweather.R;

public class Game2048Activity extends AppCompatActivity {

    private TextView tvScore;
    private static Game2048Activity gameActivity = null;
    private int score;

    public Game2048Activity() {
        gameActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScore = (TextView) findViewById(R.id.tvScore);
        Button new_game = (Button) findViewById(R.id.new_game);
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameView.getGameView().startGame();
            }
        });
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();
    }

    public static Game2048Activity getGameActivity() {
        return gameActivity;
    }
}
