package org.roy.trb.tst.credit.line.fixture;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.time.ZonedDateTime;
import java.util.UUID;
import org.roy.trb.tst.credit.line.models.requests.PostRequestCreditLineRequestBody;

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
  public static final String MOCKED_STRING_CUSTOMER_ID = "18eee9c2-f577-11ec-b939-0242ac120002";
  public static final UUID MOCKED_UUID_CUSTOMER_ID = UUID.fromString(MOCKED_STRING_CUSTOMER_ID);

  public static final String MOCKED_STRING_REQUESTED_DATE = "2020-01-01T00:00:00Z";
  public static final ZonedDateTime MOCKED_REQUESTED_DATE =
      ZonedDateTime.parse(MOCKED_STRING_REQUESTED_DATE, ISO_DATE_TIME);

  public static PostRequestCreditLineRequestBody mockSmeAcceptableRequest() {

    Float acceptableCreditValue = MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO;
    PostRequestCreditLineRequestBody postRequestCreditLineRequestBody =
        getBaseSmeCreditLineRequest();
    postRequestCreditLineRequestBody.setRequestedCreditLine(acceptableCreditValue);

    return postRequestCreditLineRequestBody;
  }

  public static PostRequestCreditLineRequestBody mockSmeRejectableRequest() {

    Float rejectableCreditValue = MOCKED_SME_MONTHLY_REVENUE / MONTHLY_REVENUE_RATIO + 100F;
    PostRequestCreditLineRequestBody postRequestCreditLineRequestBody =
        getBaseSmeCreditLineRequest();
    postRequestCreditLineRequestBody.setRequestedCreditLine(rejectableCreditValue);

    return postRequestCreditLineRequestBody;
  }

  public static PostRequestCreditLineRequestBody mockStartUpAcceptableRequest() {

    Float acceptableCreditValue = MOCKED_START_UP_SMALLER_CASH_BALANCE / CASH_BALANCE_RATIO;
    PostRequestCreditLineRequestBody postRequestCreditLineRequestBody =
        getBaseStartUpCreditLineRequest();
    postRequestCreditLineRequestBody.setRequestedCreditLine(acceptableCreditValue);

    return postRequestCreditLineRequestBody;
  }

  public static PostRequestCreditLineRequestBody mockStartURejectableRequest() {

    Float rejectableCreditValue = MOCKED_START_UP_SMALLER_CASH_BALANCE / CASH_BALANCE_RATIO + 100F;
    PostRequestCreditLineRequestBody postRequestCreditLineRequestBody =
        getBaseStartUpCreditLineRequest();
    postRequestCreditLineRequestBody.setRequestedCreditLine(rejectableCreditValue);

    return postRequestCreditLineRequestBody;
  }

  private static PostRequestCreditLineRequestBody getBaseSmeCreditLineRequest() {
    return PostRequestCreditLineRequestBody.builder()
        .monthlyRevenue(MOCKED_SME_MONTHLY_REVENUE)
        .cashBalance(MOCKED_SME_CASH_BALANCE)
        .requestedDate(MOCKED_REQUESTED_DATE)
        .build();
  }

  private static PostRequestCreditLineRequestBody getBaseStartUpCreditLineRequest() {
    return PostRequestCreditLineRequestBody.builder()
        .monthlyRevenue(MOCKED_START_UP_SMALLER_MONTHLY_REVENUE)
        .cashBalance(MOCKED_START_UP_SMALLER_CASH_BALANCE)
        .requestedDate(MOCKED_REQUESTED_DATE)
        .build();
  }
}
