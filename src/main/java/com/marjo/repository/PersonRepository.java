package com.marjo.repository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.marjo.entity.Person;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public List<Person> findByName(String name) {
        return list("name", name);
    }

    public List<Person> findByFirstname(String firstname) {
        return list("name = ?1 and firstname = ?2", firstname);
        // find("name = ?1 and status = ?2", "stef", "jj");
    }

    public List<Person> findByBirthdate(LocalDate birthdate) {
        return list("birthdate", birthdate);
    }

    public List<Person> findBy(String name, String firstname, LocalDate birthdate) {

        // V1
        Map<String, Object> params = Map.of(
                "name", isNotBlank(name) ? name : null,
                "firstname", isNotBlank(firstname) ? firstname : null,
                "birthdate", birthdate)
                .entrySet().stream()
                .filter(p -> p.getValue() != null)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        String query = params.keySet().stream().map(key -> String.format("%s = :%s", key, key))
                .collect(Collectors.joining(" and "));

        // V2
        Map<String, Object> params2 = Map.of(
                "name", name,
                "firstname", firstname,
                "birthdate", birthdate);

        String query2 = params.keySet().stream().map(key -> "(:%s isnull or %s = :%s)".replaceAll("%s", key))
                .collect(Collectors.joining(" and "));

        return list(query, params);
    }
}
