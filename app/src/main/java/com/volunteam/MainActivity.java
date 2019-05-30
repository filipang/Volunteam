package com.volunteam;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Image loading is done on separate slide to avoid NetworkingOnMainThreadException
        new Thread(new GetImageTask()).start();
    }

    //Task that loads Image solving threading/UI/Networking issues
    class GetImageTask implements Runnable {

        private Handler handler;
        private Drawable drawable;

        public GetImageTask() {

            //Handler is set to execute on UI/main thread
            handler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run() {

            drawable = Voluntariat.loadDrawableFromURL(Voluntariat.getTestList()[0].getImageURL());

            //After drawable is loaded, start updating the UI in the UI thread, using a handler
            handler.post(new Runnable() {
                @Override
                public void run() {

                    LinearLayout scrollList = findViewById(R.id.vol_scroll_list);
                    Voluntariat[] voluntariatList = Voluntariat.getTestList(); // = DatabaseHandler.fetchVoluntariatList(User.me);

                    for (Voluntariat vol : voluntariatList) {
                        LinearLayout element = new LinearLayout(MainActivity.this);
                        element.setOrientation(LinearLayout.HORIZONTAL);
                        scrollList.addView(element);

                        ImageView img = new ImageView(MainActivity.this);
                        element.addView(img);
                        img.setImageDrawable(drawable);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) img.getLayoutParams();
                        final float scale = getResources().getDisplayMetrics().density;
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());
                        img.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
                        img.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
                        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        //((LinearLayout.LayoutParams)img.getLayoutParams()).weight = 1;

                        TextView text = new TextView(MainActivity.this);
                        element.addView(text);
                        text.setText(vol.getDescription());
                    }
                }
            });
        }

    }
}

