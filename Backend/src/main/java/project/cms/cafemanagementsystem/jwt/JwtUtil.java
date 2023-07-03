package project.cms.cafemanagementsystem.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String secret = "btechdays";

    // Nhan token va trich xuat username tu trong token do
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    // Nhan token va trich xuat thoi gian het han tu trong token do
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    // Nhan token va doi tuong Function de trich xuat cac claims tu trong token do
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Nhan token va trich xuat tat ca cac claims tu trong token do
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //Nhan token va kiem tra xem no da het han hay chua, dua tren thoi gian het han duoc trich xuat tu token
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //Nhan username, role de tao ra 1 token
    public String generateToken(String username, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    // Tao token, set cac claims
    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    // Nhan token va thong tin nguoi dung, kiem tra tinh hop le cua token va check token het han hay chua
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
