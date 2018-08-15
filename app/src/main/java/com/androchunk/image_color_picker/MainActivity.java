package com.androchunk.image_color_picker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public Button choose_image;
    public TextView hexValue, rgbValue;
    public ImageView selectedImage, color_display;
    public String rgbcolor, hexcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choose_image = findViewById(R.id.Choose_image);
        hexValue = findViewById(R.id.hexcolor);
        rgbValue = findViewById(R.id.rgbcolor);
        color_display = findViewById(R.id.color_display);
        selectedImage = findViewById(R.id.seleted_image);

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start intent for select image
                Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, 1);//you can change request code you want
            }
        });

        selectedImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    //get touch event
                    final int action = motionEvent.getAction();
                    final int evX = (int) motionEvent.getX();//get x coordinate
                    final int evY = (int) motionEvent.getY();//get y coordinate
                    //get color from the pixal
                    int touchColor = getColor(selectedImage, evX, evY);
                    //get R,G,B value of the pixal
                    int r = (touchColor >> 16) & 0xFF;
                    int g = (touchColor >> 8) & 0xFF;
                    int b = (touchColor >> 0) & 0xFF;
                    rgbcolor = String.valueOf(r) + "," + String.valueOf(g) + "," + String.valueOf(b) + ",";
                    rgbValue.setText("RGB:   " + rgbcolor);

                    //get hax color from rgb value
                    hexcolor = Integer.toHexString(touchColor);
                    if (hexcolor.length() > 2) {
                        hexcolor = hexcolor.substring(2, hexcolor.length());//remove alfa from value
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        //set touch event
                        color_display.setBackgroundColor(touchColor);
                        hexValue.setText("HEX:   #" + hexcolor);
                    }
                } catch (Exception e) {
                }

                return true;
            }
        });

    }

    private int getColor(ImageView selectedImage, int evX, int evY) {
        selectedImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(selectedImage.getDrawingCache());
        selectedImage.setDrawingCacheEnabled(false);
        return bitmap.getPixel(evX, evY);//it will return selected pixal
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && !data.equals(null)) {
            Uri Image = data.getData();
            selectedImage.setImageURI(Image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Clip", "#" + hexcolor);
                Toast.makeText(this, "copied #" + hexcolor, Toast.LENGTH_SHORT).show();
                clipboardManager.setPrimaryClip(clip);
                break;

            case R.id.btn_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "ImageColorPicker");
                    String s = "\nColorPicker: Pic any color from Image\n\n";
                    s = s + "https://play.google.com/store/app/details?id=com.androchunk.imagecolorpicker";
                    i.putExtra(Intent.EXTRA_TEXT, s);
                    startActivity(Intent.createChooser(i, "Share App"));

                } catch (Exception e) {

                }
                break;

            case R.id.btn_about:
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
