package org.roy.trb.tst.credit.line.fixture;

import static org.roy.trb.tst.credit.line.constants.BusinessRulesRatios.MAX_NUMBER_OF_FAILED_ATTEMPTS;
import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.ACCEPTED;
import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.REJECTED;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_REQUESTED_DATE;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID;

import java.math.BigDecimal;
import java.util.Optional;
import org.roy.trb.tst.credit.line.entities.CreditLineRequestRecord;

public class CreditLineEntityFixture {

  public static Optional<CreditLineRequestRecord> mockAlreadyAcceptedRequest() {
    return Optional.of(
        CreditLineRequestRecord.builder()
            .customerId(MOCKED_UUID_CUSTOMER_ID)
            .acceptedCreditLine(new BigDecimal("10000.00"))
            .creditLineStatus(ACCEPTED.name())
            .requestedDate(MOCKED_REQUESTED_DATE)
            .attempts(MAX_NUMBER_OF_FAILED_ATTEMPTS)
            .build());
  }

  public static Optional<CreditLineRequestRecord> mockAlreadyRejectedRequest(Integer attempts) {
    return Optional.of(
        CreditLineRequestRecord.builder()
            .customerId(MOCKED_UUID_CUSTOMER_ID)
            .acceptedCreditLine(BigDecimal.ZERO)
            .creditLineStatus(REJECTED.name())
            .requestedDate(MOCKED_REQUESTED_DATE)
            .attempts(attempts)
            .build());
  }
}
