package com.example.web.common.mvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.web.common.mvc.CsvFileDownloadViewTest.CsvFileDownloadViewTestController;
import com.example.web.test.WebTest;

/**
 * CsvFileDownloadViewのテスト。
 * 
 * @author sample
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@WebTest
@Import(CsvFileDownloadViewTestController.class)
class CsvFileDownloadViewTest {

    private static final String TARGET_FILE_BASE_PATH = "com/example/web/common/mvc/CsvFileDownloadViewTest/";

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void test() throws Exception {
        byte[] expectedContent;
        try (InputStream in = new ClassPathResource(TARGET_FILE_BASE_PATH + "test.csv").getInputStream()) {
            expectedContent = in.readAllBytes();
        }
    mvc.perform(get("/test1"))
        .andExpectAll(
            status().isOk(),
            content().contentType("text/csv"),
            content().bytes(expectedContent),
            header()
                .string(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"=?UTF-8?Q?download.csv?=\"; filename*=UTF-8''download.csv"));
    }

    @Test
    @WithMockUser
    void testFileNotFound() {
        // ファイルが見つからなかった場合は例外がスローされる。
        // この例外はSpring Web MVCによってハンドリングされてエラー画面が返される。
        assertThrows(FileNotFoundException.class, () -> mvc.perform(get("/test2")));
    }

    @TestComponent
    @Controller
    public static class CsvFileDownloadViewTestController {

        @GetMapping("/test1")
        public String test1(Model model) {
            String targetFilePath = "classpath:" + TARGET_FILE_BASE_PATH + "test.csv";
            String downloadFileName = "download.csv";
            model
                    .addAttribute(CsvFileDownloadView.DOWNLOAD_FILE_INFO_KEY,
                            new FileDownloadAttributes(targetFilePath, downloadFileName));
            return CsvFileDownloadView.VIEW_NAME;
        }

        @GetMapping("/test2")
        public String test2(Model model) {
            String targetFilePath = "classpath:" + TARGET_FILE_BASE_PATH + "not_exists.csv";
            String downloadFileName = "download.csv";
            model
                    .addAttribute(CsvFileDownloadView.DOWNLOAD_FILE_INFO_KEY,
                            new FileDownloadAttributes(targetFilePath, downloadFileName));
            return CsvFileDownloadView.VIEW_NAME;
        }
    }
}
