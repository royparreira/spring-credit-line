package org.roy.credit.line.models.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequesterFinancialData {

  UUID customerId;

  Float cashBalance;

  Float monthlyRevenue;

  Float requestedCreditLine;

  ZonedDateTime requestedDate;
}
