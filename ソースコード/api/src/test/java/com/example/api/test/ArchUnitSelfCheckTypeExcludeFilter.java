package com.example.api.test;

import java.io.IOException;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * ArchUnitの動作確認用のクラスをSpring Bootのコンポーネントスキャン対象外にするためのフィルター。
 *
 */
public class ArchUnitSelfCheckTypeExcludeFilter extends TypeExcludeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
            throws IOException {
        return metadataReader
                .getClassMetadata().getClassName()
                .startsWith("com.example.api.archunit.selfcheck");
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}