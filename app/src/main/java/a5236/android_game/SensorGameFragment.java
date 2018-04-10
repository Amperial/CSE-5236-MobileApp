package a5236.android_game;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a5236.android_game.multiplayer.GamePlayer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SensorGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorGameFragment extends Fragment implements SensorEventListener{

    private SensorManager manager;
    private Sensor sensor;
    private long start, end;
    private Vibrator vibe;


    public SensorGameFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SensorGameFragment.
     */
    public static SensorGameFragment newInstance() {
        SensorGameFragment fragment = new SensorGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_sensor_game, container, false);

        if(manager != null) {
            sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        start = System.currentTimeMillis();

        return v;
    }


    @Override
    public void onSensorChanged(SensorEvent event){
        float[] f = new float[3];
        f = event.values.clone();

        float normal_Of_f = (float) Math.sqrt(f[0] * f[0] + f[1] * f[1] + f[2] * f[2]);

        // Normalize the accelerometer vector
        f[0] = f[0] / normal_Of_f;
        f[1] = f[1] / normal_Of_f;
        f[2] = f[2] / normal_Of_f;

        int inclination = (int) Math.round(Math.toDegrees(Math.acos(f[2])));

        //Device is face down
        if(inclination > 170 && inclination < 190){
            end = System.currentTimeMillis();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibe.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
            }else{
                //deprecated in API 26
                vibe.vibrate(500);
            }
            long elapsed = end - start;
            GamePlayer player = GamePlayer.instance;
            player.sendToHost(player.buildSensorSubmitTimePacket(player.player, elapsed));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void displayFragment(){
        SingleFragmentActivity.replaceSingleFragment(this);

    }


    @Override
    public void onPause(){
        super.onPause();
        manager.unregisterListener(this);

    }

    @Override
    public void onResume(){
        super.onResume();
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

}
