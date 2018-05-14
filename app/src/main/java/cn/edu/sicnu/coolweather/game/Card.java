package cn.edu.sicnu.coolweather.game;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

    private int num = 0;
    private TextView label;

    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setBackgroundColor(0xffbbada0);
        label.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 10, 10);
        addView(label, lp);

        setNum(0);
    }

    private void setColor() {
        if (num <= 0) {
            label.setBackgroundColor(0xffcdceb4);
        } else if (num == 2) {
            label.setBackgroundColor(0xffeee4da);
        } else if (num == 4) {
            label.setBackgroundColor(0xffede0c8);
        } else if (num == 8) {
            label.setBackgroundColor(0xfff2b179);
        } else if (num == 16) {
            label.setBackgroundColor(0xfff59563);
        } else if (num == 32) {
            label.setBackgroundColor(0xfff67c5f);
        } else if (num == 64) {
            label.setBackgroundColor(0xfff65e3b);
        } else if (num == 128) {
            label.setBackgroundColor(0xffedcf72);
        } else if (num == 256) {
            label.setBackgroundColor(0xffedcc61);
        } else if (num == 512) {
            label.setBackgroundColor(0xffedc850);
        } else if (num == 1024) {
            label.setBackgroundColor(0xffedc53f);
        } else if (num == 2048) {
            label.setBackgroundColor(0xffedc22e);
        } else if (num >= 4096) {
            label.setBackgroundColor(0xff3c3a32);
        }

        if (num <= 4) {
            label.setTextColor(0xff3c3a32);
        } else {
            label.setTextColor(0xfff9f6f2);
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num <= 0) {
            label.setText("");
        } else {
            label.setText(num + "");
        }
        setColor();
    }

    public boolean equals(Card o) {
        return getNum() == o.getNum();
    }
}
