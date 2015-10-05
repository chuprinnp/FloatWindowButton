package style.messenger.button.floating.com.floatwindowbutton;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Admin on 05.10.2015.
 */
public class FloatButtonService extends Service {

    //менеджер к которому цепляем кнопку что бы все время быть на верху
    private WindowManager windowManager;
    private ImageView chatHead;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();

        //инициализируем его
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //создаем нашу кнопку что бы отобразить
        chatHead = new ImageView(this);
        chatHead.setImageResource(R.mipmap.face1);

        //задаем параметры для картинки, что бы была
        //своего размера, что бы можно было перемещать по экрану
        //что бы была прозрачной, и устанавливается ее стартовое полодение
        //на экране при создании
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //кол перемещения тоста по экрану при помощи touch
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private boolean shouldClick;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shouldClick = true;
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if(shouldClick)
                            Toast.makeText(getApplicationContext(), "Клик по тосту случился!", Toast.LENGTH_LONG).show();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        shouldClick = false;
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                }
                return false;
            }
        });
        windowManager.addView(chatHead, params);
    }

    //удалем тост если была команда выключить сервис
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}