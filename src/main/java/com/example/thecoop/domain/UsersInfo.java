package com.example.thecoop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author iveshtard
 * @since 10/8/2018
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class UsersInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private Date birthday;
    private String location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersInfo info = (UsersInfo) o;
        return Objects.equals(id, info.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
