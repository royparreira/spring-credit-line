package org.roy.trb.tst.credit.line.fixture;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;

public class CreditLineRequestFixture {

  // Ratio
  public static final Integer MONTHLY_REVENUE_RATIO = 5;
  public static final Integer CASH_BALANCE_RATIO = 3;

  // StartUp
  public static final Float MOCKED_START_UP_GREATER_MONTHLY_REVENUE = 150_000.00F;
  public static final Float MOCKED_START_UP_SMALLER_MONTHLY_REVENUE = 15_000.00F;
  public static final Float MOCKED_START_UP_GREATER_CASH_BALANCE = 150_000.00F;
  public static final Float MOCKED_START_UP_SMALLER_CASH_BALANCE = 15_000.00F;

  // SME
  public static final Float MOCKED_SME_MONTHLY_REVENUE = 100_000.00F;
  public static final Float MOCKED_SME_CASH_BALANCE = 100_000F;

  // Common
  public static final ZonedDateTime MOCKED_REQUESTED_DATE =
      ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));

  public static final UUID MOCKED_CUSTOMER_ID =
      UUID.fromString("18eee9c2-f577-11ec-b939-0242ac120002");

  public static CreditLineRequest mockSmeAcceptableRequest() {

    Float acceptableCreditValue = MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO;
    CreditLineRequest creditLineRequest = getBaseSmeCreditLineRequest();
    creditLineRequest.setRequestedCreditLine(acceptableCreditValue);

    return creditLineRequest;
  }

  public static CreditLineRequest mockSmeRejectableRequest() {

    Float rejectableCreditValue = MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO + 100F;
    CreditLineRequest creditLineRequest = getBaseSmeCreditLineRequest();
    creditLineRequest.setRequestedCreditLine(rejectableCreditValue);

    return creditLineRequest;
  }

  public static CreditLineRequest mockStartUpAcceptableRequest() {

    Float acceptableCreditValue = MOCKED_START_UP_SMALLER_CASH_BALANCE / CASH_BALANCE_RATIO;
    CreditLineRequest creditLineRequest = getBaseStartUpCreditLineRequest();
    creditLineRequest.setRequestedCreditLine(acceptableCreditValue);

    return creditLineRequest;
  }

  public static CreditLineRequest mockStartURejectableRequest() {

    Float rejectableCreditValue = MOCKED_START_UP_SMALLER_CASH_BALANCE / CASH_BALANCE_RATIO + 100F;
    CreditLineRequest creditLineRequest = getBaseStartUpCreditLineRequest();
    creditLineRequest.setRequestedCreditLine(rejectableCreditValue);

    return creditLineRequest;
  }

  private static CreditLineRequest getBaseSmeCreditLineRequest() {
    return CreditLineRequest.builder()
        .monthlyRevenue(MOCKED_SME_MONTHLY_REVENUE)
        .cashBalance(MOCKED_SME_CASH_BALANCE)
        .requestedDate(MOCKED_REQUESTED_DATE)
        .build();
  }

  private static CreditLineRequest getBaseStartUpCreditLineRequest() {
    return CreditLineRequest.builder()
        .monthlyRevenue(MOCKED_START_UP_SMALLER_MONTHLY_REVENUE)
        .cashBalance(MOCKED_START_UP_SMALLER_CASH_BALANCE)
        .requestedDate(MOCKED_REQUESTED_DATE)
        .build();
  }
}
