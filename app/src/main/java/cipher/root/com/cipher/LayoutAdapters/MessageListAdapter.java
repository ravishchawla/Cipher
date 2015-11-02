/**Ravish Chawla*/
package cipher.root.com.cipher.LayoutAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;

import cipher.root.com.cipher.Adapters.Session;
import cipher.root.com.cipher.R;
import cipher.root.com.cipher.types.ContentFile;

/**
 * Adapter for Messages list. Populates all items in Main list view
 * using ContentFiles for the currently logged in user.
 */
public class MessageListAdapter extends ArrayAdapter<ContentFile> {

    public static ContentFile[] files;

    public static final int view_resource = R.layout.view_message_block;

    public MessageListAdapter(ContentFile[] values) {
        super(Session.getActiveContext(), -1, values);
        files = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) Session.getActiveContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;

        if(rowView == null) {
            rowView = layoutInflater.inflate(MessageListAdapter.view_resource, null);
        }

        ContentFile file = this.files[position];
        TextView titleText = (TextView)rowView.findViewById(R.id.view_message_block_title);
        TextView authorText = (TextView)rowView.findViewById(R.id.view_message_block_author);

        titleText.setText(file.getTitle());
        authorText.setText(file.getSender());

        return rowView;
    }

}
