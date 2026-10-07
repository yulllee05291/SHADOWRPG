package com.shadowrpg;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.content.Context;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(new GameView(this));
    }

    public static class GameView extends View {

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        int slimeHP = 100;
        int bossHP = 500;

        boolean bossAppeared = false;
        boolean ending = false;

        long endingTime = 0;

        GameView(Context context) {
            super(context);
            p.setTypeface(Typeface.create("sans", Typeface.BOLD));
        }

        void text(Canvas c, String s, float x, float y, float size) {
            p.setColor(Color.WHITE);
            p.setTextSize(size);
            p.setTextAlign(Paint.Align.CENTER);
            c.drawText(s, x, y, p);
        }

        @Override
        protected void onDraw(Canvas c) {

            int w = getWidth();
            int h = getHeight();

            c.drawColor(Color.rgb(7, 7, 12));

            // =========================
            // 웅장한 엔딩
            // =========================
            if (ending) {
                drawEnding(c, w, h);
                invalidate();
                return;
            }

            // =========================
            // 바닥
            // =========================

            p.setColor(Color.rgb(25, 25, 35));
            c.drawRect(0, h - 120, w, h, p);

            // =========================
            // 플레이어
            // =========================

            p.setColor(Color.CYAN);
            c.drawCircle(220, h - 190, 35, p);

            text(c, "HERO", 220, h - 184, 16);

            // =========================
            // 적 이름
            // =========================

            if (!bossAppeared) {

                text(
                        c,
                        "SHADOW SLIME",
                        w / 2,
                        65,
                        38
                );

                // 슬라임 HP
                drawHPBar(
                        c,
                        70,
                        90,
                        350,
                        slimeHP,
                        100
                );

                // 슬라임
                p.setColor(Color.rgb(70, 220, 100));
                c.drawOval(
                        w - 250,
                        260,
                        w - 110,
                        380,
                        p
                );

                text(
                        c,
                        "SLIME",
                        w - 180,
                        330,
                        20
                );

            } else {

                text(
                        c,
                        "FOREST WARDEN",
                        w / 2,
                        65,
                        38
                );

                // 보스 HP
                drawHPBar(
                        c,
                        70,
                        90,
                        500,
                        bossHP,
                        500
                );

                // 보스
                p.setColor(Color.rgb(145, 80, 210));

                c.drawCircle(
                        w - 180,
                        320,
                        90,
                        p
                );

                text(
                        c,
                        "WARDEN",
                        w - 180,
                        326,
                        20
                );
            }

            // =========================
            // 공격 안내
            // =========================

            text(
                    c,
                    "TAP THE ENEMY TO ATTACK",
                    w / 2,
                    h - 55,
                    24
            );

            invalidate();
        }

        // HP BAR
        void drawHPBar(
                Canvas c,
                float x,
                float y,
                float width,
                int hp,
                int maxHP
        ) {

            p.setColor(Color.DKGRAY);

            c.drawRect(
                    x,
                    y,
                    x + width,
                    y + 30,
                    p
            );

            float ratio =
                    Math.max(0, hp) / (float) maxHP;

            p.setColor(Color.rgb(60, 220, 90));

            c.drawRect(
                    x,
                    y,
                    x + width * ratio,
                    y + 30,
                    p
            );
        }

        // =========================
        // 화면 터치
        // =========================

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (event.getAction() != MotionEvent.ACTION_UP)
                return true;

            if (ending)
                return true;

            float x = event.getX();
            float y = event.getY();

            int w = getWidth();

            // 슬라임 공격
            if (!bossAppeared) {

                if (
                        x > w - 300 &&
                        y > 200 &&
                        y < 450
                ) {

                    slimeHP -= 25;

                    if (slimeHP <= 0) {

                        slimeHP = 0;

                        // 슬라임 처치 후 보스 등장
                        bossAppeared = true;
                    }
                }

            }

            // 보스 공격
            else {

                if (
                        x > w - 320 &&
                        y > 180 &&
                        y < 470
                ) {

                    bossHP -= 50;

                    if (bossHP <= 0) {

                        bossHP = 0;

                        // 보스 처치
                        startEnding();
                    }
                }
            }

            invalidate();

            return true;
        }

        // =========================
        // 엔딩 시작
        // =========================

        void startEnding() {

            ending = true;
            endingTime = System.currentTimeMillis();
        }

        // =========================
        // 웅장한 엔딩
        // =========================

        void drawEnding(
                Canvas c,
                int w,
                int h
        ) {

            long elapsed =
                    System.currentTimeMillis()
                    - endingTime;

            // 점점 밝아지는 빛
            float progress =
                    Math.min(1.0f, elapsed / 3000.0f);

            int brightness =
                    (int)(30 + progress * 180);

            p.setColor(Color.rgb(
                    brightness,
                    brightness,
                    brightness
            ));

            c.drawCircle(
                    w / 2,
                    h / 2,
                    100 + progress * 400,
                    p
            );

            // 첫 번째 문구
            if (elapsed > 700) {

                text(
                        c,
                        "THE FOREST HAS FALLEN SILENT",
                        w / 2,
                        h / 2 - 70,
                        30
                );
            }

            // 두 번째 문구
            if (elapsed > 1800) {

                text(
                        c,
                        "BUT THE SHADOW AWAKENS...",
                        w / 2,
                        h / 2,
                        36
                );
            }

            // 게임 타이틀
            if (elapsed > 3200) {

                text(
                        c,
                        "SHADOW RPG",
                        w / 2,
                        h / 2 + 100,
                        60
                );
            }

            // 7초 후 처음으로
            if (elapsed > 7000) {

                ending = false;
                bossAppeared = false;

                slimeHP = 100;
                bossHP = 500;
            }
        }
    }
}
