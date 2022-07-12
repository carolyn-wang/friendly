package com.example.friendly.objects;

public class Preference {
    private String parseKey;
    private String question;
    private String[] options;

    public Preference(String userKey, String question, String[] options) {
        this.parseKey = userKey;
        this.question = question;
        this.options = options;
    }
    public String getParseKey() {
        return parseKey;
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
