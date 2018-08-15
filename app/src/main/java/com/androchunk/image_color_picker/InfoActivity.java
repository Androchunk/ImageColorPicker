package com.androchunk.image_color_picker;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class InfoActivity extends Activity {

    TextView about_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info);
        about_us=findViewById(R.id.about_us);
        about_us.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
