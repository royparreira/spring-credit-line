package org.roy.credit.line.services.mappers;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.roy.credit.line.models.daos.CreditLineRequestRecordDao;
import org.roy.credit.line.models.dtos.RequesterFinancialData;
import org.roy.credit.line.models.requests.PostRequestCreditLineRequestBody;
import org.roy.credit.line.models.responses.PostRequestCreditLineResponseBody;
import org.roy.credit.line.entities.CreditLineRequestRecord;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CreditLineRequestMapper {

  /**
   * Extract the costumer financial data from request body
   *
   * @param postRequestCreditLineRequestBody POST credit-line-request request body
   * @return costumer financial object
   */
  RequesterFinancialData mapToRequesterFinancialData(
          PostRequestCreditLineRequestBody postRequestCreditLineRequestBody, UUID customerId);

  /**
   * Map entity to model layer POST credit-line-request data
   *
   * @param creditLineRequestRecord entity object
   * @return a model layer DAO object
   */
  CreditLineRequestRecordDao mapToCreditLineRequestRecordDao(
      CreditLineRequestRecord creditLineRequestRecord);

  /**
   * Map model to entity layer POST credit-line-request data
   *
   * @param creditLineRequestRecordDao model object
   * @return an entity layer Entity object
   */
  CreditLineRequestRecord mapToCreditLineRequestEntity(
      CreditLineRequestRecordDao creditLineRequestRecordDao);

  /**
   * Map DAO to POST credit-line-request response body
   *
   * @param creditLineRequestRecordDao model object
   * @return POST credit-line-request response body
   */
  @Mapping(target = "message", ignore = true)
  PostRequestCreditLineResponseBody mapToRequestCreditLineResponseBody(
      CreditLineRequestRecordDao creditLineRequestRecordDao);
}
