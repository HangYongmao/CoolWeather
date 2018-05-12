package cn.edu.sicnu.coolweather.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;


public class GameView extends GridLayout {

    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<>();
    private static GameView gameView = null;

    public GameView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        gameView = this;
        initGameView();
    }

    public GameView(Context context, AttributeSet attr) {
        super(context, attr);
        gameView = this;
        initGameView();
    }

    public GameView(Context context) {
        super(context);
        gameView = this;
        initGameView();
    }

    public static GameView getGameView() {
        return gameView;
    }

    private void initGameView() {
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new OnTouchListener() {

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                Log.d("GameView", "left");
                                swipeLeft();
                            } else if (offsetX > 5) {
                                Log.d("GameView", "right");
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                Log.d("GameView", "up");
                                swipeUp();
                            } else if (offsetY > 5) {
                                Log.d("GameView", "down");
                                swipeDown();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int cardWidth = (Math.min(w, h)) / 4;
        addCards(cardWidth, cardWidth);
        startGame();
    }

    private void addCards(int cardWidth, int CardHeight) {

        Card card;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                card = new Card(getContext());
                card.setNum(0);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(cardWidth, CardHeight);
                lp.setMargins(10, 10, 10, 10);
                addView(card, lp);
                cardsMap[x][y] = card;
            }
        }
    }

    public void startGame() {

        Game2048Activity.getGameActivity().clearScore();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        addRandom();
        addRandom();
    }

    private void addRandom() {

        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    private void swipeLeft() {

        boolean marge = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;//多遍历一次
                            marge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            Game2048Activity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            marge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (marge) {
            addRandom();
            checkComplete();
        }
    }

    private void swipeRight() {

        boolean marge = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;//多遍历一次
                            marge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            Game2048Activity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            marge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (marge) {
            addRandom();
            checkComplete();
        }
    }

    private void swipeUp() {

        boolean marge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;//多遍历一次
                            marge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            Game2048Activity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            marge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (marge) {
            addRandom();
            checkComplete();
        }
    }

    private void swipeDown() {

        boolean marge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;//多遍历一次
                            marge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            Game2048Activity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            marge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (marge) {
            addRandom();
            checkComplete();
        }
    }

    private void checkComplete() {

        boolean complete = true;

        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if ((cardsMap[x][y].getNum() == 0) ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {
                    complete = false;
                    break ALL;
                }
            }
        }
        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startGame();
                }
            }).setCancelable(false).show();
        }
    }
}
