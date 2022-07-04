package org.roy.trb.tst.credit.line.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 30)
public class RejectedCreditCache {

  @Id String rejectedCreditKey;
}
