package com.foundernest.screener.domain.investment.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyHaves {
    private List<String> have;
    private List<String> haveNot;
    private List<String> missing;
}
