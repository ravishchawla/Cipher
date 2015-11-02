/**Ravish Chawla*/
package cipher.root.com.cipher.LayoutAdapters;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import cipher.root.com.cipher.Activity.FileSendActivity;
import cipher.root.com.cipher.Adapters.*;
import cipher.root.com.cipher.Returnable.BooleanReturnable;

/**
 * Listener for the Encrypt button. handles encryption of the file selected.
 */
public class EncryptButtonListener implements View.OnClickListener {

    BooleanReturnable callback;
    public EncryptButtonListener(BooleanReturnable callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View view) {
        try {
            byte[] file = Session.getContentFile().getData();
            byte[] res = Encryption.encrypt(file, Encryption.getEncryptionKey());

            Session.getContentFile().setEncryptedData(res);
            Snackbar.make(view, "File encrypted.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            callback.booleanReturned(FileSendActivity.ENCRYPT_ACTION, true);

        } catch(Exception e) {
            Log.d("EncryptButtonListener", e.getMessage());
        }
    }
}
