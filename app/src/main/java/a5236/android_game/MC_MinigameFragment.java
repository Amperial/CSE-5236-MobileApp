package a5236.android_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import a5236.android_game.multiplayer.GamePlayer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MC_MinigameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MC_MinigameFragment extends Fragment implements View.OnClickListener {

    private TextView qtext, atext, btext, ctext, dtext;
    private String q, a, b, c, d;

    public MC_MinigameFragment() {
        // Required empty public constructor
    }

    public static MC_MinigameFragment newInstance(String q, String a, String b, String c, String d, String ans) {
        MC_MinigameFragment fragment = new MC_MinigameFragment();
        Bundle args = new Bundle();
        args.putString("q", q);
        args.putString("a", a);
        args.putString("b", b);
        args.putString("c", c);
        args.putString("d", d);
        args.putString("ans", ans);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            q = getArguments().getString("q");
            a = getArguments().getString("a");
            b = getArguments().getString("b");
            c = getArguments().getString("c");
            d = getArguments().getString("d");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mc__minigame, container, false);

        qtext = v.findViewById(R.id.Question);
        atext = v.findViewById(R.id.choiceA);
        btext = v.findViewById(R.id.choiceB);
        ctext = v.findViewById(R.id.choiceC);
        dtext = v.findViewById(R.id.choiceD);

        qtext.setText(q);
        atext.setText(a);
        btext.setText(b);
        ctext.setText(c);
        dtext.setText(d);

        atext.setOnClickListener(this);
        btext.setOnClickListener(this);
        ctext.setOnClickListener(this);
        dtext.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v){
        String answer = "";
        switch(v.getId()){
            case R.id.choiceA:
                answer = a;
                break;
            case R.id.choiceB:
                answer = b;
                break;
            case R.id.choiceC:
                answer = c;
                break;
            case R.id.choiceD:
                answer = d;
                break;
        }
        GamePlayer player = GamePlayer.instance;
        player.sendToHost(player.buildMultipleChoiceSubmitAnswerPacket(player.player, answer));
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
