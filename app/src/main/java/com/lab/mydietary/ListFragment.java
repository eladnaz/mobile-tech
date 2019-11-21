package com.lab.mydietary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView food_added_list;
    private FoodDatabase db;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        db = Room.databaseBuilder(getActivity(),FoodDatabase.class,"Food").allowMainThreadQueries().build();
        food_added_list =(RecyclerView)view.findViewById(R.id.food_added_list);
        FoodDao dao = db.getFoodDao();
        List<Food> food_list = dao.getFoods();
        if(!(food_list.size() > 0))
            Toast.makeText(getActivity(),"Nothing has been added yet",Toast.LENGTH_SHORT).show();
        else
        {
            Context context = getActivity();
            FoodListAdapter adapter = new FoodListAdapter(Food.food_groups,Food.images_groups,food_list,context,getFragmentManager(),Food.meals);
            RecyclerView.LayoutManager grid = new GridLayoutManager(getActivity(),2);

            food_added_list.setLayoutManager(grid);
            food_added_list.setHasFixedSize(true);
            food_added_list.setAdapter(adapter);
        }
        // Inflate the layout for this fragment
        return view;




    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){

    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
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
        void onFragmentInteraction(Uri uri);
    }
}
