package com.shashov.currency.common;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute", strict = false)
public class Currency {
    @Element(name = "NumCode")
    private String numCode;
    @Element(name = "CharCode")
    private String CharCode;
    @Element(name = "Nominal")
    private int nominal;
    @Element(name = "Name")
    private String name;
    @Element(name = "Value")
    private String value;

    public Currency() {
    }

    public Currency(String numCode, String charCode, int nominal, String name, String value) {
        this.numCode = numCode;
        CharCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return CharCode;
    }

    public void setCharCode(String charCode) {
        CharCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return Double.parseDouble(value.replace(",", "."));
    }

    public void setValue(String value) {
        this.value = value;
    }
}
