/**Ravish Chawla**/
package cipher.root.com.cipher.Adapters;

import android.util.Log;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

import cipher.root.com.cipher.Activities.FileSendActivity;
import cipher.root.com.cipher.Returnable.*;
import cipher.root.com.cipher.Types.*;


/**
 * Wrapper class for all Parse database queries, updates, and functions.
 * This class is a Singleton.
 */
public class ParseAdapter {

    private static ParseAdapter instance;

    public static ParseAdapter getInstance() {

        if(ParseAdapter.instance == null) {
            ParseAdapter.instance = new ParseAdapter();
        }

        return ParseAdapter.instance;
    }

    public static void init() {
        Parse.enableLocalDatastore(Session.getActiveContext());
        Parse.initialize(Session.getActiveContext(), "RZ0f3Bbv1GS3P8ZUK9RpcJ1ZGk2jXdLlCbLX4g7Q", "L9kS3vniCQjwePVvHEBRL84XJQaab8xLQz2z1o87");
    }


    /**Returns list of all messages for current user*/
    public void getMessagesList(final ListReturnable<ContentFile> callback) {
        ParseQuery<ParseObject> messageQuery = ParseQuery.getQuery(ContentFile.TABLE_NAME);
        messageQuery.whereEqualTo(ContentFile.RECEIVER_COL, Session.getSendingUser().getUsername());
        messageQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<ContentFile> files = new ArrayList<ContentFile>();
                    for (ParseObject obj : list) {
                        files.add(new ContentFile(obj));
                    }

                    callback.listReturned(files);
                } else {
                    Log.d("ParseAdapter", "Exception from Parse in getMessagesList " + e.getMessage());
                    callback.listReturned(null);
                }
            }
        });
    }

    /**Returns the information about a user*/
    public void getCurrentUserInfo(String username, final int resourcePosition, final ObjectReturnable<User> callback) {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery(User.CLASS_NAME);
        userQuery.whereEqualTo(User.NAME_COL, username);
        userQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null) {
                    if(list.size() == 1) {
                        User user = new User(list.get(0), resourcePosition);
                        callback.objectReturned(user);
                    } else {
                        Log.d("getCurrentUserInfo", "Returned " + list.size() + " users in query");
                    }
                } else {
                    Log.d("getCurrentUserInfo", "encryption::getcurrentUserInfo threw error " + e.getMessage());
                }
            }
        });
    }

    public void insertNewUser(String username, String secretColor, String publicColor) {
        final ParseUser user2 = new ParseUser();
        user2.setUsername(username);
        user2.setPassword(username);
        user2.put(User.PUBLIC_PAINT_COL, publicColor);
        user2.put(User.SECRET_COLOR_COL, secretColor);
        user2.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    Log.d("ParseAdapter", "uploaded successfullyy");
                } else {
                    Log.d("ParseAdapter", e.getMessage());
                }
            }
        });


    }

    /**Store encrypted data of a file on the database*/
    public void storeMessage(final ContentFile file, final BooleanReturnable callback) {

        final ParseFile pFile = new ParseFile(file.getEncryptedData());
        pFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e != null) {
                    Log.d("ParseAdapter", "Unable to save file " + e.getMessage());
                    callback.booleanReturned(FileSendActivity.SEND_ACTION, new Boolean(false));
                    return;
                }

                final ParseObject obj = new ParseObject(ContentFile.TABLE_NAME);
                obj.put(ContentFile.TYPE_COL, file.getType());
                obj.put(ContentFile.TITLE_COL, file.getTitle());
                obj.put(ContentFile.RECEIVER_COL, file.getReciver());
                obj.put(ContentFile.SENDER_COL, file.getSender());
                obj.put(ContentFile.LEN_COL, file.getLength());
                obj.put(ContentFile.FILE_COL, pFile);
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Boolean success;
                        if(e != null) {
                            Log.d("ParseAdapter", "Unable to save message " + e.getMessage());
                            success = new Boolean(false);
                        } else {
                            success = new Boolean(true);
                        }

                        callback.booleanReturned(FileSendActivity.SEND_ACTION, success);
                    }
                });
            }
        });
    }





}
