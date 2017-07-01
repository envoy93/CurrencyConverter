package com.shashov.currency.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.shashov.currency.common.Currency;
import com.shashov.currency.currencyconverter.R;
import com.shashov.currency.mvp.PresenterManager;
import com.shashov.currency.common.MainViewInputData;
import com.shashov.currency.mvp.presenters.BasePresenter;
import com.shashov.currency.mvp.presenters.MainPresenter;
import com.shashov.currency.mvp.views.MainView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    private LinearLayout llConverter;
    private EditText etInput;
    private Spinner spInput;
    private Spinner spOutput;
    private TextView tvOutput;
    private ImageButton ibSwap;
    private ImageButton ibConvert;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = (EditText) findViewById(R.id.et_input);
        spInput = (Spinner) findViewById(R.id.sp_input);
        spOutput = (Spinner) findViewById(R.id.sp_output);
        tvOutput = (TextView) findViewById(R.id.tv_output);
        llConverter = (LinearLayout) findViewById(R.id.ll_converter);
        ibSwap = (ImageButton) findViewById(R.id.ib_swap);
        ibConvert = (ImageButton) findViewById(R.id.ib_convert);

        presenter = (MainPresenter) PresenterManager.getInstance().getPresenter(this);

        ibConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClickConvert(etInput.getText().toString().trim(), spInput.getSelectedItemPosition(), spOutput.getSelectedItemPosition());
            }
        });

        ibSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClickSwapCurrencies();
            }
        });

    }

    @Override
    public BasePresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void showResult(String result) {
        tvOutput.setText(result);
    }

    @Override
    public void showInputData(MainViewInputData inputData) {
        etInput.setText(inputData.getInput());
        spOutput.setSelection(inputData.getOutputCurrencyIndex());
        spInput.setSelection(inputData.getInputCurrencyIndex());
    }

    @Override
    public void populateCurrencies(List<Currency> currencies) {
        spInput.setAdapter(new CurrencySpinnerAdapter(this));
        spOutput.setAdapter(new CurrencySpinnerAdapter(this));
        ((CurrencySpinnerAdapter) spInput.getAdapter()).addItems(currencies);
        ((CurrencySpinnerAdapter) spOutput.getAdapter()).addItems(currencies);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }
}
