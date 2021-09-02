package com.foundernest.screener.domain.crud.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private String name;
    private String email;
    private List<String> has;
    private List<String> hasNot;
    private int meet;
}
