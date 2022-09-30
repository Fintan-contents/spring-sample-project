package com.example.api.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.client.request.ClientCreationRequest;
import com.example.api.client.response.ClientCreationResponse;
import com.example.api.client.service.ClientCreateService;
import com.example.common.generated.model.Client;

/**
 * 顧客登録API
 */
@RestController
public class ClientCreateController {

    @Autowired
    private ClientCreateService clientService;

    /**
     * 顧客を登録する。
     * 
     * @param request 登録する顧客の情報
     * @return システムで採番した顧客IDをもった顧客情報
     */
    @PostMapping("clients")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientCreationResponse createClient(@RequestBody @Validated ClientCreationRequest request) {
        Client client = request.toClient();
        clientService.createClient(client);

        return ClientCreationResponse.fromClient(client);
    }
}
