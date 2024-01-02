package org.roy.credit.line.repositories;

import java.util.UUID;
import org.roy.credit.line.entities.CreditLineRequestRecord;
import org.springframework.data.repository.CrudRepository;

public interface CreditLineRequestRepository
    extends CrudRepository<CreditLineRequestRecord, UUID> {}
