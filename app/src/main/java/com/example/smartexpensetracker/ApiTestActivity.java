package com.example.smartexpensetracker;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartexpensetracker.api.ExpenseApi;
import com.example.smartexpensetracker.api.RetrofitClient;
import com.example.smartexpensetracker.model.Expense;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTestActivity extends AppCompatActivity {

    private static final String TAG = "API_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_api_test);
        Log.d(TAG, "ApiTestActivity started");

        ExpenseApi api =
                RetrofitClient.getExpenseApi();

        Call<List<Expense>> call =
                api.getExpenses();

        call.enqueue(new Callback<List<Expense>>() {

            @Override
            public void onResponse(
                    Call<List<Expense>> call,
                    Response<List<Expense>> response) {

                if (response.isSuccessful()) {

                    List<Expense> expenses =
                            response.body();

                    Log.d(
                            TAG,
                            "Expenses: "
                                    + expenses
                    );

                } else {

                    Log.e(
                            TAG,
                            "HTTP Error: "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<List<Expense>> call,
                    Throwable t) {

                Log.e(
                        TAG,
                        "API Error",
                        t
                );
            }
        });
    }
}