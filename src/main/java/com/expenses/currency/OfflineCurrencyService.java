package com.expenses.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class OfflineCurrencyService implements CurrencyService {
  private static final Map<Currency, BigDecimal> RATES = Map.of(
      Currency.POLISH_ZLOTY, BigDecimal.ONE,
      Currency.US_DOLLAR, BigDecimal.valueOf(3.5),
      Currency.BRITISH_POUND, BigDecimal.valueOf(5),
      Currency.EURO, BigDecimal.valueOf(4)
  );

  public BigDecimal convertToPln(BigDecimal amount, Currency currency) {
    BigDecimal rate = RATES.get(currency);

    if (rate == null) {
      throw new IllegalArgumentException("Unknown currency [" + currency + "]");
    }
    return rate.multiply(amount).setScale(2, RoundingMode.HALF_UP);
  }
}
