package com.api.landtracker.model.filter;

import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.LotState;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class LotSpecification {


    private LotSpecification() {

    }

    public static Specification<Lot> lotNameLike(String name) {
        return (root, query, builder) ->
                name == null || name.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

    public static Specification<Lot> lotCadastralAccNumberLike(String cadastralAccNumber) {
        return (root, query, builder) ->
                cadastralAccNumber == null || cadastralAccNumber.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("cadastralAccNumber")),
                                "%" + cadastralAccNumber.toLowerCase() + "%"
                        );
    }

    public static Specification<Lot> lotMunicipalAccNumberLike(String municipalAccNumber) {
        return (root, query, builder) ->
                municipalAccNumber == null || municipalAccNumber.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("municipalAccNumber")),
                                "%" + municipalAccNumber.toLowerCase() + "%"
                        );
    }
    public static Specification<Lot> priceBetweenMinMax(Double minPrice, Double maxPrice) {
        return (root, query, builder) -> {

            if (minPrice == null || maxPrice == null) {
                return builder.conjunction();
            } else {
                return builder.between(root.get("price"), minPrice, maxPrice);
            }
        };
    }

    public static Specification<Lot> stateEqualsIn(List<LotState> stateNames) {
        return (root, query, builder) -> {

            if (stateNames == null || stateNames.isEmpty()) {
                return builder.conjunction();
            } else {
                List<Predicate> predicates = new ArrayList<>();
                stateNames.forEach(stage -> {
                    predicates.add(builder.equal(root.get("state"), stage));
                });

                return builder.or(predicates.toArray(new Predicate[0]));
            }
        };

    }
}
