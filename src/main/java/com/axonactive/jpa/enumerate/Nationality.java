package com.axonactive.jpa.enumerate;

public enum Nationality {
    VIETNAM(1),
    USA(2),
    GERMANY(3);

    private int value;

    Nationality(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


