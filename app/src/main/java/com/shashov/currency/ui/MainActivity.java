package com.shashov.currency.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.shashov.currency.common.Currency;
import com.shashov.currency.currency.R;
import com.shashov.currency.mvp.PresenterManager;
import com.shashov.currency.mvp.common.MainViewInputData;
import com.shashov.currency.mvp.presenters.BasePresenter;
import com.shashov.currency.mvp.presenters.MainPresenter;
import com.shashov.currency.mvp.views.MainView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    private EditText etInput;
    private Spinner spInput;
    private Spinner spOutput;
    private TextView tvResultCaption1;
    private TextView tvResultCaption2;
    private TextView tvOutput;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = (EditText) findViewById(R.id.et_input);
        spInput = (Spinner) findViewById(R.id.sp_input);
        spOutput = (Spinner) findViewById(R.id.sp_output);
        tvOutput = (TextView) findViewById(R.id.tv_output);
        tvResultCaption1 = (TextView) findViewById(R.id.tv_result_caption1);
        tvResultCaption2 = (TextView) findViewById(R.id.tv_result_caption2);
        ImageButton ibSwap = (ImageButton) findViewById(R.id.ib_swap);
        Button ibConvert = (Button) findViewById(R.id.btn_convert);

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
    public void showResult(@NonNull String result, @NonNull String caption1, @NonNull String caption2) {
        tvOutput.setText(result);
        tvResultCaption1.setVisibility(caption1.isEmpty() ? View.GONE : View.VISIBLE);
        tvResultCaption2.setVisibility(caption2.isEmpty() ? View.GONE : View.VISIBLE);
        tvResultCaption1.setText(caption1);
        tvResultCaption2.setText(caption2);
    }

    @Override
    public void showInputData(@NonNull MainViewInputData inputData) {
        etInput.setText(inputData.getInput());
        spOutput.setSelection(inputData.getOutputCurrencyIndex());
        spInput.setSelection(inputData.getInputCurrencyIndex());
    }

    @Override
    public void populateCurrencies(@NonNull List<Currency> currencies) {
        spInput.setAdapter(new CurrencySpinnerAdapter(this));
        spOutput.setAdapter(new CurrencySpinnerAdapter(this));
        ((CurrencySpinnerAdapter) spInput.getAdapter()).addItems(currencies);
        ((CurrencySpinnerAdapter) spOutput.getAdapter()).addItems(currencies);
    }

    @Override
    public void showLoadingScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, LoadingActivity.class));
            }
        });
    }

    @Override
    public void swapCurrencies() {
        int r = spInput.getSelectedItemPosition();
        spInput.setSelection(spOutput.getSelectedItemPosition());
        spOutput.setSelection(r);
    }

    @Override
    public void showError() {
        showResult(getString(R.string.InvalidResult), "", "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.bindView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unbindView();
    }
}
