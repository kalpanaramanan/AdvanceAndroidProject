package com.lambton.finalprojectadvancedandroid;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText1,editText2;
    public final String TAG = "KALPANA";
    private PermissionListener sendSMSPermissionListener;
    private final int SPEECH_REQUEST_CODE = 500;
    private final int SPEECH_REQUEST_MAP = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = (EditText) findViewById(R.id.telephone);
        editText2 = (EditText) findViewById(R.id.textMessage);
        createPermissionListener();
    }

    public void createPermissionListener() {
        if (sendSMSPermissionListener == null) {
            sendSMSPermissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    // permission was granted.
                    Log.d(TAG, "Permission granted!");
                    sendSMS();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {
                    // you don't have permissions
                    TextView t = (TextView) findViewById(R.id.statusMessage);
                    t.setText("Sorry, I don't have permission to send an SMS.");
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    // run this if the person pressed deny the first time
                    Log.d(TAG, "PERMISSION NEEDS TO BE REQUESTED!");
                    token.continuePermissionRequest();
                }
            };
        }
    }

    public void sendSMS() {
        // UI BAKWAS - get the phone number and message from the UI
        EditText editText1 = (EditText) findViewById(R.id.telephone);
        EditText editText2 = (EditText) findViewById(R.id.textMessage);

        String tel = editText1.getText().toString();
        String msg = editText2.getText().toString();

        // LOGIC - send the sms
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(tel,null, msg, null, null);


        // UI BEYKAR -

        // update the text box to show that the message was sent
        TextView t = (TextView) findViewById(R.id.statusMessage);
        t.setText("Message sent!");

        // clear the message text box
        editText2.setText("");
    }

    private void startSpeechRecognizer( ) {

            // this is the default microphone popup box
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            // SETUP NONSENSE: the next two lines are all setup nonsense
            // you can also do: "EXTRA_LANGUAGE_MODEL, en-us" for a specific locale
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            // configure the message on the microphone popup box
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What color is the sky?");

            // uncomment this code if you want to get more than one result back
            // from the speech recognizer
            // intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

            try {
                // show the popup box
                startActivityForResult(intent, SPEECH_REQUEST_CODE);




            } catch (ActivityNotFoundException a) {
                // Sometimes you are using a phone that doesn't have speech to text functions
                // If this happens, then show error message.
                String msg = "Your phone doesn't support speech to text.";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

    }


    private void startSpeechRecognizer1( ) {

        // this is the default microphone popup box
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // SETUP NONSENSE: the next two lines are all setup nonsense
        // you can also do: "EXTRA_LANGUAGE_MODEL, en-us" for a specific locale
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // configure the message on the microphone popup box
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What color is the sky?");

        // uncomment this code if you want to get more than one result back
        // from the speech recognizer
         intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        try {
            // show the popup box
            startActivityForResult(intent, SPEECH_REQUEST_MAP);


        } catch (ActivityNotFoundException a) {
            // Sometimes you are using a phone that doesn't have speech to text functions
            // If this happens, then show error message.
            String msg = "Your phone doesn't support speech to text.";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

    }


    // This function gets called when the person finishes talking
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String answer = results.get(0);

                editText2.setText("You said: " + answer);


                if (answer.indexOf("blue") > -1)
                    Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
                }
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.SEND_SMS)
                        .withListener(sendSMSPermissionListener)
                        .check();


            }
        }

        if (requestCode == SPEECH_REQUEST_MAP) {
            if (resultCode == RESULT_OK) {

                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String answer = results.get(0);
                String answer1 = results.get(1);

                Intent intent1 = new Intent(this,MapsActivity.class);
                startActivity(intent1);
                intent1.putExtra("lat",answer);
                intent1.putExtra("lon",answer1);
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void sendSMSButtonPressed(View view) {
        startSpeechRecognizer();
    }


    public void showMeButtonPressed(View view) {
        startSpeechRecognizer1();

    }
}
