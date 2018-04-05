package a5236.android_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import a5236.android_game.multiplayer.MultiplayerClient;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    public static SingleFragmentActivity instance;
    public Fragment current;


    public MultiplayerClient multiplayerClient = null;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        instance = this;


        multiplayerClient = new MultiplayerClient(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
            current = fragment;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        multiplayerClient.leaveGame();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        multiplayerClient.onActivityResult(requestCode, resultCode, intent);
    }

    public static void replaceSingleFragment(Fragment newFragment) {
        FragmentManager fm = instance.getSupportFragmentManager();
        fm.beginTransaction()
                .remove(instance.current)
                .add(R.id.fragment_container, newFragment)
                .commit();
        instance.current = newFragment;
    }
}
