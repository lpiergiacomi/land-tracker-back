package com.api.landtracker.model.filter;

import com.api.landtracker.model.entities.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {
    private ClientSpecification() {
    }

    public static Specification<Client> clientNameLike(String name) {
        return (root, query, builder) ->
                name == null || name.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

    public static Specification<Client> emailLike(String email) {
        return (root, query, builder) ->
                email == null || email.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.lower(root.get("email")),
                                "%" + email.toLowerCase() + "%"
                        );
    }

    public static Specification<Client> documentLike(String dni) {
        return (root, query, builder) ->
                dni == null || dni.equals("") ?
                        builder.conjunction() :
                        builder.like(builder.concat("", root.get("document")), "%" + dni + "%");
    }

}
