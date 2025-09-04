package api.v1.KPI.Management.System.jwt.util;


import api.v1.KPI.Management.System.jwt.dto.JwtDTO;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final int tokenLiveTimeOneHour = 1000 * 3600;    // 1-hour
    private static final int tokenLiveTimeOneDay = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "veryLongSecretmazgillattayevlasharaaxmojonjinnijonsurbetbekkiydirhonuxlatdibekloxovdangasabekochkozjonduxovmashaynikmaydagapchishularnioqiganbolsangizgapyoqaniqsizmazgi";


    public static String encode(String username, String id, ProfileRole role) {
        return Jwts
                .builder()
                .subject(username)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTimeOneDay))
                .signWith(getSignInKey())
                .compact();
    }

    public static JwtDTO decode(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String username = claims.getSubject();
        String strRole = (String) claims.get("role");
        String id = (String) claims.get("id");
        List<ProfileRole> roleList = Arrays.stream(strRole.split(","))
                .map(ProfileRole::valueOf)
                .collect(Collectors.toList());

        return new JwtDTO(id, username, roleList);
    }

    public static String encodeVer(String id) {

        return Jwts
                .builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTimeOneHour))
                .signWith(getSignInKey())
                .compact();
    }
    public static String decodeRegVerToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    private static SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
