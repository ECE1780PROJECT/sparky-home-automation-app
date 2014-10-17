package com.example.hcp.home_control_prototype;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.hcp.home_control_prototype.R;

public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    int selectedIndex = -1;
    public CustomList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.gesture_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.gesture_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        RadioButton rbSelect = (RadioButton) rowView.findViewById(R.id.rb);
        if(selectedIndex == position){
            rbSelect.setChecked(true);
        }
        else{
            rbSelect.setChecked(false);
        }
        return rowView;

    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

}