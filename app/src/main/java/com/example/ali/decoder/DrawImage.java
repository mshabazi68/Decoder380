package com.example.ali.decoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Ali on 8/1/2015.
 */
public class DrawImage extends Activity {
    HashMap<String,String> mymap= new HashMap<>();
    String[] myColors;
    ArrayList<String> myMsg = new ArrayList<>();

    String[] chars = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9",
            "!","?",".",
            "SPACE"
    } ;




    public static Bitmap loadBitmapFromView(View view) {

        // width measure spec
        int widthSpec = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        // height measure spec
        int heightSpec = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredHeight(), View.MeasureSpec.AT_MOST);
        // measure the view
        view.measure(widthSpec, heightSpec);
        // set the layout sizes
        view.layout(view.getLeft(), view.getTop(), view.getMeasuredWidth() + view.getLeft(), view.getMeasuredHeight() + view.getTop());
        // create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // create a canvas used to get the view's image and draw it on the bitmap
        Canvas c = new Canvas(bitmap);
        // position the image inside the canvas
        c.translate(-view.getScrollX(), -view.getScrollY());
        // get the canvas
        view.draw(c);

        return bitmap;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Draw2d d = new Draw2d(this);
        setContentView(d);
        Intent intent = getIntent();
        mymap = (HashMap<String, String>) intent.getSerializableExtra("hexMap");
        myMsg= (ArrayList<String>)intent.getSerializableExtra("msgArray");

        myColors = new String[myMsg.size()];

        for(int i=0; i<myMsg.size();i++){
            myColors[i] = mymap.get(myMsg.get(i));
        }


    }


    public class Draw2d extends View {

        public Draw2d(Context context) {
            super(context);
            setDrawingCacheEnabled(true);

        }



        @Override
        protected void onDraw(Canvas c) {


            Canvas canvas = null;
            FileOutputStream fos = null;
            File file;
            File myDir;
            Bitmap bmpBase = null;


            Bitmap toDisk = null;
            int i = 0;
            int cc = 0;
            int kk = 0;


            Paint paint = new Paint();


            bmpBase = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bmpBase);
            canvas.drawColor(Color.WHITE);


            for (int j = 0; j < myColors.length; j++) {

                for (int m = 0; m < (c.getWidth() - 90) && i < myColors.length; m = m + 100) {

                   // String string = mymap.get(chars[i]).toString();
                   // Log.i(chars[i], mymap.get(chars[i]));
                    String string = myColors[i];

                    paint.setColor(Color.parseColor(string));
                    paint.setStyle(Paint.Style.FILL);

                    canvas.drawRect(10 + cc, 10 + kk, 110 + cc, 110 + kk, paint);
                    c.drawRect(10 + cc, 10 + kk, 110 + cc, 110 + kk, paint);



                    cc = cc + 100;
                    i++;

                }
                cc = 0;
                kk = kk + 100;
            }


// draw what ever you want canvas.draw...

// Save Bitmap to File
            myDir=new File("/sdcard/DCIM/Camera");
            myDir.mkdir();
            Random generator = new Random();
            int n = 20;
            n = generator.nextInt(n);
            String fname = "Enc-"+ n +".jpg";
            file = new File (myDir, fname);
            if (file.exists ()) file.delete ();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bmpBase.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                        fos = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            super.onDraw(c);
            Intent enc2Intent = new Intent(DrawImage.this, Encrypted.class);
            startActivity(enc2Intent);
            finish();
        }
    }

}
