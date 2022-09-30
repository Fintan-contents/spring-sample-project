package com.example.api.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.client.response.ClientDetailResponse;
import com.example.api.client.service.ClientDetailService;
import com.example.common.generated.model.Client;

/**
 * 顧客詳細API
 */
@RestController
public class ClientDetailController {

    @Autowired
    private ClientDetailService service;

    /**
     * 顧客IDに一致する顧客の詳細情報を返す。
     * 
     * @param clientId 顧客ID
     * @return 顧客の詳細情報
     */
    @GetMapping("clients/{clientId}")
    public ClientDetailResponse getClient(@PathVariable Integer clientId) {
        Client client = service.getClient(clientId);

        return ClientDetailResponse.fromClient(client);
    }
}
