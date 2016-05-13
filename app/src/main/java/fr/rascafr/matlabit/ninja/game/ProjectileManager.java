package fr.rascafr.matlabit.ninja.game;

import java.util.List;

import android.graphics.Canvas;

public interface ProjectileManager {
    void draw(Canvas c);

    int update();

    void setWidthAndHeight(int width, int height);

    int testForCollisions(List<TimedPath> allPaths);
}
