package moonvishnya.xyz.moonpickle.graphics;

import android.graphics.Bitmap;

/**
 * Created by Федя on 18.11.2016.
 */

public interface IBitmapManager {
    Bitmap getCompressedBitmapFrom(Bitmap input);
    Bitmap getBitmapFromResourse(int resID);
}
