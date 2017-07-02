package com.shashov.currency.mvp.common;

import java.io.Serializable;

public class MainViewInputData implements Serializable {
    private int inputCurrencyIndex;
    private int outputCurrencyIndex;
    private String input;

    public MainViewInputData(MainViewInputData data) {
        input = data.input;
        inputCurrencyIndex = data.inputCurrencyIndex;
        outputCurrencyIndex = data.outputCurrencyIndex;
    }

    public MainViewInputData(int inputCurrencyIndex, int outputCurrencyIndex, String input) {
        this.inputCurrencyIndex = inputCurrencyIndex;
        this.outputCurrencyIndex = outputCurrencyIndex;
        this.input = input;
    }

    public int getInputCurrencyIndex() {
        return inputCurrencyIndex;
    }

    public void setInputCurrencyIndex(int inputCurrencyIndex) {
        this.inputCurrencyIndex = inputCurrencyIndex;
    }

    public int getOutputCurrencyIndex() {
        return outputCurrencyIndex;
    }

    public void setOutputCurrencyIndex(int outputCurrencyIndex) {
        this.outputCurrencyIndex = outputCurrencyIndex;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
