/**Ravish Chawla*/
package cipher.root.com.cipher.LayoutAdapters;

import android.content.Intent;
import android.view.View;

import java.io.File;
import cipher.root.com.cipher.Adapters.Session;
import cipher.root.com.cipher.types.ContentFile;

/**
 * Adapter for choosing a file. Creates a ContentFile instance
 * from the file selected.
 */
public class FileChooser implements View.OnClickListener{

    public static int FILE_INTENT_CODE = 0x11;

    public FileChooser() {
        super();

    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Session.getActiveActivity().startActivityForResult(intent, FileChooser.FILE_INTENT_CODE);

    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Session.getActiveActivity().RESULT_OK) {

            String uri = data.getData().getPath();
            File file = new File(uri);
            ContentFile localfile = new ContentFile(data.getData());
            localfile.setSender(Session.getSendingUser().getUsername());

            Session.setContentFile(localfile);

            Session.startActivity(Session.FILESEND_ACTIVITY, null);

        }
    }








}
