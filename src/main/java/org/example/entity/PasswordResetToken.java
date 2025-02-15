package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetails user;

    private String token;
    private Date expiryDate;

    public PasswordResetToken(UserDetails user, String token, Date date) {
        this.user = user;
        this.token = token;
        this.expiryDate = date;
    }
}
