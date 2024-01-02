package org.roy.credit.line.models.responses;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roy.credit.line.enums.CreditLineStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@JsonInclude(Include.NON_NULL)
public class PostRequestCreditLineResponseBody {

  CreditLineStatus creditLineStatus;

  BigDecimal acceptedCreditLine;

  String message;
}
