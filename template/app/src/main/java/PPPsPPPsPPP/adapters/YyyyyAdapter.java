package ppp.ppp.ppp;

import ppp.ppp.ppp.pojos.Yyyyy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager; 

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import java.io.File;
import android.os.Environment;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class YyyyyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Yyyyy> yyyyys;
    private LayoutInflater inflater;
    private SharedPreferences sharedPrefs;
    private String dateFormat;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat dateTimeFormatter;
    private ImageHelper imageHelper = new ImageHelper();

    YyyyyAdapter() {
        yyyyys = null;
    }

    public YyyyyAdapter(Context context, ArrayList<Yyyyy> yyyyys) {
        this.context = context;
        this.yyyyys = yyyyys;
        this.inflater = LayoutInflater.from(context);
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.dateFormat = sharedPrefs.getString("prefDateFormat", "yyyy/MM/dd");
        this.formatDate = new SimpleDateFormat (dateFormat);
        this.dateTimeFormatter = new SimpleDateFormat (dateFormat + " hh:mm");
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return yyyyys.size();
    }

    public Yyyyy getItem(int position) {
        return yyyyys.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        boolean showPic = false;
        View row;
        row = inflater.inflate(R.layout.list_item_pic, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.ItemText);
        ImageView imageView = (ImageView) row.findViewById(R.id.ItemPic);

        Yyyyy obj = yyyyys.get(position);

        String result = String.format(
           ___LOAD_LIST_FIELDS___
        );
        textView.setText(result);

___LOAD_LIST_CAMERA___
___LOAD_LIST_VIDEO___
___LOAD_LIST_THUMBNAIL___
___LOAD_LIST_POST___

        if (showPic == false)
          imageView .setVisibility(View.GONE);

        return (row);
    }
}