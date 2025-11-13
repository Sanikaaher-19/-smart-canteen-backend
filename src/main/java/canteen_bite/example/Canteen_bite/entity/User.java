package canteen_bite.example.Canteen_bite.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; // stored as bcrypt hash

    @Enumerated(EnumType.STRING)
    private Role role;
}
