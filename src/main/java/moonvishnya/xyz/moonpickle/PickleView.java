package moonvishnya.xyz.moonpickle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import moonvishnya.xyz.moonpickle.graphics.BitmapResourses;
import moonvishnya.xyz.moonpickle.objects.PickleCharacter;
import moonvishnya.xyz.moonpickle.objects.Wall;


public class PickleView extends SurfaceView {

    /*
        вся игра держится на этом потоке
     */
    private static MoonPickleProjectThread mainThread;

    /*
        этим таймером настраиваем тик
     */
    private static Timer gameIterationTimer;

    /*
        1000 cек / 60 кадров = 17
        для 60 фпс обновляем экран каждые 17 мс
     */
    private static final int TICKS_PER_SEC = 17;

    /*
        масштаб
     */
    private DisplayMetrics metrics = getResources().getDisplayMetrics();
    private final float SCALE = metrics.scaledDensity;
    private float onStartPickleCharacterPosition;

    /*
        канвас инит
     */
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    /*
        после создания экземпляра этого класса
        создадутся сжатые битмапы бэкграунда
        и персонажа
     */
    private BitmapResourses bmResourses;


    /*
        костыль, который мы используем для выбора текущего
            спрайта нашего персонажа
        зависит от набранных очков.
        0 -- первый спрайт
        1 -- второй спрайт
     */
    private static boolean sprite_selector = false;

    /*
        лист, который содержит в себе преграды
     */
    public static List<Wall> walls = new ArrayList<>();


    /*
        collision detect
     */
    private float ymin = 336 * SCALE;
    private float ymax;
    private float xmin;
    private float xmax;
    private float x_of_character_left;
    private float y_of_character_bottom;

    public PickleView(Context context) {
        super(context);
        initRenderRes();
        mainThread = new MoonPickleProjectThread(SCALE);

        x_of_character_left = PickleCharacter.getX();
        y_of_character_bottom = PickleCharacter.getY() + bmResourses.getChar_anim_1().getScaledHeight(metrics);
        onStartPickleCharacterPosition = bmResourses.getChar_anim_1().getScaledHeight(metrics);

        gameIterationTimer = new Timer();
        gameIterationTimer.schedule(mainThread, 0, TICKS_PER_SEC);
    }

    private Bitmap getCurrentSpriteForPickleCharacter() {
        if (sprite_selector) {
            return bmResourses.getChar_anim_1();
        } else
            return bmResourses.getChar_anim_2();
    }

    private void initRenderRes() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        surfaceHolder = getHolder();
        bmResourses = new BitmapResourses(getContext());
        ymax = ymin - bmResourses.getWall().getScaledHeight(metrics);
        xmax = bmResourses.getWall().getWidth();
        generateWalls();
    }

    public void renderGraphics() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            if (PickleCharacter.score / 34 % 2 == 0) {
                sprite_selector = true;
            } else {
                sprite_selector = false;
            }
            renderBackground(canvas);
            renderCharacter(canvas);
            renderScore(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void renderScore(Canvas canvas) {
        canvas.drawText(PickleCharacter.score / 34 + " ", 20, 20, paint);
    }

    private void renderBackground(Canvas canvas) {
        canvas.drawBitmap(bmResourses.getBackground(),
                PickleCharacter.getBackgroundLayerX(),
                0,
                paint);

        if (-PickleCharacter.getBackgroundLayerX() >= BitmapResourses.get_background_width()-metrics.widthPixels)
            canvas.drawBitmap(bmResourses.getBackground(),
                    PickleCharacter.getBackgroundLayerX2(),
                    0,
                    paint);
        renderWalls(canvas);
    }

    private void renderWalls(Canvas canvas) {
        for (Wall w : walls) {
            canvas.drawBitmap(bmResourses.getWall(),
                    w.getX(),
                    w.getY(),
                    paint);
        }
    }

    private void renderCharacter(Canvas canvas) {
        canvas.drawBitmap(getCurrentSpriteForPickleCharacter(),
                PickleCharacter.getX(),
                PickleCharacter.getY() - onStartPickleCharacterPosition,
                paint);
    }

    private void checkCollision() {

        for (Wall w : walls) {
            xmin = w.getX();

//            if (((PickleCharacter.getX() >= xmin) & (PickleCharacter.getX() <= w.getX() + xmax))
//                    &
//                    ((PickleCharacter.getY() >= ymax) & (PickleCharacter.getY() <= ymin))) {
//                PickleCharacter.pickleState = PickleCharacter.PickleCharacterStates.DEAD;
//            }
//            for (int i = (int) PickleCharacter.getX(); i < bmResourses.getChar_anim_1().getScaledWidth(metrics); i++) {
//                for (int j = (int) PickleCharacter.getY(); j < bmResourses.getChar_anim_1().getScaledHeight(metrics); j++) {
//                    if ((bmResourses.getChar_anim_1().getPixel(i, j) != Color.TRANSPARENT) &
//                            ( (i >= xmin) & (i <= xmax + w.getX()))
//                            & ((j >= ymax) & (j <= ymin))) {
//                        PickleCharacter.pickleState = PickleCharacter.PickleCharacterStates.DEAD;
//                    }
//                }
//            }

            if ((x_of_character_left >= xmin & x_of_character_left <= xmin + xmax) & (PickleCharacter.getY() + bmResourses.getChar_anim_1().getScaledHeight(metrics) - 25*SCALE >= ymax)) {
                PickleCharacter.pickleState = PickleCharacter.PickleCharacterStates.DEAD;
            }

        }
    }

    private void generateWalls() {
        final float y = 336 * SCALE - bmResourses.getWall().getScaledHeight(metrics);
        float x = metrics.widthPixels;
        Random r = new Random();
        int randomValue = r.nextInt(200);
        int prevValue = randomValue;
        for (int i = 0; i < 200; i++) {
            randomValue = prevValue + r.nextInt(250) + 400;
            Wall wall = new Wall(x + randomValue, y);
            walls.add(wall);
            prevValue = randomValue;
        }
    }

    private class MoonPickleProjectThread extends TimerTask {

        private PickleCharacter pickleCharacter;

        /*
            начальные координаты для игрока
         */
        private static final float x = 20;
        private static final float y = 336;

        public MoonPickleProjectThread(final float SCALE) {
            pickleCharacter = new PickleCharacter(x * SCALE, y * SCALE);
        }

        @Override
        public void run() {
            while (PickleCharacter.pickleState != PickleCharacter.PickleCharacterStates.DEAD) {
                if (PickleCharacter.pickleState == PickleCharacter.PickleCharacterStates.JUMPING) {
                    pickleCharacter.jump();
                    if (PickleCharacter.pickleState == PickleCharacter.PickleCharacterStates.DEAD) return;
                }
                if (PickleCharacter.pickleState == PickleCharacter.PickleCharacterStates.RUNNING) {
                    pickleCharacter.move();
                    if (PickleCharacter.pickleState == PickleCharacter.PickleCharacterStates.DEAD) return;
                }
                checkCollision();
                renderGraphics();
            }
        }



    }

}
