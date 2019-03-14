package com.example.losts.Posts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.losts.R;


import java.util.ArrayList;

/**
 * Created by Oclemy on 6/21/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 * 1. where WE INFLATE OUR MODEL LAYOUT INTO VIEW ITEM
 * 2. THEN BIND DATA
 */
public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<Post> spacecrafts;

    public CustomAdapter(Context c, ArrayList<Post> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int pos) {
        return spacecrafts.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.model,viewGroup,false);
        }

        TextView DateTxt= (TextView) convertView.findViewById(R.id.Date);
        TextView descTxt= (TextView) convertView.findViewById(R.id.descTxt);

        final Post s= (Post) this.getItem(position);


//        DateTxt.setText(s.getDate());
//
//        descTxt.setText(s.getDescription());
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //OPEN DETAIL
//                openDetailActivity(s.getDescription(),s.getDate());
//            }
//        });

        return convertView;
    }
    //OPEN DETAIL ACTIVITY
    private void openDetailActivity(String...details)
    {
        /*
        Intent i=new Intent(c,Post_detail.class);
        i.putExtra("NAME_KEY",details[0]);
        i.putExtra("DESC_KEY",details[1]);
        i.putExtra("PROP_KEY",details[2]);

        c.startActivity(i);*/
    }
}














