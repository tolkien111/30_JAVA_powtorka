package com.expenses.currency;

import java.util.Arrays;
import java.util.Optional;

public enum Currency {
  US_DOLLAR("USD"),
  BRITISH_POUND("GBP"),
  EURO("EUR"),
  POLISH_ZLOTY("PLN");

  private final String currencyCode;

  Currency(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public static Optional<Currency> findByCode(String currencyCode) {
    return Arrays.stream(Currency.values())
        .filter(currency -> currency.getCurrencyCode().equals(currencyCode))
        .findAny();
  }
}
