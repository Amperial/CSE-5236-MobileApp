package a5236.android_game;


import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import a5236.android_game.multiplayer.GamePlayer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WheelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WheelFragment extends Fragment {

    private NumberPicker mWheel;
    private TextView mWheelText;
    private TextView mRoundText;
    private int round_num;
    private String[] minigames = {"MultipleChoice", "SensorGame"};  //Needs to be populsted with games

    public WheelFragment() {
        // Required empty public constructor
    }

    private String name;

    public static WheelFragment newInstance(String name, int round) {
        WheelFragment fragment = new WheelFragment();
        Bundle args = new Bundle();
        args.putInt("round", round);
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            round_num = getArguments().getInt("round");
            name = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wheel, container, false);


        mWheelText = (TextView) v.findViewById(R.id.wheeltext);
        mRoundText = (TextView) v.findViewById(R.id.round);
        mWheel = (NumberPicker) v.findViewById(R.id.numberPicker);
        String rnd_text = "Round " + String.valueOf(round_num);
        mRoundText.setText(rnd_text);  //Set round number

        mWheel.setWrapSelectorWheel(true);
        mWheel.setDisplayedValues(minigames);
        mWheel.setMinValue(0);
        mWheel.setMaxValue(minigames.length-1);
        mWheel.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String game_chosen = minigames[newVal];
                String game_text = R.string.game_select+game_chosen;
                mWheelText.setText(game_text);

                //Alert all players of chosen game
                //Start the selected minigame for all players
                GamePlayer player = GamePlayer.instance;
                player.sendToHost(player.buildMinigameReadyPacket(player.player));
            }
        });


        return v;
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
