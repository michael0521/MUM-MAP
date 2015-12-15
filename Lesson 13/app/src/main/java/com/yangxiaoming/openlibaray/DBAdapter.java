package com.yangxiaoming.openlibaray;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;


public class DBAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<Map<String, String>> arrayList;

    public DBAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        if (arrayList == null) return 0;
        else
            return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // your particular dataset uses String IDs
        // but you have to put something in this method return position;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.activity_main, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.book_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.book_title);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.book_author);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {
            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        String imageURL = "";
        String bookTitle = "";
        String authorName = "";

        Map<String, String> map = arrayList.get(position);

        if (map != null) {

            imageURL = map.get("cover_i");
            bookTitle = map.get("title");
            authorName = map.get("author_name");
            /*
            Log.d("url", imageURL);
            Log.d("title", bookTitle);
            Log.d("author_name", authorName);
            */
            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            if (!imageURL.equals(""))
                Picasso.with(mContext).load(imageURL).placeholder(R.drawable.book_cover).into(holder.thumbnailImageView);
            else {
                // If there is no cover ID in the object, use a placeholder
                holder.thumbnailImageView.setImageResource(R.drawable.book_cover);
            }
        }

        // Send these Strings to the TextViews for display
        holder.titleTextView.setText(bookTitle);
        holder.authorTextView.setText(authorName);

        return convertView;
    }

    public void updateData(ArrayList<Map<String, String>> list) {
        // update the adapter's dataset
        arrayList = list;
        //Log.d("resultSet amount", String.valueOf(resultSet.getCount()));
        notifyDataSetChanged();
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
}
