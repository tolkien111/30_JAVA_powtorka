package com.expenses.io;

import com.expenses.Expense;
import com.expenses.util.InvalidExpenseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public class ExpenseCsvMapper {
  private static final String DELIMITER = ",";

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public Set<Expense> read(Path path) throws IOException {
    try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
      Set<Expense> expenses = new HashSet<>();

      // Skip header
      bufferedReader.readLine();
// tutaj trzeba wrzucic try żeby nie dodawał nam nulla do pliku
      String line = bufferedReader.readLine();
      while (line != null) {
        expenses.add(mapFromCsvRow(line));
        line = bufferedReader.readLine();
      }

      return expenses;
    }
  }

  public void write(Set<Expense> expenses, Path path) throws IOException {
    try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {

      bufferedWriter.write("amount,date,location,category\n");

      for (Expense expense : expenses) {
        bufferedWriter.write(mapToCsvRow(expense));
      }
    }
  }

  private String mapToCsvRow(Expense expense) {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(expense.getAmount()).append(DELIMITER);
    stringBuilder.append(DATE_FORMATTER.format(expense.getDate())).append(DELIMITER);
    stringBuilder.append(expense.getLocation()).append(DELIMITER);
    stringBuilder.append(expense.getCategory() == null ? "" : expense.getCategory());
    stringBuilder.append("\n");

    return stringBuilder.toString();
  }

  private Expense mapFromCsvRow(String row) throws IOException {
    String[] columns = row.split(DELIMITER);

    if (columns.length < 3) {
      throw new IOException("Row has too few columns:[" + row + "]");
    }

    String amountText = columns[0];
    BigDecimal amount;
    try {
      amount = new BigDecimal(amountText);
    } catch (NumberFormatException exception) {
      throw new IOException("Cannot parse number in row: [" + amountText + "]", exception);
    }

    String dateText = columns[1];
    LocalDate date;
    try {
      date = LocalDate.parse(dateText, DATE_FORMATTER);
    } catch (DateTimeParseException exception) {
      throw new IOException("Cannot parse date in row: [" + dateText + "]", exception);
    }

    String location = columns[2];

    String category = null;
    if (columns.length > 3) {
      category = columns[3];
    }
//zadanie 2.3
    Expense expense;
    try {
      expense = Expense.from(amount, date, location, category);
    } catch (InvalidExpenseException invalidExpenseException){
      System.out.println(invalidExpenseException.getMessage());
      return null;
  }
    return expense;
  }
}
