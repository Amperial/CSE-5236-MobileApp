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
        question_list[0] = new MC_Question("Which of these countries is not landlocked?", "Zambia" , "Croatia", "Paraguay", "Slovakia", "Croatia");
        question_list[1] = new MC_Question("What percentage of people live North of the Equator?", "50%" , "30%", "70%", "90%", "90%");
        question_list[2] = new MC_Question("Which of these movies does not feature Samuel L. Jackson?", "The Shape of Water" , "The Hateful Eight", "Snakes on a Plane", "Pulp Fiction", "The Shape of Water");
        question_list[3] = new MC_Question("What is the average distance between Earth and Mars?", "100 million km" , "550 million km", "225 million km", "25 million km", "225 million km");

    }

    public MC_Question getRandQuestion(){
        int ind = rnd.nextInt(question_list.length);
        MC_Question q = question_list[ind];
        while(q == null){
            ind = rnd.nextInt(question_list.length);
            q = question_list[ind];
        }
        question_list[ind] = null;
        return q;
    }



}
