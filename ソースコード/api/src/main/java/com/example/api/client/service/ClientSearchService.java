package com.example.api.client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.client.configuration.ClientSearchProperties;
import com.example.api.client.mapper.ClientSearchMapper;
import com.example.api.client.model.search.ClientSearchCriteria;
import com.example.api.common.exception.BusinessException;
import com.example.common.generated.model.Client;

/**
 * 顧客検索業務ロジック
 */
@Service
@Transactional
public class ClientSearchService {

    @Autowired
    private ClientSearchMapper mapper;

    @Autowired
    private ClientSearchProperties properties;

    /**
     * 顧客検索を行う。
     * 
     * @param criteria 検索条件
     * @return 顧客の一覧
     */
    @Transactional(readOnly = true)
    public List<Client> searchClient(ClientSearchCriteria criteria) {
        int upperLimit = properties.getSearchUpperLimit();
        if (mapper.countClientByCriteria(criteria) > upperLimit) {
            throw new BusinessException("FB1999902", HttpStatus.BAD_REQUEST, "errors.upper.limit", upperLimit);
        }

        return mapper.selectClientByCriteria(criteria);
    }
}
