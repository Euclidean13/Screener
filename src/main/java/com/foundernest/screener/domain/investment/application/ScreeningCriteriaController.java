package com.foundernest.screener.domain.investment.application;

import com.foundernest.screener.domain.investment.core.model.Criteria;
import com.foundernest.screener.domain.investment.core.ports.incoming.CriteriaIncoming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScreeningCriteriaController {
    @Autowired
    private CriteriaIncoming criteriaIncoming;

    @GetMapping("/userCriteria")
    public ResponseEntity<?> userCriteria(@RequestParam String name) {
        Criteria resp = criteriaIncoming.getUserCriteria(name);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to find user criteria");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("/addCriteria")
    public ResponseEntity<?> addCriteria(@RequestParam String name, @RequestBody Criteria criteria) {
        Criteria resp = criteriaIncoming.addUserCriteriaHave(name, criteria);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to add criteria to user " + name);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @DeleteMapping("/deleteCriteria")
    public ResponseEntity<?> deleteCriteria(@RequestParam String name, @RequestBody Criteria criteria) {
        Criteria resp = criteriaIncoming.deleteUserCriteriaHave(name, criteria);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to delete criteria of user " + name);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
