package com.example.ali.decoder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Ali on 7/22/2015.
 */

public class Decrypt extends Activity {
    Intent decIntent;
    private static final int SELECTED_PICTURE=1;
    ImageView iv;
    ArrayList<String> colors;
    File file;
    String message="";
    private static final int PICKFILE_RESULT_CODE = 2;
    String[] chars = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9",
            "!","?",".",
            "SPACE"
    } ;
    HashMap<String, String> hexMapR = new HashMap<String,String>();
    MediaPlayer mp,er;
    Boolean keyOpened = false;
    Boolean imgOpened= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        colors = new ArrayList<>();
        decIntent = new Intent(Decrypt.this, Decrypted.class);
        Button getKey = (Button) findViewById(R.id.decKey);
        Button dec = (Button) findViewById(R.id.dec_btn);
        iv=(ImageView)findViewById(R.id.imageView);
        final Button openGallery = (Button) findViewById(R.id.selectImg);
        mp = MediaPlayer.create(this,R.raw.click);
        er = MediaPlayer.create(this,R.raw.error);



        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);


            }
        });

        getKey.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp.start();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyOpened== true) {
                    if (imgOpened == true) {
                        mp.start();
                        translate();
                        startActivity(decIntent);
                    } else
                        er.start();
                        toast("open an image first!");
                }
                else
                    er.start();
                    toast("get a key first!");


            }
        });

    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private void readFile(String filename){

        File f = new File(filename);
        Scanner txtData;
        String [] array;
        String hexVals = "";




        try{
            txtData = new Scanner(f);


            while(txtData.hasNext()){

                String str = txtData.nextLine();
                int bracketIndex1 = str.indexOf('[');

                String value = str.substring(bracketIndex1);
                int bracketIndex2 = str.indexOf(']');

                hexVals = value.substring(bracketIndex1+1,bracketIndex2);

            }

            array = hexVals.split(",");
            for(int i = 0; i < chars.length; i++) {
                hexMapR.put( array[i].replaceAll("\\s",""),chars[i]);


            }

            keyOpened = true;



        }catch(FileNotFoundException e){

        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK){
                    Uri uri=data.getData();
                    String[]projection={MediaStore.Images.Media.DATA};

                    Cursor cursor=getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex=cursor.getColumnIndex(projection[0]);
                    String filePath=cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap yourSelectedImage=BitmapFactory.decodeFile(filePath);
                    Drawable d=new BitmapDrawable(yourSelectedImage);

                    iv.setBackground(d);
                    // iv.setOnClickListener(onClick1);


                    Bitmap  b = ((BitmapDrawable) iv.getBackground()).getBitmap();


                    String whitePixelCheck="";

                    for(int y = 60; y < b.getHeight(); y=y+100){

                        for(int x = 60;x < b.getWidth(); x=x+100){
                            //
                            whitePixelCheck = String.format("%06X", 0xFFFFFF & b.getPixel(x, y));
                            if(!whitePixelCheck.equals("FFFFFF")){
                                colors.add("FF"+String.format("%06X", 0xFFFFFF & b.getPixel(x, y)));
                            }


                        }

                    }
                    imgOpened = true;











                }
                break;

            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){

                    String FilePath = data.getData().getPath();



                    readFile(FilePath);
                }
                break;

            default:
                break;
        }



    }

    private void translate(){
        for(int i=0; i<colors.size();i++){
            if(hexMapR.get(colors.get(i)).equals("SPACE"))
                message+=" ";
            else
                message += hexMapR.get(colors.get(i));
        }
        decIntent.putExtra("message", message);

    }

}