package a5236.android_game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MC_MinigameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MC_MinigameFragment extends Fragment implements View.OnClickListener {

    private MC_Game mc_game;
    private MC_Question question;
    private TextView q, a, b, c, d;

    public MC_MinigameFragment() {
        // Required empty public constructor
    }

    public static MC_MinigameFragment newInstance() {
        MC_MinigameFragment fragment = new MC_MinigameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mc__minigame, container, false);

        if(mc_game == null) {
            mc_game = new MC_Game();
        }

        question = mc_game.getRandQuestion();

        q = v.findViewById(R.id.Question);
        a = v.findViewById(R.id.choiceA);
        b = v.findViewById(R.id.choiceB);
        c = v.findViewById(R.id.choiceC);
        d = v.findViewById(R.id.choiceD);

        q.setText(question.getQuestion());
        a.setText(question.getChoiceA());
        b.setText(question.getChoiceB());
        c.setText(question.getChoiceC());
        d.setText(question.getChoiceD());

        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);
        d.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v){
        boolean correct;
        switch(v.getId()){
            case R.id.choiceA:
                correct = question.checkAnswer(a.getText().toString());
                if(correct){
                    Toast.makeText(getContext(), "You were Correct!",
                            Toast.LENGTH_SHORT).show();
                    //Give player points
                }
                else{
                    Toast.makeText(getContext(), "You were Incorrect!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.choiceB:
                correct = question.checkAnswer(b.getText().toString());
                if(correct){
                    Toast.makeText(getContext(), "You were Correct!",
                            Toast.LENGTH_SHORT).show();
                    //Give player points
                }
                else{
                    Toast.makeText(getContext(), "You were Incorrect!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.choiceC:
                correct = question.checkAnswer(c.getText().toString());
                if(correct){
                    Toast.makeText(getContext(), "You were Correct!",
                            Toast.LENGTH_SHORT).show();
                    //Give player points
                }
                else{
                    Toast.makeText(getContext(), "You were Incorrect!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.choiceD:
                correct = question.checkAnswer(d.getText().toString());
                if(correct){
                    Toast.makeText(getContext(), "You were Correct!",
                            Toast.LENGTH_SHORT).show();
                    //Give player points
                }
                else{
                    Toast.makeText(getContext(), "You were Incorrect!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




}
