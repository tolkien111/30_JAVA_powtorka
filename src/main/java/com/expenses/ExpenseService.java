package com.expenses;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ExpenseService {
  private final Set<Expense> expenses = new HashSet<>();

  public void addExpense(Expense expense) {
    expenses.add(expense);
  }

  public Set<Expense> getExpenses() {
    return new HashSet<>(expenses);
  }

  public Set<Expense> findByDate(LocalDate date) {
    Set<Expense> expensesWithRequestedDate = new HashSet<>();

    for (Expense expense : expenses) {
      if (expense.getDate().equals(date)) {
        expensesWithRequestedDate.add(expense);
      }
    }

    return expensesWithRequestedDate;
  }

  public String toString() {
    StringBuilder message = new StringBuilder("Expenses:\n");

    for (Expense expense : expenses) {
      message.append(expense).append("\n");
    }

    return message.toString();
  }
}
