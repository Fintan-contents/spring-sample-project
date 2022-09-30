package com.example.web.project.helper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.web.project.dto.detail.ClientDto;

/**
 * プロジェクト詳細機能のViewHelper。
 *
 * @author sample
 */
@Component
@Transactional(readOnly = true)
public class ProjectDetailViewHelper {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 顧客の名前を取得する。
     * 
     * @param clientId 顧客ID
     * @return 顧客の名前。見つからない場合はnullを返す。
     */
    public String getClientName(Integer clientId) {
        if (clientId == null) {
            return null;
        }
        try {
            ResponseEntity<ClientDto> response = restTemplate.getForEntity("/clients/{clientId}", ClientDto.class, clientId);
            ClientDto clientDto = response.getBody();
            if (clientDto == null) {
                return null;
            }
            return clientDto.getClientName();
        } catch (RestClientException e) {
            LoggerFactory.getLogger(ProjectDetailViewHelper.class).info(e.getMessage()); //サンプルではログ出力するにとどめる
            return null;
        }
    }

    /**
     * 改行で分割したリストを返す。
     * 
     * @param text 文字列
     * @return 改行で分割されたリスト
     */
    public List<String> splitByLineBreak(String text) {
        return List.of(StringUtils.defaultString(text).split("\r?\n"));
    }
}
