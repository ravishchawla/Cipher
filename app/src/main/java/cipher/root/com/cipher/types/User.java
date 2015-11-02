/**Ravish Chawla*/
package cipher.root.com.cipher.types;

import com.parse.ParseObject;

import java.math.BigInteger;

import cipher.root.com.cipher.Adapters.Session;
import cipher.root.com.cipher.R;

/**
 * Wrapper class for User table in Parse. Consists
 * of member functions and getters and setters for all.
 */
public class User {

    private BigInteger secretColor;
    private BigInteger publicPaint;
    private String username;

    private String id;
    private int resourcePosition;

    public static String CLASS_NAME = "_User";
    public static String NAME_COL = "username";
    public static String ID_COL = "objectId";
    public static String SECRET_COLOR_COL = "secret_color";
    public static String PUBLIC_PAINT_COL = "public_paint";

    public User(ParseObject obj, int resourcePosition) {
        this(obj.getObjectId());
        //this.secretColor = new BigInteger(obj.getString(User.SECRET_COLOR_COL));
        this.publicPaint = new BigInteger(obj.getString(User.PUBLIC_PAINT_COL));
        this.resourcePosition = resourcePosition;

        String[] keys = Session.getActiveActivity().getResources().getStringArray(R.array.keys);
        this.secretColor = new BigInteger(keys[resourcePosition]);

        this.username = obj.getString((User.NAME_COL));
    }

    public User(String id) {
        this.id = id;
    }

    public BigInteger getSecretColor() {
        return this.secretColor;
    }

    public BigInteger getPublicPaint() {
        return this.publicPaint;
    }

    public String getUsername() {
        return this.username;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResourcePosition(int pos) {
        this.resourcePosition = pos;
    }

    public int getResourcePosition() {
        return this.resourcePosition;
    }


}
