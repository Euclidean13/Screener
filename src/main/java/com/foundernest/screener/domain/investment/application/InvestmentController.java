package com.foundernest.screener.domain.investment.application;

import com.foundernest.screener.domain.investment.core.ports.incoming.CompanyIncoming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvestmentController {
    @Autowired
    private CompanyIncoming companyIncoming;

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

//    @PostMapping("/updateCompanyHaves")
//    public ResponseEntity<?> updateCompanyHaves(
//            @RequestParam String name, @RequestParam String company, @RequestBody CompanyHaves companyHaves
//    ) {
//
//    }
}
