package org.roy.trb.tst.credit.line.exceptions;

import static org.roy.trb.tst.credit.line.enums.CreditLineStatus.REJECTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.roy.trb.tst.credit.line.constants.Descriptions;
import org.roy.trb.tst.credit.line.enums.ErrorType;
import org.roy.trb.tst.credit.line.models.ContractResponse;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;
import org.roy.trb.tst.credit.line.models.ResponseError;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class CommonExceptionHandler {

  @ResponseBody
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ContractResponse<Void> handleMissingHeadersExceptions(
      HttpServletRequest request, MissingRequestHeaderException exception) {

    log.warn("Request header validation error occurred: {}", exception.getMessage());

    return ContractResponse.<Void>builder()
        .error(
            ResponseError.builder()
                .errorCode(BAD_REQUEST)
                .errorType(ErrorType.MISSING_REQUIRED_HEADER)
                .errorMessage(exception.getMessage())
                .build())
        .path(request.getServletPath())
        .build();
  }

  @ResponseBody
  @ResponseStatus(OK)
  @ExceptionHandler({RejectedCreditLineException.class})
  public ContractResponse<CreditLineResponse> handleRejectedCreditLineExceptions(
      HttpServletRequest request, RejectedCreditLineException exception) {

    log.info("Credit line request rejected!");

    return ContractResponse.<CreditLineResponse>builder()
        .response(CreditLineResponse.builder().creditLineStatus(REJECTED).build())
        .path(request.getServletPath())
        .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ContractResponse<Void> handleException(HttpServletRequest request, Exception exception) {

    log.error("Unhandled exception: {} ", ExceptionUtils.getStackTrace(exception), exception);

    return ContractResponse.<Void>builder()
        .error(
            ResponseError.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorType(ErrorType.UNKNOWN_ERROR)
                .errorMessage(Descriptions.INTERNAL_SERVER_ERROR_DESCRIPTION)
                .build())
        .path(request.getServletPath())
        .build();
  }
}
