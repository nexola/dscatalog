package com.nexola.dscatalog.dto;

import com.nexola.dscatalog.entities.Role;

public class RoleDTO {
    private Long id;
    private String authority;

    public RoleDTO() {}

    public RoleDTO(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
