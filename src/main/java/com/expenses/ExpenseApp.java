package com.expenses;

import com.expenses.currency.Currency;
import com.expenses.currency.OfflineCurrencyService;
import com.expenses.io.ExpenseCsvMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpenseApp {

  // todo tutaj przydałoby się dependency inversion

  private final Scanner scanner = new Scanner(System.in);
  private final PrintStream printStream = new PrintStream(System.out);
  private final ExpenseService expenseService = new ExpenseService();
  private final ExpenseCsvMapper expenseMapper = new ExpenseCsvMapper();
  private final OfflineCurrencyService currencyService = new OfflineCurrencyService();

  public void run(String dataFilename) {
    loadData(dataFilename);
    run();
    saveData(dataFilename);
  }

  public void run() {
    printStream.println("Hello User!");

    boolean shutdownChosen = false;
    while (!shutdownChosen) {
      printStream.println("MAIN MENU");
      printStream.println("x - exit - application shutdown");
      printStream.println("a - add - add new expense");
      printStream.println("l - list - list expenses");
      printStream.println("c - convert - convert amount to PLN");

      String option = scanner.nextLine();
      if (option.equals("x") || option.equals("exit")) {
        printStream.println("Shutting down");
        shutdownChosen = true;
      } else if (option.equals("a") || option.equals("add")) {
        tryToAddExpense();
      } else if (option.equals("l") || option.equals("list")) {
        listExpenses();
      } else if (option.equals("c") || option.equals("convert")) {
        convertAmount();
      }
    }

    printStream.println("Goodbye!");
  }

  private void tryToAddExpense() {
    printStream.println("Adding new expense");

    printStream.println("Enter date in dd-MM-yyyy format:");
    LocalDate date;
    String enteredDate = scanner.nextLine();
    try {
      date = LocalDate.parse(enteredDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    } catch (DateTimeParseException exception) {
      printStream.println("Entered date " + enteredDate + " is invalid.");
      return;
    }

    printStream.println("Enter amount:");
    BigDecimal amount;
    String enteredAmount = scanner.nextLine();
    try {
      amount = new BigDecimal(enteredAmount);
    } catch (NumberFormatException exception) {
      printStream.println("Entered amount " + enteredAmount + " is invalid.");
      return;
    }

    printStream.println("Enter non-empty location:");
    String location = scanner.nextLine();
    if (StringUtils.isBlank(location)) {
      printStream.println("Entered location is blank.");
      return;
    }

    printStream.println("Enter category (can by empty):");
    String category = scanner.nextLine();

    Expense expense = Expense.from(amount, date, location, category);

    printStream.println("Adding expense: " + expense.toString());
    expenseService.addExpense(expense);
  }

  private void listExpenses() {
    Set<Expense> expenses = expenseService.getExpenses();
    printStream.println("Listing expenses");
    expenses.forEach(printStream::println);
  }

  private void convertAmount() {
    printStream.println("Enter amount:");
    BigDecimal amount;
    String enteredAmount = scanner.nextLine();
    try {
      amount = new BigDecimal(enteredAmount);
    } catch (NumberFormatException exception) {
      printStream.println("Entered amount " + enteredAmount + " is invalid.");
      return;
    }

    printStream.println("Enter currency code:");
    String availableCurrencies = Arrays.stream(Currency.values())
        .map(Currency::getCurrencyCode)
        .collect(Collectors.joining(", "));
    printStream.println("Available codes: [" + availableCurrencies + "]");

    String enteredCode = scanner.nextLine();
    Optional<Currency> chosenCurrency = Currency.findByCode(enteredCode);
    if (chosenCurrency.isEmpty()) {
      printStream.println("Unknown currency code: [" + enteredCode + "]");
      return;
    }

    BigDecimal convertedAmount = currencyService.convertToPln(amount, chosenCurrency.get());
    printStream.println(amount + " " + enteredCode + " = "
        + convertedAmount + " " + Currency.POLISH_ZLOTY.getCurrencyCode());
  }

  private void loadData(String filename) {
    printStream.println("Loading data from " + filename);

    Set<Expense> expenses;
    try {
      expenses = expenseMapper.read(Paths.get(filename));
    } catch (IOException exception) {
      printStream.println("Could not load data from " + filename + ": " + exception.getMessage());
      return;
    }

    expenses.forEach(expenseService::addExpense);
    printStream.println("Successfully loaded " + expenses.size() + " expenses.");
  }

  private void saveData(String filename) {
    printStream.println("Saving data to " + filename);

    try {
      expenseMapper.write(expenseService.getExpenses(), Paths.get(filename));
    } catch (IOException exception) {
      printStream.println("Could not save data to " + filename + ": " + exception.getMessage());
    }
  }
}
