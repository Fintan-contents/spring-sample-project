package com.example.api.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.client.mapper.ClientUpdateMapper;
import com.example.api.common.exception.BusinessException;
import com.example.common.exception.OptimisticLockException;
import com.example.common.generated.model.Client;

/**
 * 顧客更新業務ロジック
 */
@Service
@Transactional
public class ClientUpdateService {

    @Autowired
    private ClientUpdateMapper mapper;

    /**
     * 顧客更新を行う。
     *
     * @param model 顧客情報
     */
    public void updateClient(Client model) {
        Client target = mapper.selectClientByPrimaryKey(model.getClientId());
        if (target == null) {
            throw new BusinessException("FB1999903", HttpStatus.NOT_FOUND, "errors.nothing");
        }
        if (!target.getClientName().equals(model.getClientName())
                && (mapper.countByClientName(model.getClientName()) > 0)) {
            throw new BusinessException("FB1999904", HttpStatus.CONFLICT, "errors.register.duplicate");
        }
        int count = mapper.updateClientByPrimaryKey(model);
        if (count == 0) {
            throw new OptimisticLockException();
        }
        model.setVersionNo(model.getVersionNo() + 1);
    }
}
