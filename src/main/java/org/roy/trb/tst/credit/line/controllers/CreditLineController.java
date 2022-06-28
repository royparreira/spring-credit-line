package org.roy.trb.tst.credit.line.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.roy.trb.tst.credit.line.docs.CreditLineApi;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.models.requests.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.responses.ContractResponse;
import org.roy.trb.tst.credit.line.models.responses.CreditLineApiResponse;
import org.roy.trb.tst.credit.line.models.responses.CreditLineStatusResponse;
import org.roy.trb.tst.credit.line.services.ICreditLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditLineController implements CreditLineApi {

  private final ICreditLineService creditLineService;

  @Override
  @PostMapping(
      path = "/validate",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ContractResponse<CreditLineApiResponse> validateCreditLine(
      @Valid @RequestBody CreditLineRequest creditLineRequest,
      @RequestHeader(value = "customerId") UUID customerId,
      @RequestHeader(value = "foundingType") FoundingType foundingType,
      HttpServletRequest servlet) {

    log.traceEntry(
        "Initializing credit line request validation. Request {}", creditLineRequest.toString());

    return log.traceExit(
        ContractResponse.<CreditLineApiResponse>builder()
            .response(
                creditLineService.validateCreditLine(customerId, creditLineRequest, foundingType))
            .path(servlet.getServletPath())
            .build());
  }

  @GetMapping(path = "/status/{customerId}", produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ContractResponse<CreditLineStatusResponse> getCreditLineStatusById(
      @PathVariable(value = "customerId") UUID customerId, HttpServletRequest servlet) {
    log.traceEntry("Checking existent creditLineRequests for customerId: {}", customerId);

    return log.traceExit(
        ContractResponse.<CreditLineStatusResponse>builder()
            .response(creditLineService.getCustomerCreditLine(customerId))
            .path(servlet.getServletPath())
            .build());
  }
}
