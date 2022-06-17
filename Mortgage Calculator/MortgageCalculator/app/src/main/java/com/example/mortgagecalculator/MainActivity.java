//MainActivity.java
//Calculates Mortgage Monthly Payments

package com.example.mortgagecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Bundle; // for saving state information
import android.os.TestLooperManager;
import android.text.Editable; // for EditText event handling
import android.text.TextWatcher; // EditText listener
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // for bill amount input
import android.widget.SeekBar; // for changing the tip percentage
import android.widget.SeekBar.OnSeekBarChangeListener; // SeekBar listener
import android.widget.TextView; // for displaying text
import  java.lang.Math; //Needed for the Payment Calculation

import android.os.Bundle;
public class MainActivity extends AppCompatActivity {

    final Context context = this;

    private static final android.icu.text.NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();

    private double purchaseAmount = 0.0; //Purchase Price
    private double downPaymentAmount = 0.0; //Downpayment set to 0
    private double interestRate = 1.0; // Set interest to a valule
    private double value = 0.0; //PP - DP
    private int duration = 30; // Loan length in years

    private TextView homePriceTextView;
    private TextView dpTextView;
    private TextView interestTextView;
    private TextView durationTextView;

    private Button calculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homePriceTextView = (TextView) findViewById(R.id.homePriceTextView);
        dpTextView = (TextView) findViewById(R.id.dpTextView);
        interestTextView = (TextView) findViewById(R.id.interestTextView);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        //_30_year_textView = (TextView) findViewById(R.id._30_year_textView);

        EditText homePriceEditText = (EditText) findViewById(R.id.homePriceEditText);
        homePriceEditText.addTextChangedListener(this.getEditableTextWatcher(homePriceTextView, "PP"));

        EditText dpEditText = (EditText) findViewById(R.id.dpEditText);
        dpEditText.addTextChangedListener(this.getEditableTextWatcher(dpTextView, "DP"));

        EditText interestEditText = (EditText) findViewById(R.id.interestEditText);
        interestEditText.addTextChangedListener(this.getEditableTextWatcher(interestTextView, "IR"));


        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration = progress;
                durationTextView.setText(String.valueOf(progress));
                if (progress <= 0) {
                    seekBar.setProgress(1);
                    durationTextView.setText(String.valueOf(1));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        calculate = (Button) findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double loanAmount = purchaseAmount - downPaymentAmount;
                Loan loan = new Loan(interestRate, duration, loanAmount);

                final AppCompatDialog dialog = new AppCompatDialog(context);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Result");

                TextView monthlyPayment = (TextView) dialog.findViewById(R.id.monthly_payment);
                monthlyPayment.setText(currencyFormat.format(loan.getMonthlyPayment()));

                TextView monthly10 = (TextView) dialog.findViewById(R.id._10_year_textView);
                monthly10.setText(currencyFormat.format(loan.getMonthly10()));

                TextView monthly20 = (TextView) dialog.findViewById(R.id._20_year_textView);
                monthly20.setText(currencyFormat.format(loan.getMonthly20()));

                TextView monthly30 = (TextView) dialog.findViewById(R.id._30_year_textView);
                monthly30.setText(currencyFormat.format(loan.getMonthly30()));

                TextView totalPayment = (TextView) dialog.findViewById(R.id.total_payment);
                totalPayment.setText(currencyFormat.format(loan.getTotalPayment()));

                // reset loan data
                loan.setAnnualInterestRate(0.0);
                loan.setNumberOfYears(1);
                loan.setLoanAmount(0.0);

                Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

        public TextWatcher getEditableTextWatcher ( final TextView textView, final String type){
            return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        value = Double.parseDouble(s.toString()) / 100.0;
                        if (type.equals("PP")) {
                            purchaseAmount = value;
                            textView.setText(currencyFormat.format(value));
                        } else if (type.equals("DP")) {
                            downPaymentAmount = value;
                            textView.setText(currencyFormat.format(value));
                        } else if (type.equals("IR")) {
                            interestRate = value;
                            textView.setText(String.valueOf(value));
                        }
                    } catch (NumberFormatException e) {
                        textView.setText("");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
        }
    }
