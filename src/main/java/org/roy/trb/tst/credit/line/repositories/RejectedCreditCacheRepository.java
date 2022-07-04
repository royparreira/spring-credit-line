package org.roy.trb.tst.credit.line.repositories;

import org.roy.trb.tst.credit.line.entities.RejectedCreditCache;
import org.springframework.data.repository.CrudRepository;

public interface RejectedCreditCacheRepository
    extends CrudRepository<RejectedCreditCache, String> {}
