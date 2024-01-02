package org.roy.credit.line.models.responses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roy.credit.line.enums.ErrorType;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseError {

  ErrorType errorType;

  HttpStatus errorCode;

  String errorMessage;
}
