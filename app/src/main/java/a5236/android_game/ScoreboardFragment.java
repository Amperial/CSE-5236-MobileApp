package a5236.android_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreboardFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private Player[] mParam1;
    private ListView mScoreboard;


    public ScoreboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ScoreboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoreboardFragment newInstance(Player[] param1) {
        ScoreboardFragment fragment = new ScoreboardFragment();
        fragment.mParam1 = param1;
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

        return v;
    }

}
