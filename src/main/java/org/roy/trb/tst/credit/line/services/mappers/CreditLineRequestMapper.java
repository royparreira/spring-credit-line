package org.roy.trb.tst.credit.line.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.roy.trb.tst.credit.line.entities.CreditLineRequestRecord;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;
import org.roy.trb.tst.credit.line.models.dao.CreditLineRequestRecordDao;
import org.roy.trb.tst.credit.line.models.requests.PostRequestCreditLineRequestBody;
import org.roy.trb.tst.credit.line.models.responses.PostRequestCreditLineResponseBody;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CreditLineRequestMapper {

  @Mapping(target = "requestedCredit", source = "requestedCreditLine")
  RequesterFinancialData mapToRequesterFinancialData(
      PostRequestCreditLineRequestBody postRequestCreditLineRequestBody);

  CreditLineRequestRecordDao mapToCreditLineStatusResponse(
      CreditLineRequestRecord creditLineRequestRecordEntity);

  CreditLineRequestRecord mapToCreditLineRequestEntity(
      CreditLineRequestRecordDao creditLineRequestRecordDao);

  @Mapping(target = "message", ignore = true)
  PostRequestCreditLineResponseBody mapToCreditLineApiResponse(
      CreditLineRequestRecordDao creditLineRequestRecordDao);
}
