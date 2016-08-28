package com.example.ali.decoder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;



public class CreateKey extends ActionBarActivity {
    private View root;
    private int currentBackgroundColor = 0xffffffff;
    HashMap<String,String> key = new HashMap<>();
    MediaPlayer mp,er;

    public  void saveFile(){
        try {
            // Here we  opening a file named  "key "
            // and  we create a new file or overwrite one if it already exists with the same name
            FileOutputStream fos = openFileOutput("Key.txt",
                    Context.MODE_APPEND | Context.MODE_WORLD_READABLE);
            fos.write(key.values().toString().getBytes());


            fos.close();

            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(getExternalFilesDir(null),
                        "Key.txt");
                FileOutputStream fos2 = new FileOutputStream(file);
                fos2.write(key.values().toString().getBytes());
                fos2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_key);
        root = findViewById(R.id.color_screen);
        GridView grid;
        Button ck = (Button) findViewById(R.id.ck_btn);
        final String[] web = {
                "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
                "0","1","2","3","4","5","6","7","8","9",
                "!","?",".",
                "SPACE"

        } ;

        mp = MediaPlayer.create(this,R.raw.click);
        er = MediaPlayer.create(this,R.raw.error);


        ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i =0; i<web.length;i++){
                    if(key.containsKey(web[i])){
                        count++;
                    }
                    else{

                    }
                }
                if(count!=40)
                {
                    er.start();
                    toast("Key Incomplete!");}

                else{
                    mp.start();
                    toast("Key Complete!");
                    saveFile();
                    toast("File Saved!");
                    Intent backIntent = new Intent(CreateKey.this,MainActivity.class);
                    startActivity(backIntent);

                }




            }




        });





        CustomGrid adapter = new CustomGrid(CreateKey.this, web);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                final Context context = CreateKey.this;

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Choose color")
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast("Color: FF" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {


                                if (key.containsValue(Integer.toHexString(selectedColor).toUpperCase())) {

                                    toast("You have already chosen this color! Please select a different Color!");

                                } else {

                                    key.put(web[position], Integer.toHexString(selectedColor).toUpperCase());


                                }


                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();


            }
        });




    }



    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }



}