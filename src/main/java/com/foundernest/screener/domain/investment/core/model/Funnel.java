package com.foundernest.screener.domain.investment.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Funnel {
    private String company;
    private String email;
    private int matchingScore;
    private int warnings;
    private int missingInfo;
    private int mustHaves;
    private int superNiceToHave;
    private int niceToHaves;
    private int meet;
}
