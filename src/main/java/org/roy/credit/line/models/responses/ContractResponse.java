package org.roy.credit.line.models.responses;

import static org.roy.credit.line.constants.ApiEndpoints.REQUEST_CREDIT_LINE_ENDPOINT;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(Include.NON_NULL)
@Schema(name = "Contract Response", description = "Encapsulates an api response")
public class ContractResponse<T> {

  @Schema(description = "The http response status", accessMode = AccessMode.READ_ONLY)
  private ResponseError error;

  @Schema(description = "Api response")
  private T response;

  @Builder.Default
  @Schema(example = "2022-06-26T02:14:21.120Z", description = "Time stamp of the request.")
  private ZonedDateTime utcTimestamp = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

  @Schema(example = REQUEST_CREDIT_LINE_ENDPOINT, description = "Path of the endpoint")
  private String path;
}
