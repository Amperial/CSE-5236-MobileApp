package a5236.android_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WheelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WheelFragment extends Fragment {

    private NumberPicker mWheel;
    private TextView mWheelText;
    private String[] minigames;  //Needs to be populsted with games


    public WheelFragment() {
        // Required empty public constructor
    }


    public static WheelFragment newInstance() {
        WheelFragment fragment = new WheelFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wheel, container, false);


        mWheelText = (TextView) v.findViewById(R.id.wheeltext);
        mWheel = (NumberPicker) v.findViewById(R.id.numberPicker);

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
            }
        });


        return v;
    }

}
