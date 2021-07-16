package com.chupe.fitbud.views;

public class IntegerLooper {
    private int value;

    private int min;
    private int max;

    public int getValue() {
        return value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public IntegerLooper(int from, int to) {
        min = from;
        max = to;

        value = min;
    }

    public int add(int val) {

        for (int i=0; i<val; i++)
            add_one();

        return value;
    }

    private int add_one() {
        if (value+1 == max) {
            value = 0;
            return value;
        }
        else
            return value++;
    }

    public int sub(int val) {
        for (int i=0; i<val; i++)
            sub_one();

        return value;
    }

    private int sub_one() {
        if (value-1 < min) {
            value = max-1;
            return value;
        }
        else
            return value--;
    }

}
