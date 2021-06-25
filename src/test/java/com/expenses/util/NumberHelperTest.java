package com.expenses.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NumberHelperTest {

  static Stream<Arguments> getNumbers() {
    return Stream.of(
        Arguments.of("12", true),
        Arguments.of("12.", true),
        Arguments.of("12.1", true),
        Arguments.of("12.12", true),
        Arguments.of("12.120", true),
        Arguments.of("12.012", false),
        Arguments.of("12.123", false),
        Arguments.of("12.1201", false)
    );
  }

  @ParameterizedTest(name = "Number {0} has two or less decimal places: {1}")
  @MethodSource("getNumbers")
  void shouldDetectValidNumberOfDecimalPlaces(String number, boolean expectedResult) {
    // When
    boolean actualResult = NumberHelper.hasTwoOrLessDecimalPlaces(number);

    // Then
    assertEquals(expectedResult, actualResult);
  }

  @ParameterizedTest(name = "Number {0} has two or less decimal places: {1}")
  @MethodSource("getNumbers")
  void shouldDetectValidNumberOfDecimalPlacesUsingLoop(String number, boolean expectedResult) {
    // When
    boolean actualResult = NumberHelper.hasTwoOrLessDecimalPlacesLoop(number);

    // Then
    assertEquals(expectedResult, actualResult);
  }

  @Disabled
  @Test
  void doublePrecisionCheck() {
    // Given
    double x = 0.1;
    double y = 0.2;
    double expectedResult = 0.3;

    // When
    double actualResult = x + y;

    // Then
    assertEquals(expectedResult, actualResult);
  }
}