package com.example.api.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.client.mapper.ClientDeleteMapper;
import com.example.api.common.exception.BusinessException;

/**
 * 顧客削除業務ロジック
 */
@Service
@Transactional
public class ClientDeleteService {

    @Autowired
    private ClientDeleteMapper mapper;

    /**
     * 顧客削除を行う。
     * 
     * @param clientId 顧客ID
     */
    public void deleteClient(Integer clientId) {
        int count = mapper.deleteClientByPrimaryKey(clientId);
        if (count == 0) {
            throw new BusinessException("FB1999903", HttpStatus.NOT_FOUND, "errors.nothing");
        }
    }
}
