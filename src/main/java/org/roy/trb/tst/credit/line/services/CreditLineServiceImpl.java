package org.roy.trb.tst.credit.line.services;

import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;
import org.springframework.stereotype.Service;

@Service
public class CreditLineServiceImpl implements ICreditLineService {

  @Override
  public CreditLineResponse validateCreditLine(CreditLineRequest creditLineRequest) {
    return CreditLineResponse.builder().creditLineStatus(CreditLineStatus.ACCEPTED).build();
  }
}
