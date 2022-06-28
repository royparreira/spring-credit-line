package org.roy.trb.tst.credit.line.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.roy.trb.tst.credit.line.entities.CreditLineRequestRecords;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;
import org.roy.trb.tst.credit.line.models.requests.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.responses.CreditLineApiResponse;
import org.roy.trb.tst.credit.line.models.responses.CreditLineStatusResponse;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CreditLineRequestMapper {

  @Mapping(target = "requestedCredit", source = "requestedCreditLine")
  RequesterFinancialData mapToRequesterFinancialData(CreditLineRequest creditLineRequest);

  CreditLineStatusResponse mapToCreditLineStatusResponse(
      CreditLineRequestRecords creditLineRequestRecordsEntity);

  CreditLineRequestRecords mapToCreditLineRequestEntity(
      CreditLineStatusResponse creditLineStatusResponse);

  @Mapping(target = "message", ignore = true)
  CreditLineApiResponse mapToCreditLineApiResponse(
      CreditLineStatusResponse creditLineStatusResponse);
}
