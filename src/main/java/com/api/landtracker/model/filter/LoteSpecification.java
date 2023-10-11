package com.api.landtracker.model.filter;

import com.api.landtracker.model.entities.EstadoLote;
import com.api.landtracker.model.entities.Lote;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class LoteSpecification {


    private LoteSpecification() {

    }

    public static Specification<Lote> loteNombreLike(String name) {
        return (root, query, builder) ->
                name == null || name.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("nombre")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

    public static Specification<Lote> loteNroCuentaCatastralLike(String nroCuentaCatastral) {
        return (root, query, builder) ->
                nroCuentaCatastral == null || nroCuentaCatastral.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("nroCuentaCatastral")),
                                "%" + nroCuentaCatastral.toLowerCase() + "%"
                        );
    }

    public static Specification<Lote> loteNroCuentaMunicipalLike(String nroCuentaMunicipal) {
        return (root, query, builder) ->
                nroCuentaMunicipal == null || nroCuentaMunicipal.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("nroCuentaMunicipal")),
                                "%" + nroCuentaMunicipal.toLowerCase() + "%"
                        );
    }
    public static Specification<Lote> precioEntreMinMax(Double precioMin, Double precioMax) {
        return (root, query, builder) -> {

            if (precioMin == null || precioMax == null) {
                return builder.conjunction();
            } else {
                return builder.between(root.get("precio"), precioMin, precioMax);
            }
        };
    }

    public static Specification<Lote> estadoIgualIn(List<EstadoLote> stagesNames) {
        return (root, query, builder) -> {

            if (stagesNames == null || stagesNames.isEmpty()) {
                return builder.conjunction();
            } else {
                List<Predicate> predicates = new ArrayList<>();
                stagesNames.forEach(stage -> {
                    predicates.add(builder.equal(root.get("estadoLote"), stage));
                });

                return builder.or(predicates.toArray(new Predicate[0]));
            }
        };

    }
}
