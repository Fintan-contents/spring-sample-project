package com.example.common.nablarch.validation;

import nablarch.core.validation.ee.DomainManager;

/**
 * 本システムのDomainManager実装クラス。
 * 
 * @author sample
 *
 */
public class ExampleDomainManager implements DomainManager<DomainBean> {

    @Override
    public Class<DomainBean> getDomainBean() {
        return DomainBean.class;
    }
}