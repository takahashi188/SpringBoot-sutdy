package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security用のユーザー情報取得サービス
 *
 * ログイン時に入力されたメールアドレスを元に
 * DBからユーザー情報を取得し、
 * Spring Securityが利用できるUserDetailsへ変換する。
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // ユーザー情報取得用Repository
    private final UserRepository userRepository;

    /**
     * Spring Securityが認証時に自動呼び出しするメソッド
     *
     * usernameParameter("email") で指定されたメールアドレスが
     * 引数として渡される。
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // メールアドレスでユーザー検索
        // ユーザーが存在しない場合は認証失敗とする
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("ユーザーが存在しません"));

        // 取得したユーザー情報を
        // Spring SecurityのUserDetailsへ変換
        return org.springframework.security.core.userdetails.User
                // 認証ユーザー名としてメールアドレスを設定
                .withUsername(user.getEmail())

                // DBに保存されたハッシュ化済みパスワード
                .password(user.getPassword())

                // ユーザー権限
                .roles("USER")

                // UserDetails生成
                .build();
    }
}
