package a5236.android_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    // TODO: Rename and change types of parameters
    private Player[] players;  //Somehow pass the player list to this
    private ListView mScoreboard;


    public ScoreboardFragment() {
        // Required empty public constructor
    }


    public static ScoreboardFragment newInstance(Player[] param1) {
        ScoreboardFragment fragment = new ScoreboardFragment();
        fragment.players = param1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scoreboard, container, false);
        mScoreboard = (ListView) v.findViewById(R.id.playerlist);
        ArrayList<Player> player_list = new ArrayList<Player>();

        player_list.addAll(Arrays.asList(players));
        Collections.sort(player_list, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p1.getPoints()-p2.getPoints();
            }
        });

        PlayersAdapter adapter = new PlayersAdapter(getContext(), player_list);

        mScoreboard.setAdapter(adapter);



        return v;
    }

}
