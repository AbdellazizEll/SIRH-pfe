package com.example.anywrpfe.entities.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    GESTRH_READ("gestrh:read"),
    GESTRH_UPDATE("gestrh:update"),
    GESTRH_CREATE("gestrh:create"),
    GESTRH_DELETE("gestrh:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete"),

    COLLABORATOR_READ("collaborator:read"),
    COLLABORATOR_UPDATE("collaborator:update"),
    COLLABORATOR_CREATE("collaborator:create"),
    COLLABORATOR_DELETE("collaborator:delete")

    ;

    @Getter
    private final String permission;



}
