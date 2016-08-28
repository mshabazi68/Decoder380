package com.example.ali.decoder;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Encrypted extends Activity {
    //implements View.OnClickListener

    final int RQS_LOADIMAGE = 0;
    final int RQS_SENDEIMAGE = 1;

    Button sendBtn, imageBtn;
    EditText editNum, editMsg;
    Uri imageUri = null;
    MediaPlayer mp,er;
    Boolean image = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypted);
        mp = MediaPlayer.create(this,R.raw.click);
        er = MediaPlayer.create(this,R.raw.error);


        editNum = (EditText)findViewById(R.id.et_number);
        editMsg = (EditText)findViewById(R.id.et_msg);

        imageBtn = (Button)findViewById(R.id.imgButton);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, RQS_LOADIMAGE);
            }
        });

        sendBtn=(Button)findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editNum.equals("")){
                        if(!editMsg.equals("")){
                            if(image==true){
                                mp.start();

                                try {
                                    Intent intent = new Intent(Intent.ACTION_SEND);

                                    intent.putExtra("address", editNum.getText().toString());
                                    intent.putExtra("sms_body", editMsg.getText().toString());

                                    if (imageUri != null) {
                                        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                        intent.setType("image/png");
                                    } else {
                                        intent.setType("plain/text");
                                    }
                                    startActivity(Intent.createChooser(intent, "Select picture:"));

                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }

                            else{
                                er.start();
                                toast("Upload an Image first!");
                            }


                        }
                        else{
                            er.start();
                            toast("Please Enter a message first!");
                        }

                    }
                    else
                    {
                        er.start();
                        toast("Please Enter a number first!");
                    }

            }
        });
    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch(requestCode){
                case RQS_LOADIMAGE:
                    imageUri = data.getData();
                    image=true;
                    break;
                case RQS_SENDEIMAGE:

                    break;
            }

        }

    }









}
