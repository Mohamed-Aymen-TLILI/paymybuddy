package com.paymybuddy.project.model;

import lombok.*;


import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 45)
    private String nickname;

    @Column(nullable = false, precision = 18, scale = 2)
    private double amount;

    @JoinTable(name="connections", joinColumns = {
            @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "target_id", referencedColumnName = "id", nullable = false)
    })
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<User> listFriend;

    public User (String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;

    }

    public User (Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;

    }

}
