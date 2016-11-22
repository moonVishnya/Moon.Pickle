package moonvishnya.xyz.moonpickle;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import moonvishnya.xyz.moonpickle.objects.PickleCharacter;


public class PickleActivity extends Activity implements View.OnTouchListener {

    private PickleView pickleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pickleView = new PickleView(this);
        setContentView(pickleView);
        pickleView.setOnTouchListener(this);
        log_screen_size();
    }

    /*
        в начале игры наш персонаж бежит
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PickleCharacter.pickleState = PickleCharacter.PickleCharacterStates.JUMPING;
        return true;
    }

    private void log_screen_size() {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
        Log.i("x&y", "width " + width + " height " + height);
    }
}
