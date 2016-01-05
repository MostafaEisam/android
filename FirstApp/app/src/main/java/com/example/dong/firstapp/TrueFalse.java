package com.example.dong.firstapp;

/**
 * Created by Dong on 2015-05-24.
 */
public class TrueFalse {
    private int mQuestion;

    private boolean mTrueQuestion;

    public TrueFalse(int Question, boolean TrueQuestion) {
        this.mQuestion = Question;
        this.mTrueQuestion = TrueQuestion;
    }

    public int getQuestion() {
        return mQuestion;
    }

    public void setQuestion(int mQuestion) {
        this.mQuestion = mQuestion;
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }

    public void setTrueQuestion(boolean mTrueQuestion) {
        this.mTrueQuestion = mTrueQuestion;
    }
}
