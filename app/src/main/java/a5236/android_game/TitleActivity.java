package a5236.android_game;

import android.support.v4.app.Fragment;
import android.os.Bundle;

public class TitleActivity extends SingleFragmentActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_title);
    }*/

    @Override
    protected Fragment createFragment() {
        return new TitleFragment();
    }
}
