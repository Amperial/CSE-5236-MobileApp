package a5236.android_game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;

import a5236.android_game.multiplayer.MultiplayerClient;

public class TitleFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TitleFragment";

    private MultiplayerClient multiplayerClient = null;

    private Button signIn = null;
    private Button signOut = null;
    private Button joinGame = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_title, container, false);
        Log.d(TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle) called");
        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d(TAG, "onAttach(Context) called");
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle) called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        multiplayerClient = new MultiplayerClient(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");

        Activity activity = getActivity();
        signIn = activity.findViewById(R.id.sign_in);
        signOut = activity.findViewById(R.id.sign_out);
        joinGame = activity.findViewById(R.id.join_game);
        signIn.setVisibility(View.GONE);
        signOut.setVisibility(View.VISIBLE);
        joinGame.setVisibility(View.VISIBLE);
        signIn.setOnClickListener(this);
        signOut.setOnClickListener(this);
        joinGame.setOnClickListener(this);

        // Start signed out to force signing in every time
        clickSignOut();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");

        multiplayerClient.leaveGame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView() called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach() called");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in:
                clickSignIn();
                break;
            case R.id.sign_out:
                clickSignOut();
                break;
            case R.id.join_game:
                clickJoinGame();
                break;
            default:
                // Clicked on something else?
        }
    }

    private void clickSignIn() {
        signIn.setClickable(false);
        multiplayerClient.signIn(new MultiplayerClient.SignInCallback() {
            @Override
            public void onComplete(boolean success) {
                if (success) {
                    signIn.setVisibility(View.GONE);
                    signOut.setVisibility(View.VISIBLE);
                    joinGame.setVisibility(View.VISIBLE);
                }
                signIn.setClickable(true);
            }
        });
    }

    private void clickSignOut() {
        signOut.setClickable(false);
        joinGame.setClickable(false);
        multiplayerClient.signOut(new MultiplayerClient.SignOutCallback() {
            @Override
            public void onComplete(boolean success) {
                if (success) {
                    signIn.setVisibility(View.VISIBLE);
                    signOut.setVisibility(View.GONE);
                    joinGame.setVisibility(View.GONE);
                }
                signOut.setClickable(true);
                joinGame.setClickable(true);
            }
        });
    }

    private void clickJoinGame() {
        signOut.setClickable(false);
        joinGame.setClickable(false);
        multiplayerClient.joinGame(new MultiplayerClient.JoinGameCallback() {
            @Override
            public void onComplete(boolean success) {
                signOut.setClickable(true);
                joinGame.setClickable(true);
                if (success) {
                    // change activities...
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        multiplayerClient.onActivityResult(requestCode, resultCode, intent);
    }
}
