package com.example.api.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.client.mapper.ClientDetailMapper;
import com.example.api.common.exception.BusinessException;
import com.example.common.generated.model.Client;

/**
 * 顧客詳細業務ロジック
 */
@Service
@Transactional
public class ClientDetailService {

    @Autowired
    private ClientDetailMapper mapper;

    /**
     * 顧客詳細を取得する。
     * 
     * @param clientId 顧客ID
     * @return 顧客情報
     */
    @Transactional(readOnly = true)
    public Client getClient(Integer clientId) {
        Client client = mapper.selectClientByPrimaryKey(clientId);
        if (client == null) {
            throw new BusinessException("FB1999903", HttpStatus.NOT_FOUND, "errors.nothing");
        }

        return client;
    }
}
