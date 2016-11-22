package moonvishnya.xyz.moonpickle.objects;


import android.util.Log;

import moonvishnya.xyz.moonpickle.PickleView;
import moonvishnya.xyz.moonpickle.graphics.BitmapResourses;

public class PickleCharacter implements AliveObject {

    /*
        текущие координаты объекта
     */
    private static float x;
    private static float y;

    /*
        скорость и расстояние,
         пройденное объектом
     */
    private static float speed;
    private static float distance;
    private static final float max_speed = 183;


    /*
        данные для прыжка вверх
     */
    private static float v0;
    private static float hmax;
    private static float t;
    private static float h0;
    private static float vy;
    private static int time_count;

    /*
        ускорение объекта
     */
    private float a;
    private float g;

    /*
        координаты бэкграунда
     */
    private static float backgroundLayerX = 0;
    private static float backgroundLayerX2;

    /*
        очки, которые набрал игрок
     */
    public static int score;

    /*
        enum, который мы используем для перечисления состояний игрока.
     */
    public static PickleCharacterStates pickleState;


    public PickleCharacter(final float x, final float y) {
        this.x = x;
        this.y = y;
        init_current_states();
    }

    private void init_current_states() {
        score = 0;
        speed = 0;
        distance = 0;
        a = 0.5f;
        g = 10f;
        h0 = y;
        v0 = 320f;
        t = v0 / g;
        hmax = v0*v0 / 2*g;
        backgroundLayerX2 = BitmapResourses.get_background_width();
        pickleState = PickleCharacterStates.RUNNING;
    }

    @Override
    public void move() {

        score += 1;

        if (backgroundLayerX <= -BitmapResourses.get_background_width()) {
            backgroundLayerX = 0;
            backgroundLayerX2 = BitmapResourses.get_background_width();
        }

        if (speed <= max_speed) {
            speed += a;
        }
        distance += speed;

        for (Wall w : PickleView.walls) {
                w.setX(w.getX() - speed / 17);
        }

        backgroundLayerX -= speed / 17;
        backgroundLayerX2 -= speed / 17;

    }

    @Override
    public void jump() {
            move();
            time_count += 1;

            if (time_count <= (int) t / 2) {
                vy = v0 - g / 17;
                y -= vy / 17 - g / 2 ;
            } else {
                if (y <= h0) {
                    vy = v0 + g / 17;
                    y += vy / 17 + g / 2 * 1 / 289;
                }
            }
        if (time_count == (int) t) {
            time_count = 0;
            pickleState = PickleCharacterStates.RUNNING;
        }
    }

    public enum PickleCharacterStates {
        RUNNING, JUMPING, DEAD
    }

    public static float getX() {
        return x;
    }
    public static float getY() {
        return y;
    }

    public static float getBackgroundLayerX() {
        return backgroundLayerX;
    }
    public static float getBackgroundLayerX2() {
        return backgroundLayerX2;
    }
}
