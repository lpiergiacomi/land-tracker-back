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

    public static Specification<Cliente> emailLike(String email) {
        return (root, query, builder) ->
                email == null || email.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("email")),
                                "%" + email.toLowerCase() + "%"
                        );
    }

    public static Specification<Cliente> documentoLike(String dni) {
        return (root, query, builder) ->
                dni == null || dni.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.concat("", root.get("documento")), "%" + dni + "%");
    }

}
