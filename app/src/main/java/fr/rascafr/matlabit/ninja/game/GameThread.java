package fr.rascafr.matlabit.ninja.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.view.SurfaceHolder;

import fr.rascafr.matlabit.ninja.game.ui.GameFragment;

public class GameThread implements Runnable {

    private final Paint scorePaint = new Paint();
    private final SurfaceHolder surfaceHolder;
    private final ProjectileManager projectileManager;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Paint linePaint = new Paint();
    private final Paint linePaintBlur = new Paint();
    private Bitmap bmpBack, can;

    private volatile ScheduledFuture<?> self;

    private final static int START_LIVES = 3;

    private int score = 0;
    private int width = 0;
    private int height= 0;
    private int lives = START_LIVES;
    private boolean isRunning = false;
    private GameFragment.OnGameOver gameOverListener;
    private SparseArrayCompat<TimedPath> paths;

    public GameThread(SurfaceHolder surfaceHolder, ProjectileManager projectileManager, Bitmap bmpBack, Bitmap can, GameFragment.OnGameOver gameOverListener) {
        this.surfaceHolder = surfaceHolder;
        this.projectileManager = projectileManager;
        this.bmpBack = bmpBack;
        this.can = can;
        this.gameOverListener = gameOverListener;
    }

    public void pauseGame() {
        isRunning = false;
    }

    public void resumeGame(int width, int height) {
        this.width = width;
        this.height = height;
        isRunning = true;
        projectileManager.setWidthAndHeight(width, height);
    }

    public void startGame(int width, int height) {
        this.width = width;
        this.isRunning = true;
        this.projectileManager.setWidthAndHeight(width, height);
        this.lives = START_LIVES;
        this.self = executor.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);

        this.scorePaint.setColor(Color.parseColor("#fcc48d"));
        this.scorePaint.setAntiAlias(true);
        this.scorePaint.setTextSize(90.0f);

        this.linePaint.setAntiAlias(true);
        this.linePaint.setColor(Color.WHITE);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeJoin(Paint.Join.ROUND);
        this.linePaint.setStrokeWidth(7.0f);

        this.linePaintBlur.set(this.linePaint);
        this.linePaintBlur.setMaskFilter(new BlurMaskFilter(9.0f, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    public void run() {
        Canvas canvas = null;
        if (isRunning) {
            try {

                if (lives <= 0) {
                    isRunning = false;
                    gameOverListener.onGameOver(score);
                    self.cancel(true);
                } else {

                    lives -= projectileManager.update();

                    if (paths != null && paths.size() > 0) {
                        List<TimedPath> allPaths = new ArrayList<TimedPath>();
                        for (int i = 0; i < paths.size(); i++) {
                            allPaths.add(paths.valueAt(i));
                        }
                        score += projectileManager.testForCollisions(allPaths);
                    }

                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        synchronized (surfaceHolder) {
                            canvas.drawARGB(255, 0, 0, 0);
                            canvas.drawBitmap(bmpBack, 0, 0, null);

                            projectileManager.draw(canvas);
                            canvas.drawText("" + score, 50, 130, scorePaint);
                            //canvas.drawText("Lives: " + lives, 0, height - 200, scorePaint);

                            for (int i=0;i<lives;i++) {
                                canvas.drawBitmap(can, width - 150 - i*150, 50, null);
                            }

                            if (paths != null) {
                                for (int i = 0; i < paths.size(); i++) {
                                    canvas.drawPath(paths.valueAt(i), linePaintBlur);
                                    canvas.drawPath(paths.valueAt(i), linePaint);

                                    if (paths.valueAt(i).getTimeDrawn() + 500 < System.currentTimeMillis()) {
                                        paths.removeAt(i);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("FruitNinja", e.getMessage());
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }

    public void updateDrawnPath(SparseArrayCompat<TimedPath> paths) {
        this.paths = paths;
    }
}