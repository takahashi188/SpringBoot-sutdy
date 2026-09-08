package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	// application.properties(yml)に定義した秘密鍵を取得
	@Value("${jwt.secret}")
	private String secretKey;

	// JWTの有効期限（1時間）
	private final long expirationTime = 1000 * 60 * 2;

	/**
	 * JWTの署名・検証に使用するSecretKeyを生成
	 * secretKey文字列をUTF-8のバイト配列に変換してHMAC用キーを作成する
	 */
	private SecretKey getSigningKey() {
	    return Keys.hmacShaKeyFor(
	            secretKey.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * JWTを生成する
	 *
	 * @param email ログインユーザーのメールアドレス
	 * @return 生成したJWT
	 */
	public String generateToken(String email) {
	    return Jwts.builder()
	            // JWTのSubjectにメールアドレスを設定
	            .subject(email)

	            // トークン発行日時を設定
	            .issuedAt(new Date())

	            // 有効期限を設定（現在時刻 + 1時間）
	            .expiration(
	                    new Date(System.currentTimeMillis()
	                            + expirationTime))

	            // 秘密鍵で署名
	            .signWith(getSigningKey())

	            // JWT文字列を生成
	            .compact();
	}

	/**
	 * JWTが有効か検証する
	 *
	 * @param token JWT
	 * @return 有効ならtrue、期限切れや改ざんされていればfalse
	 */
	public boolean validateToken(String token) {
	    try {
	        // 署名検証および有効期限チェックを実施
	        Jwts.parser()
	        		// 署名の検証のために秘密鍵を設定
	                .verifyWith(getSigningKey())
	                .build()
	                // JWTを解析
	                .parseSignedClaims(token);

	        return true;
	    } catch (Exception e) {
	        // トークン不正、期限切れ、署名エラーなど
	        return false;
	    }
	}

	/**
	 * JWTからメールアドレスを取得する
	 *
	 * @param token JWT
	 * @return Subjectに格納されたメールアドレス
	 */
	public String extractEmail(String token) {
	    return Jwts.parser()
	            .verifyWith(getSigningKey())
	            .build()
	            .parseSignedClaims(token)

	            // JWTのPayload(ユーザー情報)を取得
	            .getPayload()

	            // Subject(email)を取得
	            .getSubject();
	}
}