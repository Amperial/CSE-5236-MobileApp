package a5236.android_game;

/**
 * Created by Jared on 3/25/2018.
 */

public class MC_Question {
    private String question;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String answer;

    public MC_Question(String q, String a, String b, String c, String d, String ans){
        question = q;
        choiceA = a;
        choiceB = b;
        choiceC = c;
        choiceD = d;
        answer = ans;
    }

    public boolean checkAnswer(String choice){
        return (choice.equals(answer));
    }

    public String getQuestion() {
        return question;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public String getAnswer() {
        return answer;
    }
}
