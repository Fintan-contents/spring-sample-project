package com.example.api.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.client.mapper.ClientCreateMapper;
import com.example.api.common.exception.BusinessException;
import com.example.common.generated.model.Client;

/**
 * 顧客登録業務ロジック
 */
@Service
@Transactional
public class ClientCreateService {

    @Autowired
    private ClientCreateMapper clientMapper;

    /**
     * 顧客登録を行う。
     * 
     * @param model 顧客情報
     */
    public void createClient(Client model) {
        if (clientMapper.countByClientName(model.getClientName()) > 0) {
            throw new BusinessException("FB1999904", HttpStatus.CONFLICT, "errors.register.duplicate");
        }
        model.setVersionNo(1L);
        clientMapper.insertClient(model);
    }
}
