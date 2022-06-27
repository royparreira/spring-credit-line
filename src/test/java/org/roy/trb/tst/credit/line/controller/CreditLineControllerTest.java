package org.roy.trb.tst.credit.line.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.MOCKED_STRING_CUSTOMER_ID;
import static org.roy.trb.tst.credit.line.fixture.CreditLineRequestFixture.mockSmeAcceptableRequest;
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
import org.roy.trb.tst.credit.line.exceptions.RejectedCreditLineException;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.CreditLineResponse;
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

  private static final String URL = "/v1/validate";
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
            CreditLineResponse.builder()
                .creditLineStatus(CreditLineStatus.ACCEPTED)
                .approvedCredit(new BigDecimal(APPROVED_CREDIT_LINE))
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
        .andExpect(jsonPath("$.response.approvedCredit").value(APPROVED_CREDIT_LINE))
        .andExpect(jsonPath("$.utcTimestamp").exists())
        .andExpect(jsonPath("$.path").exists());
  }

  @Test
  void shouldRejectCreditLineRequest() throws Exception {

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
        .andExpect(jsonPath("$.response.approvedCredit").doesNotExist())
        .andExpect(jsonPath("$.utcTimestamp").exists())
        .andExpect(jsonPath("$.path").exists());
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
    return post(URL).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
  }
}
