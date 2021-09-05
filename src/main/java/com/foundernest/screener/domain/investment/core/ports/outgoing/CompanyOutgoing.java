package com.foundernest.screener.domain.investment.core.ports.outgoing;

import com.foundernest.screener.domain.investment.core.model.Company;

import java.util.List;

public interface CompanyOutgoing {
    String addUserCompany(String user, Company company);

    List<Company> getAllUserCompanies(String name);

    Company getUserCompanyDetails(String name, String company);

    String makeADecision(String name, String company, int decision);

    String updateCompanyHaves(String name, Company company);
}
