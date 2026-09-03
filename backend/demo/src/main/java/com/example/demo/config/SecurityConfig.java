package com.example.demo.config;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// ========================================
		// ① 認可設定
		// ========================================
		// /login と /api/users/ は未ログインでもアクセス可能
		// それ以外のURLは認証必須
		http.authorizeHttpRequests((requests) -> requests
				// 未ログインでのアクセスを許可
				.requestMatchers("/login", "/api/users/")
				.permitAll()
				// それ以外のパスはログイン済みならアクセス可能
				.anyRequest().authenticated())

		// ========================================
		// ② フォームログイン設定
		// ========================================
		.formLogin((login) -> login

				// ログインフォームで送信されるパラメータ名
				// email → UserDetailsServiceに渡されるユーザーID
				.usernameParameter("email")

				// パスワードパラメータ名
				.passwordParameter("password")

				// ログイン処理を受け付けるURL
				// POST /login が呼ばれると認証処理を実行
				.loginProcessingUrl("/login")

				// 認証後は自動的に"/"にアクセスするのを防ぐために明示的に成功時と失敗時を記述
				// 認証成功時の処理
				// SecurityContext に認証情報が保存され、
				// JSESSIONID が発行される
				.successHandler((request, response, authentication) -> {
					response.setStatus(HttpServletResponse.SC_OK);
				})

				// 認証失敗時の処理
				.failureHandler((request, response, exception) -> {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				})

				.permitAll())

		// ========================================
		// ③ ログアウト設定
		// ========================================
		.logout(logout -> logout

				// ログアウトURL
				.logoutUrl("/logout")

				// HttpSessionを破棄
				.invalidateHttpSession(true)

				// SecurityContextの認証情報を削除
				.clearAuthentication(true)

				// JSESSIONID Cookieを削除
				.deleteCookies("JSESSIONID")

				// ログアウト成功時の処理
				.logoutSuccessHandler((request, response, authentication) -> {
					response.setStatus(HttpServletResponse.SC_OK);
				})

				.permitAll());

		// ========================================
		// ④ CORS / CSRF設定
		// ========================================
		// Vue(5173) → Spring Boot(8080) の
		// クロスオリジン通信を許可
		//
		// セッション認証を利用するため
		// axios の withCredentials と組み合わせて利用
		//
		// API開発中のためCSRFは無効化
		http.cors(cors -> {})
		.csrf(csrf -> csrf.disable());

		// ========================================
		// ⑤ 未認証アクセス時の処理
		// ========================================
		// 認証が必要なURLへ未ログイン状態でアクセスした場合
		// Spring標準のログイン画面へリダイレクトせず
		// 401を返却する
		http.exceptionHandling(exception -> exception
				.authenticationEntryPoint(
						(request, response, authException) -> {
							response.sendError(
									HttpServletResponse.SC_UNAUTHORIZED);
						}));

		return http.build();
	}

	/**
	 * Spring Securityが利用するAuthenticationManagerを取得するBean
	 *
	 * AuthenticationManagerは認証処理の司令塔となるコンポーネント。
	 * ログイン要求を受け取ると、
	 * UserDetailsServiceによるユーザー検索や
	 * PasswordEncoderによるパスワード照合を実行する。
	 */
	@Bean
	AuthenticationManager authenticationManager(
	        AuthenticationConfiguration configuration)
	        throws Exception {

	    // Spring Securityが自動生成したAuthenticationManagerを取得
	    return configuration.getAuthenticationManager();
	}

}