package org.roy.trb.tst.credit.line.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.roy.trb.tst.credit.line.constants.Descriptions.SALES_AGENT_MSG;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_REQUESTED_DATE;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_STRING_CUSTOMER_ID;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_STRING_REQUESTED_DATE;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_UUID_CUSTOMER_ID;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.mockSmeAcceptableRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.roy.trb.tst.credit.line.controllers.CreditLineController;
import org.roy.trb.tst.credit.line.enums.CreditLineStatus;
import org.roy.trb.tst.credit.line.exceptions.NotFoundException;
import org.roy.trb.tst.credit.line.exceptions.RejectedCreditLineException;
import org.roy.trb.tst.credit.line.models.requests.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.responses.CreditLineApiResponse;
import org.roy.trb.tst.credit.line.models.responses.CreditLineStatusResponse;
import org.roy.trb.tst.credit.line.services.ICreditLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CreditLineController.class)
class CreditLineControllerTest {

  private static final String VALIDATE_POST_URL = "/v1/validate";
  private static final String GET_STATUS_URL = "/v1/status/" + MOCKED_STRING_CUSTOMER_ID;
  private static final String APPROVED_CREDIT_LINE = "10000";
  @Autowired ObjectMapper objectMapper;
  @MockBean private ICreditLineService creditLineService;
  @Autowired private MockMvc mockMvc;

  private static void assertErrorResponse(ResultActions performHttpCall) throws Exception {
    performHttpCall
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.response").doesNotExist());
  }

  @Test
  void shouldAcceptCreditLineRequest() throws Exception {

    when(creditLineService.validateCreditLine(any(UUID.class), any(CreditLineRequest.class), any()))
        .thenReturn(
            CreditLineApiResponse.builder()
                .creditLineStatus(CreditLineStatus.ACCEPTED)
                .acceptedCreditLine(new BigDecimal(APPROVED_CREDIT_LINE))
                .build());

    MockHttpServletRequestBuilder builder =
        getBasePostHttpRequestBuilder()
            .headers(
                new HttpHeaders() {
                  {
                    set("customerId", MOCKED_STRING_CUSTOMER_ID);
                    set("foundingType", "STARTUP");
                  }
                })
            .content(objectMapper.writeValueAsBytes(mockSmeAcceptableRequest()));

    mockMvc
        .perform(builder)
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.error").doesNotExist())
        .andExpect(jsonPath("$.response.creditLineStatus").value(CreditLineStatus.ACCEPTED.name()))
        .andExpect(jsonPath("$.response.acceptedCreditLine").value(APPROVED_CREDIT_LINE))
        .andExpect(jsonPath("$.utcTimestamp").exists())
        .andExpect(jsonPath("$.path").exists());
  }

  @Test
  void shouldRejectCreditNewLineRequest() throws Exception {

    when(creditLineService.validateCreditLine(any(UUID.class), any(CreditLineRequest.class), any()))
        .thenThrow(new RejectedCreditLineException());

    MockHttpServletRequestBuilder builder =
        getBasePostHttpRequestBuilder()
            .headers(
                new HttpHeaders() {
                  {
                    set("customerId", MOCKED_STRING_CUSTOMER_ID);
                    set("foundingType", "STARTUP");
                  }
                })
            .content(objectMapper.writeValueAsBytes(mockSmeAcceptableRequest()));

    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.error").doesNotExist())
        .andExpect(jsonPath("$.response.creditLineStatus").value(CreditLineStatus.REJECTED.name()))
        .andExpect(jsonPath("$.response.acceptedCreditLine").doesNotExist())
        .andExpect(jsonPath("$.response.message").doesNotExist())
        .andExpect(jsonPath("$.utcTimestamp").exists())
        .andExpect(jsonPath("$.path").exists());
  }

  @Test
  void shouldRejectCreditLineRequestMoreThanThreeFails() throws Exception {

    when(creditLineService.validateCreditLine(any(UUID.class), any(CreditLineRequest.class), any()))
        .thenThrow(new RejectedCreditLineException(SALES_AGENT_MSG));

    MockHttpServletRequestBuilder builder =
        getBasePostHttpRequestBuilder()
            .headers(
                new HttpHeaders() {
                  {
                    set("customerId", MOCKED_STRING_CUSTOMER_ID);
                    set("foundingType", "STARTUP");
                  }
                })
            .content(objectMapper.writeValueAsBytes(mockSmeAcceptableRequest()));

    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.error").doesNotExist())
        .andExpect(jsonPath("$.response.creditLineStatus").value(CreditLineStatus.REJECTED.name()))
        .andExpect(jsonPath("$.response.acceptedCreditLine").doesNotExist())
        .andExpect(jsonPath("$.response.message").value(SALES_AGENT_MSG))
        .andExpect(jsonPath("$.utcTimestamp").exists())
        .andExpect(jsonPath("$.path").exists());
  }

  @Test
  void shouldSuccessfullyGetCreditLineStatus() throws Exception {

    when(creditLineService.getCustomerCreditLine(any(UUID.class)))
        .thenReturn(
            CreditLineStatusResponse.builder()
                .customerId(MOCKED_UUID_CUSTOMER_ID)
                .creditLineStatus(CreditLineStatus.ACCEPTED)
                .acceptedCreditLine(new BigDecimal(APPROVED_CREDIT_LINE))
                .creditLineStatus(CreditLineStatus.ACCEPTED)
                .requestedDate(MOCKED_REQUESTED_DATE)
                .attempts(1)
                .build());

    MockHttpServletRequestBuilder builder = getBaseGetHttpRequestBuilder();

    mockMvc
        .perform(builder)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.error").doesNotExist())
        .andExpect(jsonPath("$.response.customerId").value(MOCKED_STRING_CUSTOMER_ID))
        .andExpect(jsonPath("$.response.acceptedCreditLine").value(APPROVED_CREDIT_LINE))
        .andExpect(jsonPath("$.response.creditLineStatus").value(CreditLineStatus.ACCEPTED.name()))
        .andExpect(jsonPath("$.response.requestedDate").value(MOCKED_STRING_REQUESTED_DATE))
        .andExpect(jsonPath("$.response.attempts").value(1))
        .andExpect(jsonPath("$.utcTimestamp").exists())
        .andExpect(jsonPath("$.path").exists());
  }

  @Test
  void shouldRespondNotFoundGettingNonExistentData() throws Exception {

    when(creditLineService.getCustomerCreditLine(any(UUID.class)))
        .thenThrow(new NotFoundException());

    MockHttpServletRequestBuilder builder = getBaseGetHttpRequestBuilder();

    assertErrorResponse(mockMvc.perform(builder).andExpect(status().isNotFound()));
  }

  @Test
  void shouldResponseBadRequestForMissingRequiredHeader() throws Exception {

    MockHttpServletRequestBuilder builder =
        getBasePostHttpRequestBuilder()
            .headers(
                new HttpHeaders() {
                  {
                    set("foundingType", "STARTUP");
                  }
                })
            .content(objectMapper.writeValueAsBytes(mockSmeAcceptableRequest()));

    assertErrorResponse(mockMvc.perform(builder).andExpect(status().isBadRequest()));
  }

  @Test
  void shouldResponseBadRequestForMismatchFoundingType() throws Exception {

    MockHttpServletRequestBuilder builder =
        getBasePostHttpRequestBuilder()
            .headers(
                new HttpHeaders() {
                  {
                    set("foundingType", "FAKE");
                  }
                })
            .content(objectMapper.writeValueAsBytes(mockSmeAcceptableRequest()));

    assertErrorResponse(mockMvc.perform(builder).andExpect(status().isBadRequest()));
  }

  @Test
  void shouldResponseInternalServerErrorForGeneralExceptions() throws Exception {

    when(creditLineService.validateCreditLine(any(UUID.class), any(CreditLineRequest.class), any()))
        .thenThrow(new RuntimeException());

    MockHttpServletRequestBuilder builder =
        getBasePostHttpRequestBuilder()
            .headers(
                new HttpHeaders() {
                  {
                    set("customerId", MOCKED_STRING_CUSTOMER_ID);
                    set("foundingType", "STARTUP");
                  }
                })
            .content(objectMapper.writeValueAsBytes(mockSmeAcceptableRequest()));

    assertErrorResponse(mockMvc.perform(builder).andExpect(status().isInternalServerError()));
  }

  private MockHttpServletRequestBuilder getBasePostHttpRequestBuilder() {
    return post(VALIDATE_POST_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);
  }

  private MockHttpServletRequestBuilder getBaseGetHttpRequestBuilder() {
    return get(GET_STATUS_URL).contentType(MediaType.APPLICATION_JSON);
  }
}
