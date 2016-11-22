package moonvishnya.xyz.moonpickle.objects;

/**
 * Created by Федя on 22.11.2016.
 */

public class Wall implements EnemyObject {

    private float x;
    private float y;

    public Wall(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int generate_curr_location() {
        return 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
