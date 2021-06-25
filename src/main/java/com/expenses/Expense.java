package com.expenses;

import com.expenses.util.NumberHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Expense {
  private final BigDecimal amount;
  private final LocalDate date;
  private final String location;
  private final String category;

  private Expense(BigDecimal amount, LocalDate date, String location, String category) {
    this.amount = amount;
    this.date = date;
    this.location = location;
    this.category = category;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getLocation() {
    return location;
  }

  public String getCategory() {
    return category;
  }

  @Override
  public String toString() {
    DateTimeFormatter dateFormat = DateTimeFormatter
        .ofPattern("dd-MM-yyyy");
    String message = "{%s, %s, %s, %s}";

    return String.format(message,
        date.format(dateFormat), amount, location, category);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Expense expense = (Expense) o;
    return Objects.equals(amount, expense.amount) &&
        Objects.equals(date, expense.date) &&
        Objects.equals(location, expense.location) &&
        Objects.equals(category, expense.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, date, location, category);
  }

  public static Expense from(BigDecimal amount,
                             LocalDate date,
                             String location,
                             String category) {

    // todo throwing a runtime exception here is not very useful for users of this class...

    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new RuntimeException();
    }
    if (date == null || date.isAfter(LocalDate.now())) {
      throw new RuntimeException();
    }
    if (location == null || location.isBlank()) {
      throw new RuntimeException();
    }
    if (!NumberHelper.hasTwoOrLessDecimalPlaces(amount.toPlainString())) {
      throw new RuntimeException();
    }
    return new Expense(amount, date, location, category);
  }
}
