package com.aga.hcp.home_control_prototype;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    List<String> gestureList = new ArrayList<String>();
    private List<Integer> imageId = new ArrayList<Integer>();
    int selectedIndex = -1;
    public CustomList(Activity context,
                      List<String> gestureList, List<Integer> imageId) {
        super(context, R.layout.gesture_single, gestureList);
        this.context = context;
        this.gestureList = gestureList;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.gesture_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(gestureList.get(position));

        // TODO: allow user to set new images to gesture
        int imagePosition;
        if(position >1 )
        {
            imagePosition =1 ;
        }else {
            imagePosition = position;
        }
        // above to be removed when finish TODO

        imageView.setImageResource(imageId.get(imagePosition));
        RadioButton rbSelect = (RadioButton) rowView.findViewById(R.id.rb);
        if(selectedIndex == position){
            rbSelect.setChecked(true);
        }
        else{
            rbSelect.setChecked(false);
        }
        return rowView;

    }

    public void setList(List<String> newGestureList) {
        gestureList.addAll(newGestureList);
    }

    public void clearList(){
        gestureList.clear();
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }
    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getGestureName(int position) {
        return gestureList.get(position);
    }

}