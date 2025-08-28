package com.gugus.batch.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author : smk
 * @fileName : Users
 * @date : 2025. 7. 12.
 */
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "user_id", length = 32, unique = true, nullable = false)
    private String userId = UUID.randomUUID().toString().replace("-", "");

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "email", length = 128, unique = true, nullable = false)
    private String email;

    @Column(name = "creator_no")
    private Long creatorNo;

    @Column(name = "firebase_uid")
    private String firebaseUid;

    @Column(name = "totp_uri")
    private String totpUri;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void setPasswordByAdmin(String salt, String password){
        this.salt = salt;
        this.password = password;
    }

    public void setFirebaseUidByLogin(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public void setTotpUriByLogin(String totpUri) {
        this.totpUri = totpUri;
    }
}
