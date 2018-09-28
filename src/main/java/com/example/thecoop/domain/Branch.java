package com.example.thecoop.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author iveshtard
 * @since 9/5/2018
 */

@Entity
@Getter @Setter
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Topic name cannot be empty")
    @Length(max = 100, message = "Name too long (more than 100)")
    private String name;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Message> messages;

    private String description;

    private boolean dialog;
}
