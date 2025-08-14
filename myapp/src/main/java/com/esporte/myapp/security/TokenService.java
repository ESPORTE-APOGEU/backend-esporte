import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import com.esporte.myapp.entity.User;

import java.util.Date;

@Service
public class TokenService {

    private final String secret = "senhaAleatoriaQueEuAcabeiDePensar";

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
