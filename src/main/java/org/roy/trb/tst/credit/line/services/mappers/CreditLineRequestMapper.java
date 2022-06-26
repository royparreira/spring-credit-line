package org.roy.trb.tst.credit.line.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.roy.trb.tst.credit.line.models.CreditLineRequest;
import org.roy.trb.tst.credit.line.models.RequesterFinancialData;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CreditLineRequestMapper {

  @Mapping(target = "requestedCredit", source = "requestedCreditLine")
  RequesterFinancialData mapToRequesterFinancialData(CreditLineRequest creditLineRequest);
}
