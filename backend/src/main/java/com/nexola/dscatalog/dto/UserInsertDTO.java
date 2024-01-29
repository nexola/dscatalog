package com.nexola.dscatalog.dto;

import com.nexola.dscatalog.entities.User;
import com.nexola.dscatalog.services.validations.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
    private String password;

    public UserInsertDTO() {
        super();
    }

    public UserInsertDTO(String password) {
        this.password = password;
    }

    public UserInsertDTO(Long id, String firstName, String lastName, String email, String password, String password1) {
        super(id, firstName, lastName, email, password);
        this.password = password1;
    }

    public UserInsertDTO(User entity, String password) {
        super(entity);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
