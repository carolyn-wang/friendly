package com.example.friendly.objects;

import java.util.List;

@Deprecated
public class Preference {
    private String question;
    private String[] options;

    public Preference(String question, String[] options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getOption(int index) {
        return options[index];
    }
}
