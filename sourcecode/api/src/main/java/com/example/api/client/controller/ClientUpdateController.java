package com.example.api.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.client.request.ClientUpdateRequest;
import com.example.api.client.response.ClientUpdateResponse;
import com.example.api.client.service.ClientUpdateService;
import com.example.common.generated.model.Client;

/**
 * 顧客更新API
 */
@RestController
public class ClientUpdateController {

    @Autowired
    private ClientUpdateService service;

    /**
     * 顧客を更新する。
     *
     * @param clientId 顧客ID
     * @param request 更新する顧客の情報
     * @return 更新後の顧客情報
     */
    @PutMapping("clients/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientUpdateResponse updateClient(@PathVariable Integer clientId,
            @RequestBody @Validated ClientUpdateRequest request) {
        Client client = request.toClient(clientId);
        service.updateClient(client);
        return ClientUpdateResponse.fromClient(client);
    }
}
