package com.foundernest.screener.domain.investment.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Criteria {
    private List<String> mustHave;
    private List<String> superNiceToHave;
    private List<String> niceToHave;
}
