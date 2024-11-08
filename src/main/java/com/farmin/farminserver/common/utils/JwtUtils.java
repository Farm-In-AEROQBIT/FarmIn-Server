package com.farmin.farminserver.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import com.farmin.farminserver.entity.refreshtoken.RefreshToken;
import com.farmin.farminserver.entity.refreshtoken.RefreshTokenRepository;
import com.farmin.farminserver.entity.user.UserEntity;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private Key hmacKey;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtils(Environment env, RefreshTokenRepository refreshTokenRepository) {
        log.info("현재 활성화된 프로파일: {}", Arrays.toString(env.getActiveProfiles()));
        String secretKey = env.getProperty("jwt.secret");
        if (secretKey == null) {
            log.error("JWT 비밀 키가 null입니다.");
            throw new IllegalStateException("JWT 비밀 키가 구성되지 않았습니다. application.yaml을 확인해주세요.");
        }

        this.hmacKey = new SecretKeySpec(
                secretKey.getBytes(), // Base64 인코딩이 필요없다면 이렇게 간단히
                SignatureAlgorithm.HS256.getJcaName()
        );
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateAccessToken(UserEntity userEntity) {
        Date nowDate = new Date();
        Date expiration = new Date(nowDate.getTime() + Duration.ofHours(2).toMillis());
        String jwtToken = Jwts.builder()
                .claim("name", userEntity.getName())
                .claim("sub", userEntity.getUsername())
                .claim("jti", String.valueOf(userEntity.getId()))
                .claim("role", userEntity.getRole())
                .claim("iat", nowDate)
                .claim("exp", expiration)
                .signWith(hmacKey)
                .compact();
        log.debug(jwtToken);
        return jwtToken;
    }

    public String generateRefreshToken(UserEntity userEntity) {
        Date nowDate = new Date();
        Date expiration = new Date(nowDate.getTime() + Duration.ofDays(30).toMillis());
        String jwtToken = Jwts.builder()
                .claim("name", userEntity.getName())
                .claim("sub", userEntity.getUsername())
                .claim("jti", String.valueOf(userEntity.getId()))
                .claim("role", userEntity.getRole())
                .claim("iat", nowDate)
                .claim("exp", expiration)
                .signWith(hmacKey)
                .compact();
        RefreshToken refreshToken = new RefreshToken(userEntity.getUsername(),jwtToken);
        refreshTokenRepository.save(refreshToken);
        log.debug(jwtToken);
        return jwtToken;
    }

    private Claims getAllClaimsFromToken(String token) {
        Jws<Claims> jwt = Jwts.parser().setSigningKey(hmacKey).build().parseClaimsJws(token);
        return jwt.getBody();
    }

    public String getSubjectFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public String getNameFromToken(String token){
        final Claims claims = getAllClaimsFromToken(token);
        return String.valueOf(claims.get("name"));
    }

    private Date getExpirationDateFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserEntity userEntity) {
        // 토큰 유효기간 체크
        if (isTokenExpired(token)) {
            return false;
        }

        // 토큰 내용을 검증
        String subject = getSubjectFromToken(token);
        String username = userEntity.getUsername();

        return subject != null && username != null && subject.equals(username);
    }
}