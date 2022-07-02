package org.roy.trb.tst.credit.line.docs;

import static org.roy.trb.tst.credit.line.constants.Descriptions.BAD_REQUEST_DESCRIPTION;
import static org.roy.trb.tst.credit.line.constants.Descriptions.CREDIT_LINE_REQUEST_ACCEPTED_DESCRIPTION;
import static org.roy.trb.tst.credit.line.constants.Descriptions.CREDIT_LINE_REQUEST_REJECTED_DESCRIPTION;
import static org.roy.trb.tst.credit.line.constants.Descriptions.INTERNAL_SERVER_ERROR_DESCRIPTION;
import static org.roy.trb.tst.credit.line.constants.Descriptions.TOO_MANY_REQUESTS_DESCRIPTION;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.models.requests.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.responses.ContractResponse;
import org.roy.trb.tst.credit.line.models.responses.PostRequestCreditLineResponseBody;

public interface CreditLineApi {

  @ApiResponse(responseCode = "200", description = CREDIT_LINE_REQUEST_REJECTED_DESCRIPTION)
  @ApiResponse(responseCode = "202", description = CREDIT_LINE_REQUEST_ACCEPTED_DESCRIPTION)
  @ApiResponse(responseCode = "400", description = BAD_REQUEST_DESCRIPTION)
  @ApiResponse(responseCode = "429", description = TOO_MANY_REQUESTS_DESCRIPTION)
  @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR_DESCRIPTION)
  ContractResponse<PostRequestCreditLineResponseBody> validateCreditLine(
      @Valid CreditLineRequest creditLineRequest,
      @Parameter(
              description = "Id of the customer asking for credit.",
              example = "18eee9c2-f577-11ec-b939-0242ac120002",
              required = true)
          UUID customerId,
      @Parameter(required = true, description = "Customer request type of founding")
          FoundingType foundingType,
      HttpServletRequest servlet);
}
