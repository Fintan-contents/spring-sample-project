package com.example.web.security.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.generated.model.SystemAccount;
import com.example.common.generated.model.Users;
import com.example.web.security.configuration.SecurityLoginProperties;
import com.example.web.security.mapper.SecurityLoginMapper;

/**
 * ログインのService。
 * {@link UserDetailsService}実装クラス。
 * 
 * @author sample
 *
 */
@Service
@Transactional
public class SecurityLoginService implements UserDetailsService {

    @Autowired
    private SecurityLoginMapper mapper;

    @Autowired
    private SecurityLoginProperties properties;

    /**
     * ログインIDをもとにユーザー情報を取得して返す。
     * 
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(loginId)) {
            throw new UsernameNotFoundException(loginId);
        }

        SystemAccount systemAccount = mapper.selectSystemAccountByLoginId(loginId);
        if (systemAccount == null) {
            throw new UsernameNotFoundException(loginId);
        }

        Users users = mapper.selectUsersByPrimaryKey(systemAccount.getUserId());

        LocalDate now = LocalDate.now();

        String password = systemAccount.getUserPassword();
        boolean enabled = true;
        boolean accountNonExpired = !systemAccount.getEffectiveDateFrom().isAfter(now) && !systemAccount.getEffectiveDateTo().isBefore(now);
        boolean credentialsNonExpired = systemAccount.getPasswordExpirationDate().isAfter(now);
        boolean accountNonLocked = systemAccount.getFailedCount().intValue() <= properties.getFailedCountToLock();
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (users.getPmFlag()) {
            authorities.add(new SimpleGrantedAuthority("PROJECT_MANAGER"));
        }

        return new User(loginId, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    /**
     * ログイン認証失敗時に呼び出されるイベントリスナー。
     * ログイン失敗回数をインクリメントする。
     * 
     * @param event ログイン認証失敗イベント
     */
    @EventListener(AuthenticationFailureBadCredentialsEvent.class)
    public void incrementFailedCount(AuthenticationFailureBadCredentialsEvent event) {
        String loginId = event.getAuthentication().getName();
        mapper.updateSystemAccountIncrementFailedCountByLoginId(loginId);
    }

    /**
     * ログイン成功時に呼び出されるイベントリスナー。
     * ログイン失敗回数をリセットし、最終ログイン日時を更新する。
     * 
     * @param event ログイン成功イベント
     */
    @EventListener(AuthenticationSuccessEvent.class)
    public void resetFailedCountAndUpdateLastLoginDateTime(AuthenticationSuccessEvent event) {
        String loginId = event.getAuthentication().getName();
        LocalDateTime lastLoginDateTime = LocalDateTime.now();
        mapper.updateSystemAccountSetLoginInfoByLoginId(lastLoginDateTime, loginId);
    }
}
