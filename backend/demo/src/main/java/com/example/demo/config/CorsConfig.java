package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

	    // ========================================
	    // CORS設定
	    // ========================================
	    // Vue (http://localhost:5173) から
	    // Spring Boot (http://localhost:8080) への
	    // クロスオリジンリクエストを許可する
	    registry.addMapping("/**")

	            // リクエストを許可するオリジン
	            // Vue開発サーバーからのアクセスを許可
	            .allowedOrigins("http://localhost:5173")

	            // 許可するHTTPメソッド
	            // GET, POST, PUT, DELETE など全て許可
	            .allowedMethods("*")

	            // 許可するHTTPヘッダー
	            // Content-Type, Authorization など全て許可
	            .allowedHeaders("*")

	            // Cookieやセッション情報(JSESSIONID)の
	            // 送受信を許可する
	            // axiosのwithCredentials:trueとセットで利用
	            .allowCredentials(true);
	}
}
