package org.roy.credit.line.fixture;

import static org.roy.credit.line.constants.BusinessRulesConstants.MAX_NUMBER_OF_FAILED_ATTEMPTS;

import java.math.BigDecimal;
import java.util.Optional;

import org.roy.credit.line.entities.CreditLineRequestRecord;
import org.roy.credit.line.enums.CreditLineStatus;

public class CreditLineEntityFixture {

  public static Optional<CreditLineRequestRecord> mockAlreadyAcceptedRequest() {
    return Optional.of(
        CreditLineRequestRecord.builder()
            .customerId(CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID)
            .acceptedCreditLine(new BigDecimal("10000.00"))
            .creditLineStatus(CreditLineStatus.ACCEPTED.name())
            .requestedDate(CreditLineRequestFixture.MOCKED_REQUESTED_DATE)
            .attempts(MAX_NUMBER_OF_FAILED_ATTEMPTS)
            .build());
  }

  public static Optional<CreditLineRequestRecord> mockAlreadyRejectedRequest(Integer attempts) {
    return Optional.of(
        CreditLineRequestRecord.builder()
            .customerId(CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID)
            .acceptedCreditLine(BigDecimal.ZERO)
            .creditLineStatus(CreditLineStatus.REJECTED.name())
            .requestedDate(CreditLineRequestFixture.MOCKED_REQUESTED_DATE)
            .attempts(attempts)
            .build());
  }
}
