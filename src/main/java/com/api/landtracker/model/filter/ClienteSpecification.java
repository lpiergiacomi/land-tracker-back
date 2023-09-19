package com.api.landtracker.model.filter;

import com.api.landtracker.model.entities.Cliente;
import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecification {
    private ClienteSpecification() {
    }

    public static Specification<Cliente> clienteNombreLike(String name) {
        return (root, query, builder) ->
                name == null || name.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("nombre")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

}
