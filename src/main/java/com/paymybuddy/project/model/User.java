package com.paymybuddy.project.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;

@Data
@Entity
@Table(name="user", indexes = @Index(name="user_email_idx",columnList = "email"))
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(length = 100,nullable = false)
    private String email;

    @NotNull
    @Size(max=30)
    @Column(length = 30,nullable = false)
    private String firstname;

    @NotNull
    @Size(max=60)
    @Column(length = 60,nullable = false)
    private String lastname;

    @NotNull
    @Size(max=80)
    @Column(length = 80,nullable = false)
    private String password;

    @Column(precision = 12,scale=2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
