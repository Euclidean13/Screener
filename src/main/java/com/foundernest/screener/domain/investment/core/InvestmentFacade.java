package com.foundernest.screener.domain.investment.core;

import com.foundernest.screener.domain.crud.core.ports.outgoing.UserOutgoing;
import com.foundernest.screener.domain.investment.core.model.*;
import com.foundernest.screener.domain.investment.core.ports.incoming.CompanyIncoming;
import com.foundernest.screener.domain.investment.core.ports.incoming.CriteriaIncoming;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CompanyOutgoing;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CriteriaOutgoing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InvestmentFacade implements CriteriaIncoming, CompanyIncoming {
    @Autowired
    private CriteriaOutgoing criteriaOutgoing;

    @Autowired
    private CompanyOutgoing companyOutgoing;

    @Autowired
    private UserOutgoing userOutgoing;

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

    @Override
    public List<Funnel> getUserCompaniesFunnel(String name) {
        // Get user companies
        User user = companyOutgoing.getUserDetails(name);

        // Calculate companies funnel
        return user.getCompanies().stream().map((company) -> new Funnel(
                company.getName(),
                company.getEmail(),
                calculateMatchingScore(user.getCriteria(), company),
                calculateWarnings(company.getHasNot().size(), user.getCriteria()),
                calculateMissingInfo(user.getCriteria(), company),
                calculateMustHavesMatchPercentage(user.getCriteria().getMustHave(), company.getHas()),
                calculateSuperNiceToHaveMatchPercentage(user.getCriteria().getSuperNiceToHave(), company.getHas()),
                calculateNiceToHaveMatchPercentage(user.getCriteria().getNiceToHave(), company.getHas()),
                company.getMeet()
        )).collect(Collectors.toList());
    }

    public int calculateMatchingScore(Criteria criteria, Company company) {
        int mustHave = findNumberCommonElements(criteria.getMustHave(), company.getHas());
        int superNiceToHave = findNumberCommonElements(criteria.getSuperNiceToHave(), company.getHas());
        int niceToHave = findNumberCommonElements(criteria.getNiceToHave(), company.getHas());
        List<String> criteriaElements = Stream.of(criteria.getMustHave(), criteria.getSuperNiceToHave(),
                criteria.getNiceToHave()).flatMap(Collection::stream).collect(Collectors.toList());
        return (int) ((double) (mustHave + superNiceToHave + niceToHave) / criteriaElements.size() * 100);
    }

    private int calculateMustHavesMatchPercentage(List<String> criteriaMustHave, List<String> companyMustHave) {
        return (int) ((double) findNumberCommonElements(criteriaMustHave, companyMustHave) / criteriaMustHave.size() * 100);
    }

    private int calculateSuperNiceToHaveMatchPercentage(
            List<String> criteriaSuperNiceToHave, List<String> companySuperNiceToHave
    ) {
        return (int) ((double) findNumberCommonElements(criteriaSuperNiceToHave, companySuperNiceToHave) / criteriaSuperNiceToHave.size() * 100);
    }

    private int calculateNiceToHaveMatchPercentage(List<String> criteriaNiceToHave, List<String> companyNiceToHave) {
        return (int) ((double) findNumberCommonElements(criteriaNiceToHave, companyNiceToHave) / criteriaNiceToHave.size() * 100);
    }

    public int calculateWarnings(int companyHasNot, Criteria criteria) {
        List<String> criteriaElements = Stream.of(criteria.getMustHave(), criteria.getSuperNiceToHave(),
                criteria.getNiceToHave()).flatMap(Collection::stream).collect(Collectors.toList());
        return (int) ((double) companyHasNot / criteriaElements.size() * 100);
    }

    public int calculateMissingInfo(Criteria criteria, Company company) {
        List<String> criteriaElements = Stream.of(criteria.getMustHave(), criteria.getSuperNiceToHave(),
                criteria.getNiceToHave()).flatMap(Collection::stream).collect(Collectors.toList());
        List<String> companyElements = Stream.of(company.getHas(), company.getHasNot())
                .flatMap(Collection::stream).collect(Collectors.toList());
        int commonElements = findNumberCommonElements(criteriaElements, companyElements);
        return (int) (((double) criteriaElements.size() - (double) commonElements) / criteriaElements.size() * 100);
    }

    public int findNumberCommonElements(List<String> array1, List<String> array2) {
        List<String> commonElements = new ArrayList<>();
        for (String el1 : array1) {
            for (String el2 : array2) {
                if (el1.equals(el2)) {
                    //Check if the list already contains the common element
                    if (!commonElements.contains(el1)) {
                        //add the common element into the list
                        commonElements.add(el1);
                    }
                }
            }
        }
        return commonElements.size();
    }
}
