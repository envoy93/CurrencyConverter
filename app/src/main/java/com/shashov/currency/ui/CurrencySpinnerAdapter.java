package com.shashov.currency.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.shashov.currency.common.Currency;
import com.shashov.currency.currencyconverter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by envoy on 01.07.2017.
 */
public class CurrencySpinnerAdapter  extends ArrayAdapter<String> {
    private List<Currency> langs = new ArrayList<>();

    public CurrencySpinnerAdapter(@NonNull Context context) {
        super(context, R.layout.currency_spinner_item);
    }

    public void addItems(@NonNull List<Currency> items) {
        langs.addAll(items);
        String[] currencies = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            currencies[i] = items.get(i).getName();
        }

        addAll(currencies);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_spinner_item, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.standard_spinner_format);
        textView.setText(langs.get(position).getCharCode().toUpperCase());
        return view;
    }

    @Override
    public String getItem(int position) {
        return langs.get(position).getName();
    }
}
