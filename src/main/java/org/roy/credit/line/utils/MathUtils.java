package org.roy.credit.line.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

  public static BigDecimal roundFloatTwoPlaces(Float numberToRound) {
    return BigDecimal.valueOf(numberToRound).setScale(2, RoundingMode.HALF_UP);
  }
}
