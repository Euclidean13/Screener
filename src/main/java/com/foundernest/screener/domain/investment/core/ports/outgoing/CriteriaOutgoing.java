package com.foundernest.screener.domain.investment.core.ports.outgoing;

import com.foundernest.screener.domain.investment.core.model.Criteria;

public interface CriteriaOutgoing {
    Criteria addUserCriteriaHave(String user, Criteria criteria);

    Criteria deleteUserCriteriaHave(String user, Criteria criteria);

    Criteria getUserCriteria(String user);
}
