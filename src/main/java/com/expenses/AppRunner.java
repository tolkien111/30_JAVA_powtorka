package com.expenses;

public class AppRunner {
  public static void main(String[] args) {
    ExpenseApp expenseApp = new ExpenseApp();

    expenseApp.run("data.csv");
  }
}
