package com.example.web.common.token.transaction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jp.fintan.keel.spring.web.token.transaction.TransactionTokenInterceptor;
import jp.fintan.keel.spring.web.token.transaction.TransactionTokenRequestDataValueProcessor;

/**
 * 二重送信防止機能の設定を行うConfigurationクラス。
 */
@Configuration(proxyBeanMethods = false)
public class TransactionTokenConfiguration implements WebMvcConfigurer {

    /**
     * CsrfRequestDataValueProcessorとTransactionTokenRequestDataValueProcessorを
     * コンポーネント登録するためのBeanDefinitionRegistryPostProcessorを構築する。
     * 
     * @return BeanDefinitionRegistryPostProcessor
     */
    @Bean
    public static BeanDefinitionRegistryPostProcessor requestDataValueProcessorPostProcessor() {
        return new BeanDefinitionRegistryPostProcessor() {

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                // noop
            }

            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                /*
                 * 二重送信防止機能を有効にするためには、 requestDataValueProcessor という名前で
                 * RequestDataValueProcessor の Bean を登録する必要がある。
                 * しかし、この名前の Bean は Spring Security の AutoConfiguration が
                 * CsrfRequestDataValueProcessor を登録するために既に使用してしまっている。
                 * そこで、一旦 requestDataValueProcessor の Bean 定義を削除し、
                 * 改めて CompositeRequestDataValueProcessor で CsrfRequestDataValueProcessor と
                 * TransactionTokenRequestDataValueProcessor を合成した Bean を登録しなおしている。
                 *
                 * 参考: https://github.com/spring-projects/spring-boot/issues/4676#issuecomment-163249226
                 */
                registry.removeBeanDefinition("requestDataValueProcessor");
                registry
                        .registerBeanDefinition("requestDataValueProcessor",
                                new RootBeanDefinition(CompositeRequestDataValueProcessor.class, () -> {
                                    return new CompositeRequestDataValueProcessor(
                                            new CsrfRequestDataValueProcessor(),
                                            new TransactionTokenRequestDataValueProcessor());
                                }));
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new TransactionTokenInterceptor())
                // 二重送信防止対象のリクエストパス
                .addPathPatterns("/**")
                .order(Ordered.LOWEST_PRECEDENCE - 10);
    }
}
