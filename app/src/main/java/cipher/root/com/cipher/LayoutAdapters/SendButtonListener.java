/**Ravish Chawla*/
package cipher.root.com.cipher.LayoutAdapters;

import android.support.design.widget.Snackbar;
import android.view.View;

import cipher.root.com.cipher.Activity.FileSendActivity;
import cipher.root.com.cipher.Adapters.*;
import cipher.root.com.cipher.Returnable.*;

/**
 * Listener class for Send Button. Handles sending the file to the database.
 */
public class SendButtonListener implements View.OnClickListener {

    BooleanReturnable callback;

    public SendButtonListener(BooleanReturnable callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View view) {
        if(Session.getReceivingUser() == null) {
            Snackbar.make(view, "A user has not been selected.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        ParseAdapter.getInstance().storeMessage(Session.getContentFile(), callback);
    }


}
