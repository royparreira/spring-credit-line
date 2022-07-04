package org.roy.trb.tst.credit.line.models.requests;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class PostRequestCreditLineRequestBody {

  @Schema(
      required = true,
      description = "The cash balance of the credit requester",
      example = "1000000.99")
  Float cashBalance;

  @Schema(
      required = true,
      description = "The monthly revenue of the credit requester",
      example = "150000.99")
  Float monthlyRevenue;

  @Schema(
      required = true,
      description = "The amount of credit being requested",
      example = "10000.99")
  Float requestedCreditLine;

  @DateTimeFormat(iso = ISO.TIME)
  @Schema(
      required = true,
      description = "Date of the credit line request. Must be in the ISO Time format.",
      example = "2022-06-26T02:14:21.120Z")
  ZonedDateTime requestedDate;
}
