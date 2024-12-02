package com.farmin.farminserver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.farmin.farminserver.common.error.ErrorCode;
import com.farmin.farminserver.common.exception.ApiException;
import com.farmin.farminserver.common.utils.JwtUtils;
import com.farmin.farminserver.domain.user.dto.CustomUserDetail;
import com.farmin.farminserver.entity.user.UserEntity;
import com.farmin.farminserver.entity.user.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 인증이 필요 없는 경로들을 추가
        return path.equals("/") || // 루트 경로
                path.startsWith("/open-api/user/join") || // 회원가입 경로
                path.startsWith("/open-api/user/login") || // 로그인 경로
                path.contains("/ftp-service") || // FTP 서비스
                path.startsWith("/actuator/") || // Spring Actuator
                path.equals("/favicon.ico") || // Favicon 요청
                path.startsWith("/robots.txt"); // robots.txt 요청
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Request URI: {}", request.getRequestURI());
        log.info("Request Method: {}", request.getMethod());
        log.info("Authorization Header: {}", request.getHeader("Authorization"));

        // 인증 불필요 경로는 필터를 통과시킴
        String path = request.getRequestURI();
        if (path.equals("/") || path.equals("/favicon.ico") || path.startsWith("/open-api")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Authorization 헤더가 없거나 Bearer 토큰이 아니면 예외 처리
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.warn("Authorization 헤더 누락 또는 토큰 형식 오류 - 경로: {}", request.getRequestURI());
                throw new ApiException(ErrorCode.UNAUTHORIZED, "Authorization 헤더 누락 또는 토큰 형식 오류");
            }

            String jwtToken = authorizationHeader.substring(7);
            String subject = jwtUtils.getSubjectFromToken(jwtToken);

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserEntity entity = userRepository.findByUsername(subject);
                if (entity != null && jwtUtils.validateToken(jwtToken, entity)) {
                    CustomUserDetail customUserDetail = new CustomUserDetail(entity);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(customUserDetail, null, customUserDetail.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ApiException e) {
            // SecurityContext를 클리어하고 API 예외를 다시 던짐
            SecurityContextHolder.clearContext();
            log.warn("인증 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            // 기타 예외 처리
            SecurityContextHolder.clearContext();
            log.error("JWT 처리 중 오류 발생 - 경로: {}", request.getRequestURI(), e);
            throw new ApiException(ErrorCode.UNAUTHORIZED, "인증 처리 중 오류가 발생했습니다");
        }

        // 다음 필터 체인 실행
        filterChain.doFilter(request, response);
    }
}
