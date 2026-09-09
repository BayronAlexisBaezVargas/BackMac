package backend.backmac.controller;

import backend.backmac.model.Usuario;
import backend.backmac.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<Map<String, Object>> login(@AuthenticationPrincipal Jwt jwt) {
        String azureOid = jwt.getClaimAsString("oid");
        String email = jwt.getClaimAsString("preferred_username");
        String nombre = jwt.getClaimAsString("name");

        if (email == null || email.isBlank()) email = jwt.getClaimAsString("upn");
        if (email == null || email.isBlank()) email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) email = azureOid + "@sin-correo.local";
        if (nombre == null || nombre.isBlank()) nombre = email;

        final String finalEmail = email;
        final String finalNombre = nombre;
        final String finalOid = azureOid;

        Usuario usuario = usuarioRepository.findByAzureOid(azureOid)
            .orElseGet(() -> usuarioRepository.findByEmail(finalEmail)
                .orElseGet(() -> {
                    Usuario nuevo = Usuario.builder()
                        .email(finalEmail)
                        .nombre(finalNombre)
                        .azureOid(finalOid)
                        .roles("ROLE_USER")
                        .build();
                    return usuarioRepository.save(nuevo);
                })
            );

        usuario.setLastLogin(LocalDateTime.now());
        if (usuario.getAzureOid() == null) {
            usuario.setAzureOid(azureOid);
        }
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
            "email", usuario.getEmail(),
            "nombre", usuario.getNombre(),
            "roles", usuario.getRoles(),
            "lastLogin", usuario.getLastLogin().toString()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ms-login OK");
    }
}
