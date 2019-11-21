package com.lab.mydietary;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements Comparator<Food> {

    private OnFragmentInteractionListener mListener;
    private RecyclerView food_added_list;
    private FoodDatabase db;
    private List<Food> food_list;
    private FoodListAdapter adapter;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("RCV",newText);
                newText=newText.toLowerCase();
                List<Food> newList = new ArrayList<>();
                for (Food food : food_list){
                    String food_name=food.getName().toLowerCase();
                    String user = food.getUser().toLowerCase();
                    String address = food.getAddress().toLowerCase();
                    if (food_name.contains(newText)||user.contains(newText)||address.contains(newText)){
                        newList.add(food);
                    }
                }
                adapter.setFilter(newList);
                return true;
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        db = Room.databaseBuilder(getActivity(),FoodDatabase.class,"Food").allowMainThreadQueries().build();
        food_added_list =(RecyclerView)view.findViewById(R.id.food_added_list);
        FoodDao dao = db.getFoodDao();
        food_list = dao.getFoods();

        if(!(food_list.size() > 0))
            Toast.makeText(getActivity(),"Nothing has been added yet",Toast.LENGTH_SHORT).show();
        else
        {
            Collections.sort(food_list,this);
            Context context = getActivity();
            adapter = new FoodListAdapter(Food.food_groups,Food.images_groups,food_list,context,getFragmentManager(),Food.meals,dao,food_added_list);
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

    @Override
    public int compare(Food o1, Food o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
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
