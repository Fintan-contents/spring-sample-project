package com.example.common;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * {@link CommonConfiguration}をインポートするクラス。
 * 
 * @author sample
 *
 */
public class CommonConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                CommonConfiguration.class.getName()
        };
    }
}
