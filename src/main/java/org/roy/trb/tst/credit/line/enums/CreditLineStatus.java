package org.roy.trb.tst.credit.line.enums;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CreditLineStatus {
  ACCEPTED(Bandwidth.classic(2, Refill.smooth(2, Duration.ofMinutes(2)))),

  REJECTED(Bandwidth.classic(1, Refill.smooth(1, Duration.ofSeconds(30))));

  @Getter private final Bandwidth limit;
}
