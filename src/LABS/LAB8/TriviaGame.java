package LABS.LAB8;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


abstract class TriviaQuestion {

    public static final int TRUEFALSE = 0;
    public static final int FREEFORM = 1;


    public String question;        // Actual question
    public String answer;        // Answer to question
    public int value;            // Point value of question

    public TriviaQuestion(String q, String a, int v) {
        question = q;
        answer = a;
        value = v;
    }

    abstract public void play();
}


class TrueFalseTQ extends TriviaQuestion {
    public TrueFalseTQ(String q, String a, int v) {
        super(q, a, v);
    }

    @Override
    public void play() {
        System.out.println(question);
        System.out.println("Enter 'T' for true or 'F' for false.");
    }

}

class FreeFormTQ extends TriviaQuestion {
    public FreeFormTQ(String q, String a, int v) {
        super(q, a, v);
    }

    @Override
    public void play() {
        System.out.println(question);
    }
}


class TQFactory {
    static TriviaQuestion create(String q, String a, int v, int t) {
        if (t == 0) {
            return new TrueFalseTQ(q, a, v);
        } else {
            return new FreeFormTQ(q, a, v);
        }
    }
}

class TriviaData {

    private ArrayList<TriviaQuestion> data;

    public TriviaData() {
        data = new ArrayList<TriviaQuestion>();
    }

    public void addQuestion(String q, String a, int v, int t) {
        data.add(TQFactory.create(q, a, v, t));
    }

    public void showQuestion(int index) {
        TriviaQuestion q = data.get(index);
        System.out.println("Question " + (index + 1) + ".  " + q.value + " points.");
        q.play();
    }

    public int numQuestions() {
        return data.size();
    }

    public TriviaQuestion getQuestion(int index) {
        return data.get(index);
    }

    public ArrayList<TriviaQuestion> getData() {
        return data;
    }
}

public class TriviaGame {

    public TriviaData questions;    // Questions

    public TriviaGame() {
        // Load questions
        questions = new TriviaData();
        questions.addQuestion("The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3, TriviaQuestion.FREEFORM);
        questions.addQuestion("Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1, TriviaQuestion.TRUEFALSE);
        questions.addQuestion("1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2, TriviaQuestion.FREEFORM);
        questions.addQuestion("I am the geometric figure most like a lost parrot",
                "polygon", 2, TriviaQuestion.FREEFORM);
        questions.addQuestion("Generics were introducted to Java starting at version 5.0.",
                "T", 1, TriviaQuestion.TRUEFALSE);
    }

    // Main game loop

    public static void main(String[] args) {
        int score = 0;            // Overall score
        int questionNum = 0;    // Which question we're asking
        TriviaGame game = new TriviaGame();
        Scanner keyboard = new Scanner(System.in);
        // Ask a question as long as we haven't asked them all
        while (questionNum < game.questions.numQuestions()) {
            // Show question
            game.questions.showQuestion(questionNum);
            // Get answer
            String answer = keyboard.nextLine();
            // Validate answer
            TriviaQuestion q = game.questions.getQuestion(questionNum);

            if (answer.equalsIgnoreCase(q.answer)) {
                System.out.println("That is correct!  You get " + q.value + " points.");
                score += q.value;
            } else {
                System.out.println("Wrong, the correct answer is " + q.answer);
            }

            System.out.println("Your score is " + score);
            questionNum++;
        }
        System.out.println("Game over!  Thanks for playing!");
    }
}
