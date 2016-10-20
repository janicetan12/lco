package com.janice_tan.lco_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by janice on 19/10/16.
 */

public class PlacesArrayAdapter extends ArrayAdapter <Place> {

    private static final String TAG = PlacesArrayAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Place> placeList;

    public PlacesArrayAdapter(Context context, int resource, ArrayList<Place> placeList) {
        super(context, resource, placeList);

        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        Place place = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //View view = inflater.inflate(R.layout.row_place, null);
            convertView = inflater.inflate(R.layout.row_place, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.place_title);
            viewHolder.category = (TextView) convertView.findViewById(R.id.place_category);
            //viewHolder.rating = (TextView) convertView.findViewById(R.id.place_rating);
            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.place_layout);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.place_rating_bar);

            //LayerDrawable stars = (LayerDrawable) viewHolder.ratingBar.getProgressDrawable();
            //stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            //stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            //stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);


            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(place.getName());
        viewHolder.category.setText(((Category) place.getCategory()).getName() + " (" + place.getDisplayDate() + ")");
        //viewHolder.rating.setText("" + place.getRating());

        String fileName = place.getImagePath();
        int dotIndex = fileName.lastIndexOf('.');
        if(dotIndex >= 0) {
            fileName = fileName.substring(0, dotIndex);
        }

        String uri = "@drawable/" + fileName;
        Log.d(TAG, uri);
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(imageResource, null);
        viewHolder.layout.setBackground(drawable);

        viewHolder.ratingBar.setRating(place.getRating());

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView category;
        //TextView rating;
        RelativeLayout layout;
        RatingBar ratingBar;
    }

}
