package com.example.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Table(name = "Account")
public class Account {
    @Id
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}
