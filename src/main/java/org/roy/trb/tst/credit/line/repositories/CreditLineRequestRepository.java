package org.roy.trb.tst.credit.line.repositories;

import java.util.UUID;
import org.roy.trb.tst.credit.line.entities.CreditLineRequestRecord;
import org.springframework.data.repository.CrudRepository;

public interface CreditLineRequestRepository
    extends CrudRepository<CreditLineRequestRecord, UUID> {}
