package com.example.demo.utility;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    
    private final int bearerAfter = 7;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Authorizationヘッダーを取得
        // 例: "Bearer eyJhbGciOiJIUzI1NiJ9..."
        String header =
                request.getHeader("Authorization");

        // Authorizationヘッダーが存在し、
        // "Bearer "から始まる場合のみJWT認証を実施
        if (header != null
                && header.startsWith("Bearer ")) {

            // "Bearer "を除いたJWT本体を取得
            String token =
                    header.substring(bearerAfter);

            // JWTの署名や有効期限を検証
            if (jwtService.validateToken(token)) {

                // JWTのPayloadからメールアドレスを取得
                String email =
                        jwtService.extractEmail(token);

                // Spring Security用の認証情報を作成
                // Principal : email
                // Credentials : なし(null)
                // Authorities : 権限なし(emptyList)
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.emptyList());

                // SecurityContextに認証情報を登録
                // ここでSpring Securityが
                // 「このユーザーは認証済み」と判断する
                // JWT認証は送られてきたトークンから認証情報を毎回作り直す
                // セッション認証は送られてきたJSESSIONIDでサーバー側の
                // セッションから復元する
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            }
        }

        // 次のフィルターへ処理を引き渡す
        filterChain.doFilter(
                request,
                response);
    }
}