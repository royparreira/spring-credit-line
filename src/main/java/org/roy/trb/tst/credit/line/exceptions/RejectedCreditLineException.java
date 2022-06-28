package org.roy.trb.tst.credit.line.exceptions;

import lombok.Getter;

public class RejectedCreditLineException extends RuntimeException {

  @Getter private final String customMessage;

  public RejectedCreditLineException(String customMessage) {
    this.customMessage = customMessage;
  }

  public RejectedCreditLineException() {
    customMessage = "";
  }
}
