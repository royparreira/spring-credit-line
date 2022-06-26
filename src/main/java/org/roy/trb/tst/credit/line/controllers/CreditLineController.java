package org.roy.trb.tst.credit.line.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;
import org.roy.trb.tst.credit.line.services.ICreditLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreditLineController {

  private final ICreditLineService creditLineService;

  @PostMapping(
      path = "/validate",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<CreditLineResponse> validateCreditLine(
      @Valid @RequestBody CreditLineRequest creditLineRequest) {

    log.traceEntry(
        "Initializing credit line request validation. Request {}", creditLineRequest.toString());

    return log.traceExit(
        new ResponseEntity<>(
            creditLineService.validateCreditLine(creditLineRequest), HttpStatus.ACCEPTED));
  }
}
