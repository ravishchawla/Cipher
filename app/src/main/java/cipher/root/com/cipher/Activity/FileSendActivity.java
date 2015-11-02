/**Ravish Chawla**/
package cipher.root.com.cipher.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;

import java.util.*;

import cipher.root.com.cipher.Adapters.*;
import cipher.root.com.cipher.LayoutAdapters.*;
import cipher.root.com.cipher.R;
import cipher.root.com.cipher.types.*;
import cipher.root.com.cipher.Returnable.*;

/**
 * Activity for sending files. Handles events for encrypting and sending the file, and responses
 * from the events.
 */
public class FileSendActivity extends AppCompatActivity implements ObjectReturnable<User>, BooleanReturnable{

    private ContentFile fileToSend;
    private EditText fileNameText;
    private Spinner personChooserSpinner;
    private Button encryptButton;
    private Button sendButton;

    public static final int ENCRYPT_ACTION = 0x1;
    public static final int SEND_ACTION = 0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filesend);

        Session.setActiveActivity(this);
        Session.setActiveContext(this);

        fileToSend = Session.getContentFile();
        fileNameText = (EditText)this.findViewById(R.id.fileName);
        personChooserSpinner = (Spinner)this.findViewById(R.id.personChooser);
        encryptButton = (Button)this.findViewById(R.id.encryptButton);
        sendButton = (Button)this.findViewById(R.id.sendButton);
        sendButton.setClickable(false);

        fileNameText.setText(this.fileToSend.getTitle() + "." + this.fileToSend.getType());
        encryptButton.setOnClickListener(new EncryptButtonListener(this));

        sendButton.setOnClickListener(new SendButtonListener(this));

        String[] users = getResources().getStringArray(R.array.users);
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                new ArrayList(Arrays.asList(users)));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personChooserSpinner.setAdapter(adapter);


        String currentUserObject = (String)personChooserSpinner.getItemAtPosition(Session.getSendingUser().getResourcePosition());
        adapter.remove(currentUserObject);
        adapter.notifyDataSetChanged();
        final ObjectReturnable<User> self = this;
        personChooserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ParseAdapter.getInstance().getCurrentUserInfo(adapter.getItem(position), position, self);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
}

    @Override
    protected void onResume() {
        super.onResume();
        Session.setActiveActivity(this);
        Session.setActiveActivity(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void booleanReturned(int request, Boolean result) {

        switch(request) {
            case FileSendActivity.SEND_ACTION:
                if(result == true) {
                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), "File sent succesfully", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null).show();
                    final FileSendActivity self = this;
                    snackbar.setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                                self.finish();
                        }
                    });
                }
                break;
            case FileSendActivity.ENCRYPT_ACTION:
                this.sendButton.setFocusable(result);
                this.sendButton.setClickable(result);
                this.sendButton.setEnabled(result);
                break;
        }
    }

    @Override
    public void objectReturned(User result) {
        Session.setReceivingUser(result);
        Session.getContentFile().updateReceiver();
    }
}
