package a5236.android_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreboardFragment extends Fragment {


    private  ArrayList<Player> player_list;
    private ListView mScoreboard;


    public ScoreboardFragment() {
        // Required empty public constructor
    }


    public static ScoreboardFragment newInstance(String[] player_names, int[] player_scores) {
        ScoreboardFragment fragment = new ScoreboardFragment();
        Bundle args = new Bundle();
        args.putStringArray("player_names", player_names);
        args.putIntArray("player_scores", player_scores);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            String[] names = getArguments().getStringArray("player_names");
            int[] scores = getArguments().getIntArray("player_scores");
            for(int i = 0; i<names.length;i++){
                Player temp = new Player(null, names[i]);
                temp.setPoints(scores[i]);
                player_list.add(temp);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        mScoreboard = (ListView) v.findViewById(R.id.playerlist);

        PlayersAdapter adapter = new PlayersAdapter(getContext(), player_list);

        mScoreboard.setAdapter(adapter);



        return v;
    }

    public void displayFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, this);
        transaction.addToBackStack(null);

        transaction.commit();
    }



}
