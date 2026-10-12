package com.example.smartexpensetracker.api;

import com.example.smartexpensetracker.model.Expense;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExpenseApi {

    @GET("api/expenses/")
    Call<List<Expense>> getExpenses();

    @POST("api/expenses/")
    Call<Expense> createExpense(
            @Body Expense expense
    );

    @PUT("api/expenses/{id}/")
    Call<Expense> updateExpense(
            @Path("id") int expenseId,
            @Body Expense expense
    );

    @DELETE("api/expenses/{id}/")
    Call<Void> deleteExpense(
            @Path("id") int expenseId
    );
}