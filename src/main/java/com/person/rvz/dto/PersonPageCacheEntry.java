package com.person.rvz.dto;

import com.person.rvz.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonPageCacheEntry {
    private List<Person> content;
    private long totalElements;
}
