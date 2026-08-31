package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 設定クラスのアノテーション
@Configuration

// Auditing機能の使用
@EnableJpaAuditing
public class JpaAuditingConfig {

}
