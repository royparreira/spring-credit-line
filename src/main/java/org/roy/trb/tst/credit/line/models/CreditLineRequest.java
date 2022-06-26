package org.roy.trb.tst.credit.line.models;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class CreditLineRequest {
  @NotNull
  @Schema(required = true, description = "Type of founding")
  FoundingType foundingType;

  @NotNull
  @Schema(
      required = true,
      description = "The cash balance of the credit requester",
      example = "1000000.99")
  BigDecimal cashBalance;

  @NotNull
  @Schema(
      required = true,
      description = "The monthly revenue of the credit requester",
      example = "150000.99")
  BigDecimal monthlyRevenue;

  @NotNull
  @Schema(
      required = true,
      description = "The amount of credit being requested",
      example = "10000.99")
  BigDecimal requestedCreditLine;

  @DateTimeFormat(iso = ISO.TIME)
  @Schema(
      required = true,
      description = "Date of the credit line request. Must be in the ISO Time format",
      example = "2022-06-26T02:14:21.120Z")
  ZonedDateTime requestedDate;
}
