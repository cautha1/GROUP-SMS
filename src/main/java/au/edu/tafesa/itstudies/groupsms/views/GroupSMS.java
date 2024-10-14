package au.edu.tafesa.itstudies.groupsms.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import au.edu.tafesa.itstudies.groupsms.R;
import au.edu.tafesa.itstudies.groupsms.models.SMSDataModelArray;

public class GroupSMS extends AppCompatActivity {

    public static final String CLASS_TAG = "GroupSMS";
    private SMSDataModelArray messageData;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sms);
        TextView tvMessageDetails = findViewById(R.id.tvMessageDetails);
        tvMessageDetails.setBackgroundColor(Color.GREEN);
        tvMessageDetails.setMovementMethod(new ScrollingMovementMethod());
        messageData = new SMSDataModelArray("NOT SET", 5);
        messageData.addPhoneNumber("0401125172");

        ActivityResultContract simpleRawIntentContract;
        ActivityResultLauncher launchEditMessageActivity;
        HandleActivityResultForMessage handleActivityResultForMessage;

        simpleRawIntentContract = new ActivityResultContracts.StartActivityForResult();
        handleActivityResultForMessage = new HandleActivityResultForMessage();

        //Instantiating our lauchActivity object for EditMessage
        launchEditMessageActivity = registerForActivityResult(simpleRawIntentContract, handleActivityResultForMessage);

        //Instantiating our lauchActivity object for EditSendTo
        ActivityResultLauncher launchEditSendToActivity;
        launchEditSendToActivity = registerForActivityResult(simpleRawIntentContract, new HandleActivityResultForPhone());

        /**
         * Handle Edit Message Button OnClick by starting the activity. This is an example of
         * starting another activity using an explicit Intent.
         */
        Button btnEditMessage;
        btnEditMessage = (Button) this.findViewById(R.id.btnEditMessage);
        btnEditMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent;
                Activity sourceActivity;
                Class destinationClass;

                sourceActivity = GroupSMS.this;
                destinationClass = EditMessage.class;

                // Sending information to the intent receiver through the Intent object
                editIntent = new Intent(sourceActivity, destinationClass);
                editIntent.putExtra("CURRENT_MESSAGE",messageData.getMessage());

                //MainActivity.this.startActivity(editIntent);
                launchEditMessageActivity.launch(editIntent);

            }
        });


        /**
         * Handle Edit Send To Button OnClick by starting the activity. This is an example of
         * starting another activity using an explicit Intent.
         */
        Button btnEditPhone;
        btnEditPhone = (Button) this.findViewById(R.id.btnEditPhone);
        btnEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent;
                Activity sourceActivity;
                Class destinationClass;

                sourceActivity = GroupSMS.this;
                destinationClass = EditSendTo.class;

                // Sending information to the intent receiver through the Intent object
                editIntent = new Intent(sourceActivity, destinationClass);
                editIntent.putExtra("CURRENT_PHONE", GroupSMS.this.messageData);

                launchEditSendToActivity.launch(editIntent);

            }
        });

        // Send Button onClick Action uses the SMSManager class to
        // send an SMS. Use an anonymous inner class for practice.
        Button btnSend;
        btnSend = this.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (messageData.getMessage().equals("NOT SET") ){
                    Toast.makeText(getApplicationContext(), "No Message to send", Toast.LENGTH_LONG).show();
                }
                else {
                    SmsManager smsManager = SmsManager.getDefault();
                    for (int i = 0; i < messageData.getNumPhoneNumbers(); i++) {
                        String phone = messageData.getPhoneNumber(i);
                        smsManager.sendTextMessage(phone, null, messageData.getMessage(), null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent to " + phone, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        setSummary();

    }

    /**
     * Put together a summary of the phone and message and display it.
     */
    private void setSummary() {
        StringBuilder summary;
        String phone;
        summary = new StringBuilder("Sending To:\n");
        summary.append(messageData.getNumPhoneNumbers());
        summary.append(" numbers) : \n");
        for (int i = 0; i < messageData.getNumPhoneNumbers(); i++) {
            phone = messageData.getPhoneNumber(i);
            if (phone != null)
                summary.append(phone + (i!=(messageData.getNumPhoneNumbers()-1)?",":""));
        }
        summary.append("\n\nMessage:\n");
        summary.append(messageData.getMessage());
        TextView tvMessageDetails = (TextView) findViewById(R.id.tvMessageDetails);
        tvMessageDetails.setText(summary);
    }


    public class HandleActivityResultForMessage implements ActivityResultCallback {
        /**
         * @param dataIn
         */
        @Override
        public void onActivityResult(Object dataIn) {
            ActivityResult result;
            result = (ActivityResult)dataIn;
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String newMessage = (String) (data.getStringExtra("NEW_MESSAGE"));
                messageData.setMessage(newMessage);
                setSummary();
            }
        }
    }

    /**
     *
     */
    public class HandleActivityResultForPhone implements ActivityResultCallback {
        /**
         * @param dataIn
         */
        @Override
        public void onActivityResult(Object dataIn) {
            ActivityResult result;
            result = (ActivityResult)dataIn;
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String newPhone = (String) (data.getStringExtra("NEW_PHONE"));
                GroupSMS.this.messageData = (SMSDataModelArray) (data.getSerializableExtra("NEW_PHONE"));
                setSummary();
            }
        }
    }




}