package org.roy.trb.tst.credit.line.entities;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREDIT_LINE_REQUESTS")
public class CreditLineRequestRecords {

  @Id
  @Column(unique = true, nullable = false)
  private UUID customerId;

  @Column(nullable = false)
  private BigDecimal acceptedCreditLine;

  @Column(nullable = false, columnDefinition = "varchar(20)")
  private String creditLineStatus;

  @Column(nullable = false, columnDefinition = "timestamp without time zone")
  private ZonedDateTime requestedDate;

  @Column(nullable = false)
  private Integer attempts;
}
