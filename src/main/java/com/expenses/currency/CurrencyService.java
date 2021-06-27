package com.expenses.currency;

import java.math.BigDecimal;

public interface CurrencyService {

    BigDecimal convertToPln(BigDecimal amount, Currency currency);
}
