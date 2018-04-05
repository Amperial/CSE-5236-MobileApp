package a5236.android_game;


import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import a5236.android_game.multiplayer.GamePlayer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WheelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WheelFragment extends Fragment implements View.OnClickListener{

    private TextView mRoundText;
    private TextView mWheelText;
    private Button mReadyButton;
    private String minigame;
    private int round_num;
    private String[] minigames = {"MultipleChoice", "SensorGame"};  //Needs to be populsted with games

    public WheelFragment() {
        // Required empty public constructor
    }


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
            minigame = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wheel, container, false);


        mWheelText = (TextView) v.findViewById(R.id.wheeltext);
        mRoundText = (TextView) v.findViewById(R.id.round);
        mReadyButton = (Button) v.findViewById(R.id.minigameready);


        String rnd_text = "Round " + String.valueOf(round_num);
        mRoundText.setText(rnd_text);  //Set round number

        mWheelText.setText(minigame); // Set minigame name

        mReadyButton.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.minigameready:
                GamePlayer player = GamePlayer.instance;
                player.sendToHost(player.buildMinigameReadyPacket(player.player));
        }
    }
}
