package com.foundernest.screener.domain.investment.core.ports.incoming;

import com.foundernest.screener.domain.investment.core.model.Criteria;

public interface CriteriaIncoming {
    Criteria addUserCriteriaHave(String user, Criteria criteria);

    Criteria deleteUserCriteriaHave(String user, Criteria criteria);

    Criteria getUserCriteria(String user);
}
