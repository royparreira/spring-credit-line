package org.roy.trb.tst.credit.line.models;

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

  Float cashBalance;

  Float monthlyRevenue;

  Float requestedCredit;
}
