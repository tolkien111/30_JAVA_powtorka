package com.expenses.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberHelper {
  public static boolean hasTwoOrLessDecimalPlaces(String number) {
    String patternString = "[0-9]*(\\.[0-9]{0,2}0*)?";
    Pattern pattern = Pattern.compile(patternString);

    Matcher matcher = pattern.matcher(number);

    return matcher.matches();
  }

  public static boolean hasTwoOrLessDecimalPlacesLoop(String number) {
    String[] splitByDot = number.split("[.,]");
    if (splitByDot.length < 2) {
      return true;
    }

    String afterDot = splitByDot[1];
    for (int i = afterDot.length() - 1; i >= 0; --i) {
      char digit = afterDot.charAt(i);
      if (digit != '0') {
        return i < 2;
      }
    }

    return true;
  }
}
