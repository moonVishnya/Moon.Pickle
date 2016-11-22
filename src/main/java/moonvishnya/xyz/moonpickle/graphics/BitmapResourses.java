package moonvishnya.xyz.moonpickle.graphics;


import android.content.Context;
import android.graphics.Bitmap;
import moonvishnya.xyz.moonpickle.R;

public class BitmapResourses implements CanvasDrawer {

    private Context context;

    private BitmapManager bitmapManager;

    private static Bitmap background;
    private Bitmap char_anim_1;
    private Bitmap char_anim_2;
    private Bitmap wall;

    public BitmapResourses(Context context) {
        this.context = context;
        bitmapManager = new BitmapManager(context);

        setBackgroundImg();
        setWall();
        setChar_anim_1_ID();
        setChar_anim_2_ID();
    }

    @Override
    public void setWall() {
        wall = bitmapManager.getBitmapFromResourse(
                R.drawable.wall);
    }

    @Override
    public void setBackgroundImg() {
        background = bitmapManager.getScaledBackgroundFrom(
                bitmapManager.getBitmapFromResourse(
                        R.drawable.background));

    }

    @Override
    public void setChar_anim_1_ID() {
        char_anim_1 = bitmapManager.getCompressedBitmapFrom(
                bitmapManager.getBitmapFromResourse(
                        R.drawable.char_anim_1));
    }

    @Override
    public void setChar_anim_2_ID() {
        char_anim_2 = bitmapManager.getCompressedBitmapFrom(
                bitmapManager.getBitmapFromResourse(
                        R.drawable.char_anim_2));
    }

    public Bitmap getBackground() {
        return background;
    }

    public Bitmap getChar_anim_1() {
        return char_anim_1;
    }

    public Bitmap getChar_anim_2() {
        return char_anim_2;
    }

    public Bitmap getWall() {
        return wall;
    }

    public static float get_background_width() {
        return background.getWidth();
    }

}
