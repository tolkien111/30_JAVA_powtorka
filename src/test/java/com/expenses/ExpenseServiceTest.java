package com.expenses;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {

  @Test
  void shouldReturnExpensesWithRequestedDate() {
    // Given
    LocalDate requestedDate = LocalDate.now().minusDays(5);

    Expense expense1 = Expense.from(valueOf(100), requestedDate, "Loc", "Cat");
    Expense expense2 = Expense.from(valueOf(100), requestedDate.minusDays(10), "Loc", "Cat");
    Expense expense3 = Expense.from(valueOf(100), requestedDate, "Loc2", "Cat2");

    ExpenseService expenseService = new ExpenseService();
    expenseService.addExpense(expense1);
    expenseService.addExpense(expense2);
    expenseService.addExpense(expense3);

    // When
    Set<Expense> foundExpenses = expenseService.findByDate(requestedDate);

    // Then
    assertEquals(2, foundExpenses.size());
    assertTrue(foundExpenses.contains(expense1));
    assertTrue(foundExpenses.contains(expense3));
  }

}