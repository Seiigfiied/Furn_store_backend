package com.chesnakov.hardwareecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private String roleName;

    private String roleDescription;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
