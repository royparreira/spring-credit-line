package org.roy.trb.tst.credit.line.models;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class CreditLineResponse {

  CreditLineStatus creditLineStatus;
}
