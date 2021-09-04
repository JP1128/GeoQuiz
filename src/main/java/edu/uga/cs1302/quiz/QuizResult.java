package edu.uga.cs1302.quiz;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class QuizResult extends RecursiveTreeObject<QuizResult> implements Serializable, Comparable<QuizResult> {
    private final String username;
    private final Date date; // time in which the quiz is started
    private int right; // number of questions gotten right
    private int total; // total number of questions quizzed
    private final List<QuestionResult> questionResults = new LinkedList<>(); // result of each questions

    public QuizResult(String username) {
        this.username = username;
        date = new Date(System.currentTimeMillis());
    } // QuizResult

    public String getUsername() {
        return username;
    }

    public int getRight() {
        return right;
    }

    public int getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public List<QuestionResult> getQuestionResults() {
        return questionResults;
    }

    public void add(World.Country country, String guessed) {
        boolean correct = country.getContinent().equalsIgnoreCase(guessed);
        questionResults.add(new QuestionResult(country.getName(), guessed, country.getContinent(), correct));
        total++;
        if (correct) {
            right++;
        } // if
    } // add

    public static class QuestionResult extends RecursiveTreeObject<QuestionResult> implements Serializable {
        private final String country; // country being tested
        private final String guessed; // continent guessed
        private final String answer; // correct continent
        private final boolean isCorrect; // guessed correctly

        public QuestionResult(String country, String guessed, String answer, boolean isCorrect) {
            this.country = country;
            this.guessed = guessed;
            this.answer = answer;
            this.isCorrect = isCorrect;
        } // Result

        public boolean isCorrect() {
            return isCorrect;
        }

        @Override
        public String toString() {
            return country + ": GUESSED: " + guessed + ", ANSWER: " + answer;
        } // toString

    } // Result

    @Override
    public int compareTo(QuizResult o) {
        return getDate().compareTo(o.getDate());
    } // compareTo

} // QuizResults
