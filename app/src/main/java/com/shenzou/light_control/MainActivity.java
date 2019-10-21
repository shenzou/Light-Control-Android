package com.shenzou.light_control;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

//import dev.sasikanth:colorsheet;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.mollin.yapi.YeelightDevice;
import com.mollin.yapi.enumeration.YeelightEffect;

public class MainActivity extends AppCompatActivity implements ColorPicker.OnColorChangedListener {

    TextView helloW;
    Button button1;
    ColorPicker picker;
    YeelightDevice device;
    SeekBar luminosity;
    Button salon;
    Button alex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloW = findViewById(R.id.textView);
        button1 = findViewById(R.id.button1);
        picker = findViewById(R.id.picker);
        luminosity = findViewById(R.id.seekBar);
        salon = findViewById(R.id.salon);
        alex = findViewById(R.id.alex);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Title");
            builder.setMessage("Message");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
             */
        }


        //Par défaut, réglé sur chambre Alex
        changeDevice("192.168.0.12");

        salon.setOnClickListener(v -> {
            changeDevice("192.168.0.11");
        });

        alex.setOnClickListener(v -> {
            changeDevice("192.168.0.12");
        });

        button1.setOnClickListener(v -> {

            Thread thread = new Thread(() -> {
                try{
                    device.toggle();
                }
                catch(Exception e){
                    helloW.setText(e.toString());
                }
            });


            thread.start();
            helloW.setText("OK !");
            helloW.setTextColor(Color.GREEN);

            /*
            ColorSheet().colorPicker(
                    colors = colors,
                    listener = { color ->
                            // Handle color
                    })
                    .show(supportFragmentManager);
             */
        });


        picker.setOnColorChangedListener(this);

        luminosity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int value = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Thread thread = new Thread(() -> {
                    try{
                        device.setBrightness(value);
                        helloW.setText("OK !");
                        helloW.setTextColor(Color.GREEN);
                    }
                    catch(Exception e){
                        helloW.setText(e.toString());
                    }
                });


                thread.start();
            }
        });

    }

    @Override
    public void onColorChanged(int color) {

        Thread thread = new Thread(() -> {
            try{
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);
                device.setRGB(red, green, blue);
            }
            catch(Exception e){
                helloW.setText(e.toString());
            }
        });

        thread.start();
        thread.interrupt();

    }

    public void changeDevice(String ip){
        Thread thread = new Thread(() -> {
            try{
                device = new YeelightDevice(ip, 55443, YeelightEffect.SMOOTH, 500);
            }
            catch(Exception e){
                helloW.setText(e.toString());
            }
        });
        thread.start();
    }
}
