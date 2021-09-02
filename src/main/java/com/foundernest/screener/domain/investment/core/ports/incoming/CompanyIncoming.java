package com.foundernest.screener.domain.investment.core.ports.incoming;

import com.foundernest.screener.domain.investment.core.model.Company;
import com.foundernest.screener.domain.investment.core.model.CompanyHaves;

import java.util.List;

public interface CompanyIncoming {
    List<Company> getAllUserCompanies(String name);

    Company getUserCompanyDetails(String name, String company);

    String makeADecision(String name, String company, int decision);

    String updateCompanyHaves(String name, String company, CompanyHaves companyHaves);
}
