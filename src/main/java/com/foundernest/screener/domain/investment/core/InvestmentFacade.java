package com.foundernest.screener.domain.investment.core;

import com.foundernest.screener.domain.investment.core.model.Company;
import com.foundernest.screener.domain.investment.core.model.CompanyHaves;
import com.foundernest.screener.domain.investment.core.model.Criteria;
import com.foundernest.screener.domain.investment.core.ports.incoming.CompanyIncoming;
import com.foundernest.screener.domain.investment.core.ports.incoming.CriteriaIncoming;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CompanyOutgoing;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CriteriaOutgoing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class InvestmentFacade implements CriteriaIncoming, CompanyIncoming {
    @Autowired
    private CriteriaOutgoing criteriaOutgoing;

    @Autowired
    private CompanyOutgoing companyOutgoing;

    @Override
    public Criteria addUserCriteriaHave(String user, Criteria criteria) {
        return criteriaOutgoing.addUserCriteriaHave(user, criteria);
    }

    @Override
    public Criteria deleteUserCriteriaHave(String user, Criteria criteria) {
        return criteriaOutgoing.deleteUserCriteriaHave(user, criteria);
    }

    @Override
    public Criteria getUserCriteria(String user) {
        return criteriaOutgoing.getUserCriteria(user);
    }

    @Override
    public String addUserCompany(String user, Company company) {
        return companyOutgoing.addUserCompany(user, company);
    }

    @Override
    public List<Company> getAllUserCompanies(String name) {
        return companyOutgoing.getAllUserCompanies(name);
    }

    @Override
    public Company getUserCompanyDetails(String name, String company) {
        return companyOutgoing.getUserCompanyDetails(name, company);
    }

    @Override
    public String makeADecision(String name, String company, int decision) {
        return companyOutgoing.makeADecision(name, company, decision);
    }

    @Override
    public Company updateCompanyHaves(String name, String company, CompanyHaves companyHaves) {
        // Get user company details
        Company companyDetails = getUserCompanyDetails(name, company);
        List<String> finalHaves = companyDetails.getHas();
        List<String> finalHaveNot = companyDetails.getHasNot();
        // Compare existing with new ones
        if (!companyHaves.getHave().isEmpty()) {
            finalHaves = mergeListWithoutDuplicates(companyDetails.getHas(), companyHaves.getHave());
            if (!companyDetails.getHasNot().isEmpty()) {
                finalHaveNot.removeAll(companyHaves.getHave());
            }
        }
        if (!companyHaves.getHaveNot().isEmpty()) {
            finalHaveNot = mergeListWithoutDuplicates(companyDetails.getHasNot(), companyHaves.getHaveNot());
            if (!companyDetails.getHas().isEmpty()) {
                finalHaves.removeAll(companyHaves.getHaveNot());
            }
        }
        if (!companyHaves.getMissing().isEmpty()) {
            finalHaveNot.removeAll(companyHaves.getMissing());
            finalHaves.removeAll(companyHaves.getMissing());
        }
        // Update company haves
        Company finalCompany = new Company(
                companyDetails.getName(),
                companyDetails.getEmail(),
                finalHaves,
                finalHaveNot,
                companyDetails.getMeet()
        );
        return companyOutgoing.updateCompanyHaves(name, finalCompany);
    }

    private List<String> mergeListWithoutDuplicates(List<String> listOne, List<String> listTwo) {
        Set<String> set = new LinkedHashSet<>(listOne);
        set.addAll(listTwo);
        return new ArrayList<>(set);
    }
}
