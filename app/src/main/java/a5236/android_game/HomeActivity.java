package a5236.android_game;

import android.support.v4.app.Fragment;

/**
 * Created by cliffy on 3/18/18.
 */

public class HomeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new HomeFragment();
    }
}
