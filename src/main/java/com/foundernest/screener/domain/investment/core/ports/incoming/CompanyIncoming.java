package com.foundernest.screener.domain.investment.core.ports.incoming;

import com.foundernest.screener.domain.investment.core.model.Company;
import com.foundernest.screener.domain.investment.core.model.CompanyHaves;

import java.util.List;

public interface CompanyIncoming {
    String addUserCompany(String user, Company company);

    List<Company> getAllUserCompanies(String name);

    Company getUserCompanyDetails(String name, String company);

    String makeADecision(String name, String company, int decision);

    Company updateCompanyHaves(String name, String company, CompanyHaves companyHaves);
}
