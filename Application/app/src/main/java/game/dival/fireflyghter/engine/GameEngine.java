package game.dival.fireflyghter.engine;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.View;

import java.util.ArrayList;

import game.dival.fireflyghter.engine.entity.Camera;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.renderer.GLESRenderer;


/**
 * Created by arauj on 24/02/2017.
 * DEPRECATED
 */
public class GameEngine {

    public ArrayList<Entity> entities;
    public GameResources resouces;
    public GLESRenderer glesRenderer;
    public GameUpdates updates;
    public Camera camera;
    public boolean runningEngine;
    private GLSurfaceView surface;
    private Activity activity;

    public GameEngine(Activity activity, GLSurfaceView surfaceOfTheGame, GameResources resources, GameUpdates gameUpdates) {
        this.surface = surfaceOfTheGame;
        this.activity = activity;
        this.updates = gameUpdates;

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (surfaceOfTheGame != null && glesRenderer != null) {
            surface.setEGLContextClientVersion(2);
            glesRenderer = new GLESRenderer(this, gameUpdates);
            surface.setRenderer(glesRenderer);
        }

        hideSystemUI();
        entities = new ArrayList<>();
        this.resouces = resources;
        resources.isLoaded();

    }

    public Camera getCamera() {
        return camera;
    }

    public void pause() {
        runningEngine = false;
        surface.onPause();
        showSystemUI();
    }

    public void play() {
        runningEngine = true;
        surface.onResume();
        hideSystemUI();
    }

    public void finish() {
        runningEngine = false;
        surface.onPause();
        showSystemUI();
    }

    public boolean isRunning() {
        return runningEngine;
    }

    private void hideSystemUI() {
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN); // hide status bar
        }

    }

    private void showSystemUI() {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * It will update MOVEMENT, PHYSICS, COLLISIONS of all entities
     *
     * @param mMVPMatrix
     */
    public void engineUpdates(float[] mMVPMatrix) {
        updates.gameFrame();
        for (Entity entity : entities) {

            //ONE THREAD
            entity.run(this);

            //ASYNC
//            new RunComponentsAsync(this,mMVPMatrix).execute(entity);
//            entity.getModel3D().draw(this,mMVPMatrix);
        }
//        for (Component camComp : camera.components)
//            camComp.draw(this,mMVPMatrix);
    }

    public void addCamera(Camera camera) {
        this.camera = camera;
//        entities.add(camera);
    }

    public interface GameUpdates {
        void gameFrame();
    }

//    private class RunComponentsAsync extends AsyncTask<Entity, Void, Entity> {
//
//        private GameEngine engine;
//        private float[] mvp;
//
//        public RunComponentsAsync(GameEngine engine, float[] mvp) {
//            this.engine = engine;
//            this.mvp = mvp;
//        }
//
//        @Override
//        protected Entity doInBackground(Entity... ent) {
//            for (Component component : ent[0].components)
//                if (!(component instanceof Model3D))
//                    component.draw(engine, mvp);
//            return ent[0];
//        }
//    }

}
