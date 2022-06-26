package org.roy.trb.tst.credit.line.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.roy.trb.tst.credit.line.docs.CreditLineApi;
import org.roy.trb.tst.credit.line.enums.FoundingType;
import org.roy.trb.tst.credit.line.models.ContractResponse;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;
import org.roy.trb.tst.credit.line.services.ICreditLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
  public ContractResponse<CreditLineResponse> validateCreditLine(
      @Valid @RequestBody CreditLineRequest creditLineRequest,
      @RequestHeader(value = "customerId") UUID customerId,
      @RequestHeader(value = "foundingType") FoundingType foundingType,
      HttpServletRequest servlet) {

    log.traceEntry(
        "Initializing credit line request validation. Request {}", creditLineRequest.toString());

    return log.traceExit(
        ContractResponse.<CreditLineResponse>builder()
            .response(
                creditLineService.validateCreditLine(customerId, creditLineRequest, foundingType))
            .path(servlet.getServletPath())
            .build());
  }
}
