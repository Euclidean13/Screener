package com.foundernest.screener.domain.crud.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String email;
    private Criteria criteria;
    private List<Company> companies;
}
