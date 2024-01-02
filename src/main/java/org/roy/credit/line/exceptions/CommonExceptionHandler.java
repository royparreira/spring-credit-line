package org.roy.credit.line.exceptions;

import static org.roy.credit.line.enums.CreditLineStatus.REJECTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.roy.credit.line.constants.Descriptions;
import org.roy.credit.line.constants.Messages;
import org.roy.credit.line.enums.ErrorType;
import org.roy.credit.line.models.responses.ContractResponse;
import org.roy.credit.line.models.responses.PostRequestCreditLineResponseBody;
import org.roy.credit.line.models.responses.ResponseError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@ControllerAdvice
public class CommonExceptionHandler {

  @ResponseBody
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ResponseEntity<ContractResponse<Void>> handleMissingHeadersExceptions(
      HttpServletRequest request, MissingRequestHeaderException exception) {

    log.warn("Request header validation error occurred: {}", exception.getMessage());

    var contractResponse =
        ContractResponse.<Void>builder()
            .error(
                ResponseError.builder()
                    .errorCode(HttpStatus.BAD_REQUEST)
                    .errorType(ErrorType.MISSING_REQUIRED_HEADER)
                    .errorMessage(exception.getMessage())
                    .build())
            .path(request.getServletPath())
            .build();

    return new ResponseEntity<>(
        contractResponse, getProducesJsonHttpHeader(), HttpStatus.BAD_REQUEST);
  }

  @ResponseBody
  @ExceptionHandler({RejectedCreditLineException.class})
  public ResponseEntity<ContractResponse<PostRequestCreditLineResponseBody>>
      handleRejectedCreditLineExceptions(
          HttpServletRequest request, RejectedCreditLineException exception) {

    log.info("Credit line request rejected!");

    String customMessage =
        exception.getCustomMessage().isEmpty() ? null : exception.getCustomMessage();

    var contractResponse =
        ContractResponse.<PostRequestCreditLineResponseBody>builder()
            .response(
                PostRequestCreditLineResponseBody.builder()
                    .creditLineStatus(REJECTED)
                    .message(customMessage)
                    .build())
            .path(request.getServletPath())
            .build();

    return new ResponseEntity<>(contractResponse, getProducesJsonHttpHeader(), HttpStatus.OK);
  }

  @ResponseBody
  @ExceptionHandler({TooManyRequestsException.class})
  public ResponseEntity<ContractResponse<Void>> handleNotFoundExceptions(
      HttpServletRequest request, TooManyRequestsException exception) {

    log.warn("Too many requests: {}", Messages.TOO_MANY_REQUESTS_MSG);

    var contractResponse =
        ContractResponse.<Void>builder()
            .error(
                ResponseError.builder()
                    .errorCode(HttpStatus.TOO_MANY_REQUESTS)
                    .errorType(ErrorType.EXCEED_API_QUOTA)
                    .errorMessage(Messages.TOO_MANY_REQUESTS_MSG)
                    .build())
            .path(request.getServletPath())
            .build();

    return new ResponseEntity<>(
        contractResponse, getProducesJsonHttpHeader(), HttpStatus.TOO_MANY_REQUESTS);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ContractResponse<Void>> handleException(
      HttpServletRequest request, Exception exception) {

    log.error("Unhandled exception: {} ", ExceptionUtils.getStackTrace(exception));

    var contractResponse =
        ContractResponse.<Void>builder()
            .error(
                ResponseError.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorType(ErrorType.UNKNOWN_ERROR)
                    .errorMessage(Descriptions.INTERNAL_SERVER_ERROR_DESCRIPTION)
                    .build())
            .path(request.getServletPath())
            .build();

    return new ResponseEntity<>(
        contractResponse, getProducesJsonHttpHeader(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  @ResponseBody
  public ResponseEntity<ContractResponse<Void>> handleException(
      HttpServletRequest request, InternalServerErrorException exception) {

    log.error(exception.getMessage() + ": {}", ExceptionUtils.getStackTrace(exception));

    var contractResponse =
        ContractResponse.<Void>builder()
            .error(
                ResponseError.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorType(ErrorType.UNKNOWN_ERROR)
                    .errorMessage(Descriptions.INTERNAL_SERVER_ERROR_DESCRIPTION)
                    .build())
            .path(request.getServletPath())
            .build();

    return new ResponseEntity<>(
        contractResponse, getProducesJsonHttpHeader(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  public ResponseEntity<ContractResponse<Void>> handleException(
      HttpServletRequest request, HttpMessageNotReadableException exception) {

    log.warn(exception.getMessage() + ": {}", exception.getMessage());
    var contractResponse =
        ContractResponse.<Void>builder()
            .error(
                ResponseError.builder()
                    .errorCode(HttpStatus.BAD_REQUEST)
                    .errorType(ErrorType.MISMATCH_REQUEST)
                    .errorMessage(Descriptions.MISMATCH_REQUEST_DESCRIPTION)
                    .build())
            .path(request.getServletPath())
            .build();

    return new ResponseEntity<>(
        contractResponse, getProducesJsonHttpHeader(), HttpStatus.BAD_REQUEST);
  }

  private HttpHeaders getProducesJsonHttpHeader() {

    var defaultHttpHeaders = new HttpHeaders();
    defaultHttpHeaders.set("produces", APPLICATION_JSON_VALUE);

    return defaultHttpHeaders;
  }
}
