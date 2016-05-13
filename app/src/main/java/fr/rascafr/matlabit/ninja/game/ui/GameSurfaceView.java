package fr.rascafr.matlabit.ninja.game.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import fr.rascafr.matlabit.R;
import fr.rascafr.matlabit.ninja.game.FruitProjectileManager;
import fr.rascafr.matlabit.ninja.game.GameThread;
import fr.rascafr.matlabit.ninja.game.ProjectileManager;
import fr.rascafr.matlabit.ninja.game.TimedPath;

public class GameSurfaceView extends SurfaceView implements OnTouchListener, SurfaceHolder.Callback {

    private GameThread gameThread;
    private ProjectileManager projectileManager;
    private GameFragment.OnGameOver gameOverListener;
    private boolean isGameInitialised = false;
    private final SparseArrayCompat<TimedPath> paths = new SparseArrayCompat<TimedPath>();
    private Context context;

    public GameSurfaceView(Context context) {
        super(context);
        this.context = context;

        initialise();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialise();
    }

    public void setGameOverListener(GameFragment.OnGameOver gameOverListener) {
        this.gameOverListener = gameOverListener;
    }


    public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialise();
    }

    private void initialise() {
        this.setOnTouchListener(this);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                createNewPath(event.getX(), event.getY(), event.getPointerId(0));
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                int newPointerIndex = event.getActionIndex();
                createNewPath(event.getX(newPointerIndex), event.getY(newPointerIndex), event.getPointerId(newPointerIndex));

                break;
            case MotionEvent.ACTION_MOVE:

                for (int i = 0; i < paths.size(); i++) {
                    int pointerIndex = event.findPointerIndex(paths.indexOfKey(i));

                    if (pointerIndex >= 0) {
                        paths.valueAt(i).lineTo(event.getX(pointerIndex), event.getY(pointerIndex));
                        paths.valueAt(i).updateTimeDrawn(System.currentTimeMillis());
                    }
                }
                break;
        }

        gameThread.updateDrawnPath(paths);
        return true;
    }

    private void createNewPath(float x, float y, int ptrId) {
        TimedPath path = new TimedPath();
        path.moveTo(x, y);
        path.updateTimeDrawn(System.currentTimeMillis());
        paths.append(ptrId, path);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (isGameInitialised) {
            gameThread.resumeGame(width, height);
        } else {
            isGameInitialised = true;
            projectileManager = new FruitProjectileManager(getResources());
            gameThread = new GameThread(
                    getHolder(),
                    projectileManager,
                    Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back_fruit),0,0,width,height),
                    getResizedBitmap(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_can)),width/20),
                    gameOverListener
            );
            gameThread.startGame(width, height);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.pauseGame();
    }

    // width scaling
    public Bitmap getResizedBitmap(Bitmap bm, int newSize) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scale = ((float) newSize) / width;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scale, scale);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
}
