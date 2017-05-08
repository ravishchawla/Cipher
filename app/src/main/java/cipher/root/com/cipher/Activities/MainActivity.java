/**Ravish Chawla**/
package cipher.root.com.cipher.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.*;
import android.widget.*;

import com.parse.ParseUser;

import java.util.List;

import cipher.root.com.cipher.Adapters.*;
import cipher.root.com.cipher.BuildConfig;
import cipher.root.com.cipher.LayoutAdapters.*;
import cipher.root.com.cipher.Returnable.*;
import cipher.root.com.cipher.R;
import cipher.root.com.cipher.Types.*;

/**
 * Main Activity, responsible for showing List of Messages for users.
 * Allows User selection, and contains handler for sending messages to different users.
 */
public class MainActivity extends AppCompatActivity implements ListReturnable<ContentFile>, ObjectReturnable<User> {

    private static final int FILE_ACCESS_IDENTIFIER = 0x1;

    private ParseAdapter parseAdapter;
    private ListView messagesList;
    private MessageListAdapter listAdapter;

    private ContentFile[] messageFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Session.setActiveActivity(this);
        Session.setActiveContext(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new FileChooser());

        final String[] users = getResources().getStringArray(R.array.users);
        final ObjectReturnable<User> self = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose User to Login as");
        builder.setItems(R.array.users, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseAdapter.getInstance().getCurrentUserInfo(users[which], which, self);
            }
        });

        builder.create().show();

        this.messagesList = (ListView)findViewById(R.id.messagesList);

        this.messagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(getCurrentFocus(), "File downloaded succesfully", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                ContentFile selectedFile = MessageListAdapter.files[position];
                selectedFile.saveFileFromCloud();
            }
        });

        this.parseAdapter = ParseAdapter.getInstance();
        this.parseAdapter.init();
        ParseUser.getCurrentUser().logOut();

        this.updateDynamicPermissions();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ParseAdapter.getInstance().getMessagesList(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public void listReturned(List<ContentFile> results) {

       if(results == null) {
           return;
       }

       Log.d("MainActivityd", results.size() + "");

       for(ContentFile f : results) {
           Log.d("MainActivity", f.getTitle());
       }

       ContentFile[] files = new ContentFile[results.size()];
       this.listAdapter = new MessageListAdapter(results.toArray(files));
       this.messagesList.setAdapter(this.listAdapter);
    }

    //empty method
    public void objectReturned(User result) {

        Session.setSendingUser(result);
        this.parseAdapter.getMessagesList(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FileChooser.FILE_INTENT_CODE) {
            FileChooser.onActivityResult(requestCode, resultCode, data);
        }
    }

	/**
     * Check for Android M permissions, specifically File External Storage
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void updateDynamicPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Local File Access is required to use this app", Toast.LENGTH_LONG).show();
            } else {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.FILE_ACCESS_IDENTIFIER);
            }
        }
    }

	/**
     * Handle user response to Permission Request
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == MainActivity.FILE_ACCESS_IDENTIFIER) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(this, "Local File Access is required to use this app", Toast.LENGTH_LONG).show();
            }
        }

        return;
    }

}
