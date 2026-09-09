package backend.backmac.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String nombre;

    @Column(name = "azure_oid", unique = true)
    private String azureOid;

    private String roles;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
