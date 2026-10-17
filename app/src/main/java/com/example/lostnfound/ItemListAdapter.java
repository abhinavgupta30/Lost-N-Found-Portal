package com.example.lostnfound;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> itemList;
    ArrayList<String> imageUris;

    public ItemListAdapter(Context context,
                           ArrayList<String> itemList,
                           ArrayList<String> imageUris) {
        super(context, 0, itemList);

        this.context = context;
        this.itemList = itemList;
        this.imageUris = imageUris;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = LayoutInflater.from(context).inflate(
                    R.layout.item_list_row, parent, false);
        }

        TextView tvItemInfo = row.findViewById(R.id.tvItemInfo);
        ImageView imgItem = row.findViewById(R.id.imgItem);

        tvItemInfo.setText(itemList.get(position));

        String imageUri = imageUris.get(position);

        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                imgItem.setImageURI(Uri.parse(imageUri));
                imgItem.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                imgItem.setVisibility(View.GONE);
            }
        } else {
            imgItem.setVisibility(View.GONE);
        }

        return row;
    }
}
