package com.example.web.common.mvc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

/**
 * CSVファイルのダウンロードを行うView実装。
 * 
 * @author sample
 *
 */
@Component
public class CsvFileDownloadView extends AbstractView {

    public static final String DOWNLOAD_FILE_INFO_KEY = "fileInfo";
    public static final String VIEW_NAME = "csvFileDownloadView";

    @Autowired
    private ResourceLoader resourceLoader;

    @SuppressWarnings("resource")
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileDownloadAttributes attributes = (FileDownloadAttributes) model.get(DOWNLOAD_FILE_INFO_KEY);

        // レスポンスヘッダを設定します
        response.setContentType("text/csv");
        // 日本語のファイル名に対応するため、Content-DispositionはRFC6266の形式に沿って設定する
        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(attributes.getDownloadFileName(), StandardCharsets.UTF_8)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        // ダウンロード対象のファイルを読込み、レスポンスボディに書込む
        try (InputStream in = resourceLoader.getResource(attributes.getTargetFilePath()).getInputStream()) {
            in.transferTo(response.getOutputStream());
        }
    }
}
