package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.UserRole;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid_seq")
    @SequenceGenerator(allocationSize = 1, name = "users_userid_seq", sequenceName = "users_userid_seq")
    private Long userId;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 64, unique = true)
    private String username;
    @Column(length = 100)
    private String password;
    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    public User() {}

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdDate = LocalDate.now();
        this.role = UserRole.USER;
    }

    // TODO: remove for migration merge
    @Deprecated // For jdbc compatibility/compilation
    public User(long userId, String username, String password, String email, Date userDate, UserRole userRole) {

    }

    // TODO: remove for migration merge
    @Deprecated // For jdbc compatibility/compilation
    public User(long userId, String username, String password, String email, Date userDate, long imageId, UserRole userRole) {
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Image getImage() {
        return image;
    }
    public UserRole getRole() {
        return role;
    }

    public void updateImage(byte[] data) {
        this.image = new Image(data);
    }

    public User updateLegacyUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdDate = LocalDate.now();
        this.role = UserRole.USER;
        return this;
    }

}
