package com.example.api.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.client.request.ClientSearchRequest;
import com.example.api.client.response.ClientSearchResponse;
import com.example.api.client.service.ClientSearchService;
import com.example.common.generated.model.Client;

/**
 * 顧客検索API
 */
@RestController
public class ClientSearchController {

    @Autowired
    private ClientSearchService service;

    /**
     * 顧客検索を行う。
     * 
     * @param request 検索条件
     * @return 検索条件に一致する顧客の一覧
     */
    @GetMapping("clients")
    public ClientSearchResponse searchClient(@Validated ClientSearchRequest request) {
        List<Client> clientList = service.searchClient(request.toClientSearchCriteria());

        return ClientSearchResponse.fromClientList(clientList);
    }
}
