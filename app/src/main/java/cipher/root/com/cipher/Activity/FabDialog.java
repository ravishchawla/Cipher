/**Ravish Chawla**/
package cipher.root.com.cipher.Activity;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ListView;

import cipher.root.com.cipher.Adapters.Session;
import cipher.root.com.cipher.R;

/**
 * A basic Dialog child class, intended for dialog creation and abstract it
 * out of Activities. Class not used because dialogs were eventually not used.
 * Kept for possible future functionality.
 */
public class FabDialog extends AlertDialog {

    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;
    private ListView userList;

    private final static int DIALOG_LAYOUT_RESOURCE = R.layout.activity_choose_user;

    public FabDialog() {
        super(Session.getActiveActivity());

        this.builder = new AlertDialog.Builder(Session.getActiveActivity());
        this.layoutInflater = Session.getActiveActivity().getLayoutInflater();
    }

    public AlertDialog createDialog() {
        this.builder.setView(this.layoutInflater.inflate(DIALOG_LAYOUT_RESOURCE, null));

        return this.builder.create();
    }
}
