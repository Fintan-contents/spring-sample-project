package com.example.web.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

/**
 * セキュリティに関する設定。
 * 
 * @author sample
 *
 */
@Configuration
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationEventPublisher eventPublisher;
    @Autowired
    private MessageSource messageSource;

    /**
     * 認可の設定とログイン・ログアウトの設定を行う。
     * 
     * @param http Spring Securityでセキュリティの設定を構築するためのクラス
     * @return セキュリティの設定がされた{@link SecurityFilterChain}
     * @throws Exception Spring Securityがスローする
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 認可の設定
                .authorizeHttpRequests(c -> c
                        // ログイン画面は誰でも見られる
                        .requestMatchers("/login").permitAll()
                        // エラー画面は誰でも見られる
                        .requestMatchers("/error").permitAll()
                        // CSSとJSは誰でも参照できる
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        // ヘルスチェックのエンドポイントは認証しない
                        .requestMatchers("/actuator/health").permitAll()
                        // プロジェクト登録画面とプロジェクト更新画面、プロジェクトアップロード画面はプロマネしか見られない
                        .requestMatchers("/project/create/**", "/project/update/**", "/project/upload/**").hasAuthority("PROJECT_MANAGER")
                        // 上記以外の画面は認証が必要
                        .anyRequest().authenticated())

                // ログインの設定
                .formLogin(c -> c
                        // ログイン認証を行うパスを設定する
                        .loginPage("/login")
                        // ユーザー名のパラメーター名を設定する
                        .usernameParameter("loginId")
                        // パスワードのパラメーター名を設定する
                        .passwordParameter("userPassword")
                        // ログイン後は必ずトップページへ遷移する
                        .defaultSuccessUrl("/", true))

                // ログアウトの設定
                .logout(c -> c
                        // ログアウトを行うパスを設定する
                        .logoutUrl("/logout"))

                .authenticationManager(authenticationManager())

                .headers(c -> c
                        // Referrer-Policyレスポンスヘッダの設定
                        // https://developer.mozilla.org/ja/docs/Web/HTTP/Headers/Referrer-Policy
                        .referrerPolicy(policy -> policy
                                .policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)))
                .build();
    }

    /**
     * PBKDF2でパスワードをハッシュ化するパスワードエンコーダーを生成する。
     * 
     * @return パスワードエンコーダー
     */
    @Bean
    public Pbkdf2PasswordEncoder passwordEncoder() {
        String secret = "";
        int saltLength = 16;
        int iterations = 10000;
        int hashWidth = 256;
        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(secret, saltLength, iterations, hashWidth);
        passwordEncoder.setEncodeHashAsBase64(true);
        return passwordEncoder;
    }

    /**
     * {@link AuthenticationManager}のインスタンスを生成する。
     * 
     * @return {@link AuthenticationManager}のインスタンス
     */
    @Bean
    public ProviderManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        // ここでMessageSourceを設定するため、明示的にDaoAuthenticationProviderを構築している。
        // ※MessageSourceを除けば自動で設定されるため、Spring Securityがデフォルトで用意している
        // メッセージで事足りるなら明示的にDaoAuthenticationProviderを構築しなくてよい。
        provider.setMessageSource(messageSource);

        ProviderManager providerManager = new ProviderManager(provider);
        providerManager.setAuthenticationEventPublisher(eventPublisher);
        return providerManager;
    }
}
