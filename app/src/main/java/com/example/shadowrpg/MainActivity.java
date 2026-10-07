package com.shadowrpg;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }

    public static class GameView extends View {

        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private int slimeHP = 100;
        private int bossHP = 500;

        private boolean bossAppeared = false;
        private boolean ending = false;

        GameView(Context context) {
            super(context);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        private void drawText(
                Canvas canvas,
                String text,
                float x,
                float y,
                float size
        ) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(size);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, x, y, paint);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            int width = getWidth();
            int height = getHeight();

            canvas.drawColor(Color.rgb(8, 8, 15));

            // 엔딩
            if (ending) {
                drawText(
                        canvas,
                        "THE JOURNEY HAS ENDED",
                        width / 2,
                        height / 2 - 30,
                        32
                );

                drawText(
                        canvas,
                        "SHADOW RPG",
                        width / 2,
                        height / 2 + 40,
                        42
                );

                return;
            }

            // 바닥
            paint.setColor(Color.rgb(25, 25, 35));
            canvas.drawRect(
                    0,
                    height - 120,
                    width,
                    height,
                    paint
            );

            // 플레이어
            paint.setColor(Color.CYAN);
            canvas.drawCircle(
                    180,
                    height - 170,
                    35,
                    paint
            );

            drawText(
                    canvas,
                    "HERO",
                    180,
                    height - 115,
                    18
            );

            if (!bossAppeared) {

                // 슬라임
                drawText(
                        canvas,
                        "SHADOW SLIME",
                        width / 2,
                        60,
                        32
                );

                paint.setColor(Color.rgb(70, 220, 100));

                canvas.drawOval(
                        width - 250,
                        height / 2 - 70,
                        width - 110,
                        height / 2 + 70,
                        paint
                );

                drawText(
                        canvas,
                        "SLIME",
                        width - 180,
                        height / 2 + 8,
                        20
                );

                drawHPBar(
                        canvas,
                        70,
                        90,
                        350,
                        slimeHP,
                        100
                );

            } else {

                // 보스
                drawText(
                        canvas,
                        "FOREST WARDEN",
                        width / 2,
                        60,
                        32
                );

                paint.setColor(Color.rgb(145, 80, 210));

                canvas.drawCircle(
                        width - 180,
                        height / 2,
                        100,
                        paint
                );

                drawText(
                        canvas,
                        "WARDEN",
                        width - 180,
                        height / 2 + 8,
                        20
                );

                drawHPBar(
                        canvas,
                        70,
                        90,
                        500,
                        bossHP,
                        500
                );
            }

            drawText(
                    canvas,
                    "TAP THE ENEMY",
                    width / 2,
                    height - 45,
                    24
            );
        }

        private void drawHPBar(
                Canvas canvas,
                float x,
                float y,
                float width,
                int hp,
                int maxHP
        ) {

            paint.setColor(Color.DKGRAY);

            canvas.drawRect(
                    x,
                    y,
                    x + width,
                    y + 25,
                    paint
            );

            float ratio = hp / (float) maxHP;

            if (ratio < 0) {
                ratio = 0;
            }

            paint.setColor(Color.rgb(60, 220, 90));

            canvas.drawRect(
                    x,
                    y,
                    x + width * ratio,
                    y + 25,
                    paint
            );
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (event.getAction() != MotionEvent.ACTION_UP) {
                return true;
            }

            if (ending) {
                return true;
            }

            float x = event.getX();
            float y = event.getY();

            int width = getWidth();
            int height = getHeight();

            boolean hitEnemy =
                    x > width - 300 &&
                    y > height / 2 - 160 &&
                    y < height / 2 + 160;

            if (!hitEnemy) {
                return true;
            }

            // 슬라임
            if (!bossAppeared) {

                slimeHP -= 25;

                if (slimeHP <= 0) {

                    slimeHP = 0;

                    // 슬라임 처치 → 보스 등장
                    bossAppeared = true;
                }

            }

            // 보스
            else {

                bossHP -= 50;

                if (bossHP <= 0) {

                    bossHP = 0;

                    // 보스 처치 → 엔딩
                    ending = true;
                }
            }

            invalidate();

            return true;
        }
    }
}
