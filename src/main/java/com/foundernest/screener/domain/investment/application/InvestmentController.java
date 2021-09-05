package com.foundernest.screener.domain.investment.application;

import com.foundernest.screener.domain.investment.core.model.Company;
import com.foundernest.screener.domain.investment.core.model.CompanyHaves;
import com.foundernest.screener.domain.investment.core.ports.incoming.CompanyIncoming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvestmentController {
    @Autowired
    private CompanyIncoming companyIncoming;

    @GetMapping("/userCompanies")
    public ResponseEntity<?> getUserCompanies(@RequestParam String user) {
        List<Company> resp = companyIncoming.getAllUserCompanies(user);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get " + user + " companies");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/userCompanyDetails")
    public ResponseEntity<?> getUserCompanyDetails(@RequestParam String user, @RequestParam String company) {
        Company resp = companyIncoming.getUserCompanyDetails(user, company);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to get " + user + " " + company + " details");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("/decision")
    public ResponseEntity<?> makeADecision(
            @RequestParam String name, @RequestParam String company, @RequestParam int decision
    ) {
        String resp = companyIncoming.makeADecision(name, company, decision);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update user " + name +
                    " company " + company + " decision");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("/updateCompanyHaves")
    public ResponseEntity<?> updateCompanyHaves(
            @RequestParam String name, @RequestParam String company, @RequestBody CompanyHaves companyHaves
    ) {
        Company resp = companyIncoming.updateCompanyHaves(name, company, companyHaves);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update user " + name +
                    " company " + company + " haves");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("/addUserCompany")
    public ResponseEntity<?> addUserCompany(@RequestParam String user, @RequestBody Company company) {
        String resp = companyIncoming.addUserCompany(user, company);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to add user " + user +
                    " company " + company);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
