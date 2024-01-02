package org.roy.credit.line.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessRulesConstants {

  /**
   * After a credit line request being rejected that many times, the response body will contain the
   * message: "A sales agent will contact you"
   *
   * <p>Configurable changing application.yml ratio.max-failed-attempts property, default = 3
   */
  public static final Integer MAX_NUMBER_OF_FAILED_ATTEMPTS = 3;

  /**
   * Ratio for calculate recommended credit line based on the requester monthly revenue
   *
   * <p>Configurable changing application.yml ratio.monthly-revenue property, default = 5
   */
  public static final Integer MONTHLY_REVENUE_RATIO = 5;

  /**
   * Ratio for calculate recommended credit line based on the requester cash balance
   *
   * <p>Configurable changing application.yml cash-balance property, default = 3
   */
  public static final Integer CASH_BALANCE_RATIO = 3;
}
