package au.edu.tafesa.itstudies.groupsms.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import au.edu.tafesa.itstudies.groupsms.R;

public class EditMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);


        // Get the intent for this activity. Every activity has an intent and
        // set the EditText contents to the string in the extra info that comes with
        // the intent
        Intent editIntent;
        EditText etMessage;
        editIntent = this.getIntent();
        String theMessage;
        theMessage = editIntent.getStringExtra("CURRENT_MESSAGE");
        etMessage = (EditText) this.findViewById(R.id.etMessage);
        etMessage.setText(theMessage);

        // Get an event handler going for the Done button so that we can return the
        // new message via an Intent object
        Button btnDone = (Button) this.findViewById(R.id.btnEditMessageDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("NEW_MESSAGE", ((EditText) findViewById(R.id.etMessage)).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}