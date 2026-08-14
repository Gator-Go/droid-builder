package ppp.ppp.ppp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log; 

import java.util.List;
import java.util.ArrayList;

/**
 * Lllll
 *
 * This is a simple custom ActivityAdapter (extends BaseAdapter) for a ListView.
 * It is a basic adapter that displays a list of plain text items.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class ActivityAdapter extends BaseAdapter {

    private Context context;
    private String[] values;
    private static LayoutInflater inflater = null;

    public ActivityAdapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return values.length;
    }

    public String getItem(int position) {
        return values[position];

    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null)
          row = inflater.inflate(R.layout.list_item, null);

        TextView textView = (TextView) row.findViewById(R.id.ItemView);
        textView.setText(values[position]);
        return row;
    }
}
