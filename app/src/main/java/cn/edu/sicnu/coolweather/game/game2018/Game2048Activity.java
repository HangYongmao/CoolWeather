package cn.edu.sicnu.coolweather.game.game2018;

import android.animation.AnimatorInflater;
import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.edu.sicnu.coolweather.R;

public class Game2048Activity extends AppCompatActivity {

    private MyLayout layout = null;
    private TextView textView = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int addScore = msg.arg1;
            int possessScore = Integer.valueOf(textView.getText().toString());
            textView.setText(String.valueOf(addScore + possessScore));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048);
        layout = (MyLayout) findViewById(R.id.id_layout);
        textView = (TextView) findViewById(R.id.id_textView);
        setAnimator();
        layout.setHandler(handler);
    }
    //设置Layout里面的View出场动画
    private void setAnimator() {
        LayoutTransition lt = new LayoutTransition();
        lt.setDuration(200);
        //设置View的出场动画
        lt.setAnimator(LayoutTransition.APPEARING, AnimatorInflater.loadAnimator(this, R.animator.appear_anim));
        layout.setLayoutTransition(lt);
    }
}
