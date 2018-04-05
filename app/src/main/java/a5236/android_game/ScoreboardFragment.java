package a5236.android_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import a5236.android_game.multiplayer.GamePlayer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreboardFragment extends Fragment implements View.OnClickListener {


    private  ArrayList<Player> player_list = new ArrayList<>();
    private ListView mScoreboard;

    private Button mContinueButton;


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

        mContinueButton = v.findViewById(R.id.continueButton);
        mContinueButton.setOnClickListener(this);

        PlayersAdapter adapter = new PlayersAdapter(getContext(), player_list);

        mScoreboard.setAdapter(adapter);



        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueButton:
                GamePlayer player = GamePlayer.instance;
                player.sendToHost(player.buildScoreboardContinuePacket(player.player));
        }
    }

    public void displayFragment(){
        SingleFragmentActivity.replaceSingleFragment(this);
        /*
        TitleActivity.activity.replaceCurrent(this);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, this);
        transaction.addToBackStack(null);

        transaction.commit();
        */
    }

}
