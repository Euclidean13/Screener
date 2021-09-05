package com.foundernest.screener.domain.investment.core;

import com.foundernest.screener.domain.investment.core.model.Company;
import com.foundernest.screener.domain.investment.core.model.CompanyHaves;
import com.foundernest.screener.domain.investment.core.ports.incoming.CompanyIncoming;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CompanyOutgoing;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CriteriaOutgoing;
import org.junit.jupiter.api.Test;
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
public class InvestmentFacadeTest {
    @Autowired
    private CompanyIncoming companyIncoming;

    @MockBean
    private CriteriaOutgoing criteriaOutgoing;

    @MockBean
    private CompanyOutgoing companyOutgoing;

    @Test
    void updateCompanyHaves() {
        String userName = "tonyStark";
        String companyName = "Umbrella Corp";
        String email = "umbrella@gmail.com";
        List<String> has = Arrays.asList("Zombies", "employees");
        List<String> newHas = List.of("Boston");
        List<String> hasNot = Arrays.asList("CTO", "Angeles");
        List<String> newHasNot = List.of("employees");
        List<String> missing = new ArrayList<>();
        Company companyDetails = new Company(userName, email, has, hasNot, 0);
        CompanyHaves newCompanyHaves = new CompanyHaves(newHas, newHasNot, missing);
        Company finalCompany = new Company(
                userName,
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
                        userName,
                        email,
                        Arrays.asList("Zombies", "Boston"),
                        Arrays.asList("CTO", "Angeles", "employees"),
                        0
                )
        );
    }
}
