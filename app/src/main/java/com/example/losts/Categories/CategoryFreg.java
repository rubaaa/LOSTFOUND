package com.example.losts.Categories;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.losts.Posts.Form;
import com.example.losts.Posts.MyPostList;
import com.example.losts.Posts.Post;
import com.example.losts.Posts.listAdapter;
import com.example.losts.Posts.postsList;
import com.example.losts.R;
import com.example.losts.Setting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFreg.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFreg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFreg extends Fragment {
    Query postQuery;
    CustomAdapter mlistAdapter;
    ArrayList<Category> mArrayList;
    Category post;

    public static String tYPE = "LOST";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GridView mainGrid;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoryFreg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFreg.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFreg newInstance(String param1, String param2) {
        CategoryFreg fragment = new CategoryFreg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View RootView = inflater.inflate(R.layout.fragment_category_freg, container, false);
        //radioButton
        ((RadioGroup)RootView.findViewById(R.id.toggle)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()            {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int selectedId) {
                RadioButton srb = ((RadioButton)RootView.findViewById(selectedId));
                tYPE = srb.getText().toString();
                Toast.makeText(getContext(), "selected mode: " + tYPE,  Toast.LENGTH_LONG).show();
            }
        });
        mainGrid =  RootView.findViewById(R.id.mainGrid);

       // LoadData7();

        //Set Event
        setSingleEvent(mainGrid);

        // Inflate the layout for this fragment
        return RootView;
    }



    private void LoadData7() {
        postQuery = FirebaseDatabase.getInstance().getReference().child("Cat");
        // Attach a listener to read the data at our posts reference
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList = new ArrayList<Category>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    post = child.getValue(Category.class);
                    if (post != null) {
                            mArrayList.add(post);


                    }
                }
                mlistAdapter = new CustomAdapter(getActivity(),R.layout.content_grid,mArrayList );
                mainGrid.setAdapter(mlistAdapter);
                if (mArrayList.size() == 0){

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });}











    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
////////////////lostList
    private void setSingleEvent(GridView mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
             final String cat = ((TextView)((ViewGroup)cardView.getChildAt(0)).getChildAt(1)).getText().toString();
            //Toast.makeText(getActivity(), "Selected Cat: " + cat, Toast.LENGTH_SHORT).show();
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                   if(tYPE.equals("LOST")){
                            Intent intent = new Intent(getActivity(), postsList.class);
                            intent.putExtra("mode",tYPE);
                            intent.putExtra("category",  cat);
                            startActivity(intent);


                    } else {

                       Intent intent = new Intent( getActivity() , Form.class);
                       intent.putExtra("category",  cat);
                       startActivity(intent);


                   }

                    /*
                    Intent intent = new Intent(getActivity(),AcivityOne.class);
                    intent.putExtra("info","This is activity from card item index  "+finalI);
                    startActivity(intent);*/

                }
            });
        }
    }
    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
/*
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(CategoriesActivity.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {*/

                    //Change background color
                    cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    Toast.makeText(getActivity(), "State : False", Toast.LENGTH_SHORT).show();
                    //   }
                }
            });
        }
    }

    private void found(){

    }



}
