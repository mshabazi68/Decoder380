package com.example.ali.decoder;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Encrypt extends Activity {
    private static final int PICKFILE_RESULT_CODE = 1;
    String[] chars = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9",
            "!","?",".",
            "SPACE"
    } ;
    String mymsg="";
    ArrayList<String> msgArray = new ArrayList<>();
    HashMap<String, String> hexMap = new HashMap<String,String>();
    Button enc2 ;
    Button open ;
    EditText txtmsg ;
    Intent enc2Intent;
    Boolean keyOpened = false;
    Boolean message = false;
    MediaPlayer mp,er;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        enc2 = (Button)findViewById(R.id.enc2_btn);
        open =(Button) findViewById(R.id.key_btn);
         txtmsg = (EditText) findViewById(R.id.txtmsg);
        enc2Intent= new Intent(Encrypt.this, DrawImage.class);
        mp = MediaPlayer.create(this,R.raw.click);
        er = MediaPlayer.create(this,R.raw.error);



        enc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readText();


                if(message == true){
                    if(keyOpened == true){
                        mp.start();
                        startActivity(enc2Intent);
                        finish();
                    }
                    else
                    { er.start();
                        toast("Get a Key first!");}
                }
                else    {er.start();
                        toast("Enter a Message first!");}
            }
        });

        open.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mp.start();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);

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
            for(int i = 0; i < chars.length; i++){
                hexMap.put(chars[i], "#"+array[i].replaceAll("\\s",""));
            }
            enc2Intent.putExtra("hexMap", hexMap);
            keyOpened= true;





        }catch(FileNotFoundException e){

        }

    }

    private void readText() {
        mymsg = txtmsg.getText().toString();
        if(!mymsg.equals("")) {
            for (int i = 0; i < mymsg.length(); i++) {
                if (String.valueOf(mymsg.charAt(i)).equals(" "))
                    msgArray.add(i, "SPACE");
                else
                    msgArray.add(i, String.valueOf(mymsg.charAt(i)).toUpperCase());
            }


            enc2Intent.putExtra("msgArray", msgArray);
            message = true;

        }


    }













    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                    readFile(FilePath);
                }
                break;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
