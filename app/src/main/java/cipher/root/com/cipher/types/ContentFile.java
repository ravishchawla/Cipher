/**Ravish Chawla */
package cipher.root.com.cipher.types;

import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import com.parse.*;

import java.io.*;

import cipher.root.com.cipher.Adapters.*;
import cipher.root.com.cipher.Returnable.ObjectReturnable;

/**
 * Local file representation of a file. Uses ParseObject
 * associated with this class to populate its fields.
 * Provides wrapper functions for encrypting and decryptiong files,
 * loading and saving files from disk.
 */
public class ContentFile implements Serializable, ObjectReturnable<User>{

    protected String sender;
    protected String reciver;



    protected String title;

    protected byte[] data;


    private byte[] encryptedData;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private int length;

    protected ParseObject fileObj;

    public static final String CLASS_NAME = "ContentFile";
    public static final String TABLE_NAME = "Messages";
    public static final String FILE_COL = "data";
    public static final String TYPE_COL = "FileType";
    public static final String TITLE_COL = "Title";
    public static final String SENDER_COL = "sender";
    public static final String RECEIVER_COL = "receiver";
    public static String LEN_COL = "length";

    public static final String DOWNLOAD_ROOT = "/storage/emulated/0/Download/";

    public ContentFile(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public ContentFile(ParseObject obj) {
        this(obj.getString(ContentFile.TITLE_COL), obj.getString(ContentFile.TYPE_COL));
        this.sender = obj.getString(ContentFile.SENDER_COL);
        this.reciver = obj.getString(ContentFile.RECEIVER_COL);
        this.sender = obj.getString(ContentFile.SENDER_COL);
        this.length = obj.getInt(ContentFile.LEN_COL);
        this.fileObj = obj;
        ParseAdapter.getInstance().getCurrentUserInfo(this.sender, 0, this);
        this.getEncryptedDataFromCloud();
    }

    public ContentFile(byte[] data, String type) {
        this.data = data;
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int len) {
        this.length = len;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getTitle() {
        return this.title;
    }

    public void updateReceiver() {
        this.reciver = Session.getReceivingUser().getUsername();
    }

    public ContentFile(Uri path) {

        String localFilePath = ContentFile.getLocalFilePath(path);
        String[] dirs = localFilePath.split("\\/");
        if(dirs.length <= 1) {
            return;
        }
        String name = dirs[dirs.length-1];

        String[] nameAndType = name.split("\\.", 2);
        if(dirs.length <= 1) {
            return;
        }


        java.io.File localFile = new java.io.File(localFilePath);
        byte[] data = new byte[(int)localFile.length()];
        Log.d("File::File", (localFile == null) + "");
        try {

            FileInputStream ifstream = new FileInputStream(localFile);
            ifstream.read(data, 0, data.length);
            ifstream.close();
            this.data = data;
            this.title = nameAndType[0];
            this.type = nameAndType[1];
            this.length = data.length;

        } catch(FileNotFoundException e) {
            Log.d("File::File", e.getMessage());
        } catch(IOException e) {
            Log.d("File::File", e.getMessage());
        } catch(Exception e) {
            Log.d("File::File", e.getMessage());
        }
    }

    private static String getLocalFilePath(Uri path) {

        try {

            Cursor cur = Session.getActiveContext().getContentResolver().query(
                    path, null, null, null, null);


            cur.moveToFirst();

            String displayName = cur.getString(cur.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            cur.close();

            return ContentFile.DOWNLOAD_ROOT + displayName;


        } catch(Exception e) {
            Log.d("File::getLocalFilePath", "conversion failure when geting local file path " + e.getMessage());
        }

        return null;
    }




    public void loadFile(final ParseObject obj, final ObjectReturnable<ContentFile> callback) {
        ParseFile parseFile = (ParseFile)obj.get(ContentFile.FILE_COL);
        parseFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if(e == null) {
                    ContentFile file = new ContentFile(bytes, obj.getString(ContentFile.TYPE_COL));
                    callback.objectReturned(file);
                } else {
                    Log.d("File", "error in convertFromParseObject when getting file data " + e.getMessage());
                    callback.objectReturned(null);
                }
            }
        });
    }

    public void saveFileFromCloud() {
        if(encryptedData == null) {
            return;
        }

        byte[] data = Encryption.decrypt(this.encryptedData, Encryption.getEncryptionKey());
        this.data = data;

        String localFilePath = ContentFile.DOWNLOAD_ROOT + "_" + this.getTitle() + "." + this.getType();
        File file = new File(localFilePath);

        if(!file.exists()) {

            try {
                FileOutputStream ostream = new FileOutputStream(file);

                ostream.write(data, 0, this.length);
                ostream.flush();
                ostream.close();

            } catch (Exception e) {
                Log.d("ContentFile::saveFile", e.getMessage());
            }
        }

    }

    public void getEncryptedDataFromCloud() {
        ParseFile pFile = this.fileObj.getParseFile(ContentFile.FILE_COL);
        final ContentFile self = this;

        pFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

                if(e == null) {
                    self.encryptedData = bytes;

                } else {
                    Log.d("ContentFile", e.getMessage());
                }
            }
        });
    }

    public void objectReturned(User user) {
        Session.setReceivingUser(user);

    }
}
