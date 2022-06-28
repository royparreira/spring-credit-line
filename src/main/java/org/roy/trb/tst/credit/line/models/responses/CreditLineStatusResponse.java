package org.roy.trb.tst.credit.line.models.responses;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class CreditLineStatusResponse {

  private UUID customerId;

  private BigDecimal acceptedCreditLine;

  private CreditLineStatus creditLineStatus;

  private ZonedDateTime requestedDate;

  private Integer attempts;
}
