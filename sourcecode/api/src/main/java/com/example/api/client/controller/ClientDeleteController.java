package com.example.api.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.client.service.ClientDeleteService;

/**
 * 顧客削除API
 */
@RestController
public class ClientDeleteController {

    @Autowired
    private ClientDeleteService service;

    /**
     * 顧客IDに一致する顧客を削除する。
     * 
     * @param clientId 顧客ID
     */
    @DeleteMapping("clients/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Integer clientId) {
        service.deleteClient(clientId);
    }
}
