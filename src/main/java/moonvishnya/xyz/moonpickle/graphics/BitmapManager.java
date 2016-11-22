package moonvishnya.xyz.moonpickle.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import moonvishnya.xyz.moonpickle.PickleView;

public class BitmapManager implements IBitmapManager {


    /*
        находим масштаб экрана, чтобы сжать изображение
     */
    private static DisplayMetrics metrics;
    private static float SCALE;

    /*
        масштабы сжатия
     */
    private static final int COMPRESS_WIDTH  = 70;
    private static final int COMPRESS_HEIGHT = 140;

    private Context context;
    public BitmapManager(Context context) {
        this.context = context;
        metrics = context.getResources().getDisplayMetrics();
        SCALE = metrics.scaledDensity;
    }

    @Override
    public Bitmap getCompressedBitmapFrom(Bitmap original) {
        return Bitmap.createScaledBitmap(original, COMPRESS_WIDTH * (int) SCALE, COMPRESS_HEIGHT * (int) SCALE, false);
    }

    @Override
    public Bitmap getBitmapFromResourse(int resID) {
         Bitmap output = BitmapFactory.decodeResource(context.getResources(), resID);
        return output;
    }

    private static int get_screen_height() {
        return metrics.heightPixels;
    }

    public static Bitmap getScaledBackgroundFrom(Bitmap original) {
        return Bitmap.createScaledBitmap(original, original.getWidth(), get_screen_height(), false);
    }

}
