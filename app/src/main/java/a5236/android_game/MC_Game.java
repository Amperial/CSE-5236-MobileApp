package a5236.android_game;

import java.util.Random;

/**
 * Created by Jared on 3/26/2018.
 */

public class MC_Game {
    private MC_Question[] question_list;
    private Random rnd = new Random();

    public MC_Game(){
        question_list = new MC_Question[4];
        question_list[0] = new MC_Question("@string/q1q", "string/q1a" , "@string/q1b", "@string/q1c", "@string/q1d", "@string/q1b");
        question_list[1] = new MC_Question("@string/q2q", "string/q2a" , "@string/q2b", "@string/q2c", "@string/q2d", "@string/q2d");
        question_list[2] = new MC_Question("@string/q3q", "string/q3a" , "@string/q3b", "@string/q3c", "@string/q3d", "@string/q3a");
        question_list[3] = new MC_Question("@string/q4q", "string/q4a" , "@string/q4b", "@string/q4c", "@string/q4d", "@string/q4c");

    }

    public MC_Question getRandQuestion(){

        return question_list[rnd.nextInt(question_list.length)];
    }



}
