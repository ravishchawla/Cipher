/**Ravish Chawla*/
package cipher.root.com.cipher.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cipher.root.com.cipher.Activities.FileSendActivity;
import cipher.root.com.cipher.Types.*;

/**
 * Maintain session information, and provide utility functions for transactions
 * that involve Session variables.
 */
public class Session {

    private static User receivingUser;
    private static User sendingUser;
    private static ContentFile contentFile;

    public static final String MAIN_ACTIVITY = "MainActivity";
    public static final String FILESEND_ACTIVITY = "FileSendActivity";
    public static final String BUNDLE_NAME = "IntentBundle";

    private static Context activeContext;
    private static Activity activeActivity;

    public static Activity getActiveActivity() {
        return Session.activeActivity;
    }

    public static void setActiveActivity(Activity activeActivity) {
        Session.activeActivity = activeActivity;
    }


    public static ContentFile getContentFile() {
        return contentFile;
    }

    public static void setContentFile(ContentFile contentFile) {
        Session.contentFile = contentFile;
    }

    public static Context getActiveContext() {
        return Session.activeContext;
    }

    public static void setActiveContext(Context activeContext) {
        Session.activeContext = activeContext;
    }


    public static User getReceivingUser() {
        return receivingUser;
    }

    public static void setReceivingUser(User receivingUser) {
        Session.receivingUser = receivingUser;
    }

    public static User getSendingUser() {
        return sendingUser;
    }

    public static void setSendingUser(User sendingUser) {
        Session.sendingUser = sendingUser;
    }

    public static void switchUsers() {
        User n = Session.receivingUser;
        Session.receivingUser = Session.sendingUser;
        Session.sendingUser = n;
    }

    public static void startActivity(String activityName, Bundle data) {
        switch(activityName) {
            case Session.FILESEND_ACTIVITY:
                Intent intent = new Intent(Session.activeContext, FileSendActivity.class);

                if(data != null) {
                    intent.putExtra(Session.BUNDLE_NAME, data);
                }

                Session.getActiveContext().startActivity(intent);
        }
    }



}

