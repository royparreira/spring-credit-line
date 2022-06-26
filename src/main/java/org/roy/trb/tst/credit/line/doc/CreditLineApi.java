package org.roy.trb.tst.credit.line.doc;

import static org.roy.trb.tst.credit.line.constants.Descriptions.BAD_REQUEST_DESCRIPTION;
import static org.roy.trb.tst.credit.line.constants.Descriptions.CREDIT_LINE_REQUEST_ACCEPTED_DESCRIPTION;
import static org.roy.trb.tst.credit.line.constants.Descriptions.CREDIT_LINE_REQUEST_REJECTED_DESCRIPTION;
import static org.roy.trb.tst.credit.line.constants.Descriptions.INTERNAL_SERVER_ERROR_DESCRIPTION;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;
import org.springframework.http.ResponseEntity;

public interface CreditLineApi {

  @ApiResponse(responseCode = "200", description = CREDIT_LINE_REQUEST_REJECTED_DESCRIPTION)
  @ApiResponse(responseCode = "202", description = CREDIT_LINE_REQUEST_ACCEPTED_DESCRIPTION)
  @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION)
  @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR_DESCRIPTION)
  ResponseEntity<CreditLineResponse> validateCreditLine(@Valid CreditLineRequest creditLineRequest);
}
