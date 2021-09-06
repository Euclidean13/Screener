package com.foundernest.screener.domain.investment.core;

import com.foundernest.screener.domain.crud.core.ports.outgoing.UserOutgoing;
import com.foundernest.screener.domain.investment.core.model.Company;
import com.foundernest.screener.domain.investment.core.model.CompanyHaves;
import com.foundernest.screener.domain.investment.core.model.Criteria;
import com.foundernest.screener.domain.investment.core.ports.incoming.CompanyIncoming;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CompanyOutgoing;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CriteriaOutgoing;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {InvestmentFacade.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InvestmentFacadeTest {
    @Autowired
    private CompanyIncoming companyIncoming;

    @MockBean
    private CriteriaOutgoing criteriaOutgoing;

    @MockBean
    private CompanyOutgoing companyOutgoing;

    @MockBean
    private UserOutgoing userOutgoing;

    InvestmentFacade investmentFacade;
    String userName;
    String companyName;
    String email;
    List<String> has;
    List<String> hasNot;

    @BeforeAll
    public void setup() {
        investmentFacade = new InvestmentFacade();
        userName = "tonyStark";
        companyName = "Umbrella Corp";
        email = "umbrella@gmail.com";
        has = Arrays.asList("Zombies", "employees");
        hasNot = Arrays.asList("CTO", "Angeles");
    }

    @Test
    void updateCompanyHaves() {
        List<String> newHas = List.of("Boston");
        List<String> newHasNot = List.of("employees");
        List<String> missing = new ArrayList<>();
        Company companyDetails = new Company(companyName, email, has, hasNot, 0);
        CompanyHaves newCompanyHaves = new CompanyHaves(newHas, newHasNot, missing);
        Company finalCompany = new Company(
                companyName,
                email,
                Arrays.asList("Zombies", "Boston"),
                Arrays.asList("CTO", "Angeles", "employees"),
                0
        );

        Mockito.when(companyOutgoing.getUserCompanyDetails(userName, companyName)).thenReturn(companyDetails);
        Mockito.when(companyOutgoing.updateCompanyHaves(userName, finalCompany)).thenReturn(finalCompany);

        companyIncoming.updateCompanyHaves(userName, companyName, newCompanyHaves);

        ArgumentCaptor<Company> argument = ArgumentCaptor.forClass(Company.class);
        verify(companyOutgoing).updateCompanyHaves(anyString(), argument.capture());
        assertThat(argument.getValue()).isEqualTo(
                new Company(
                        companyName,
                        email,
                        Arrays.asList("Zombies", "Boston"),
                        Arrays.asList("CTO", "Angeles", "employees"),
                        0
                )
        );
    }

    @Test
    void checkFindCommonElements() {
        List<String> array1 = Arrays.asList("Zombies", "employees");
        List<String> array2 = Arrays.asList("Zombies", "CTO", "Angeles", "employees");

        int commonElements = investmentFacade.findNumberCommonElements(array1, array2);

        assertThat(commonElements).isEqualTo(2);
    }

    @Test
    void checkCalculateMissingInfo() {
        Company company = new Company(companyName, email, has, hasNot, 0);
        Criteria criteria = new Criteria(
                Arrays.asList("Zombies", "employees"),
                List.of("CTO"),
                List.of("Boston"));

        int missingInfoPercentage = investmentFacade.calculateMissingInfo(criteria, company);
        assertThat(missingInfoPercentage).isEqualTo(25);
    }

    @Test
    void checkCalculateWarnings() {
        int companyHasNot = 2;
        Criteria criteria = new Criteria(
                Arrays.asList("Zombies", "employees"),
                List.of("CTO"),
                List.of("Boston"));
        int warnPercentage = investmentFacade.calculateWarnings(companyHasNot, criteria);
        assertThat(warnPercentage).isEqualTo(50);
    }

    @Test
    void checkCalculateMatchingScore() {
        Company company = new Company(companyName, email, has, hasNot, 0);
        Criteria criteria = new Criteria(
                Arrays.asList("Zombies", "employees"),
                List.of("CTO"),
                List.of("Boston"));
        int matchingScore = investmentFacade.calculateMatchingScore(criteria, company);
        assertThat(matchingScore).isEqualTo(50);
    }
}
