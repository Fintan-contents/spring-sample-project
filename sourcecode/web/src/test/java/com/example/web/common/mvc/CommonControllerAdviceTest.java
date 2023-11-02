package com.example.web.common.mvc;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.View;

import com.example.web.common.mvc.CommonControllerAdviceTest.TestController;
import com.example.web.test.WebTest;

@SpringBootTest
@AutoConfigureMockMvc
@WebTest
@Import(TestController.class)
class CommonControllerAdviceTest {

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void testNoParam() throws Exception {
        mvc.perform(post("/test")
                .with(csrf()))
                .andExpect(model().attribute("foobar", nullValue()));
    }

    @Test
    @WithMockUser
    void testEmptyToNull() throws Exception {
        mvc.perform(post("/test")
                .param("foobar", "")
                .with(csrf()))
                .andExpect(model().attribute("foobar", nullValue()));
    }

    @Test
    @WithMockUser
    void testTrimming() throws Exception {
        mvc.perform(post("/test")
                .param("foobar", "   ")
                .with(csrf()))
                .andExpect(model().attribute("foobar", nullValue()));
    }

    @TestComponent
    @Controller
    public static class TestController {

        @PostMapping("/test")
        public View post(TestForm form, Model model) {
            model.addAttribute("foobar", form.getFoobar());
            return (m, req, res) -> {
                System.out.println(m);
                res.setStatus(HttpServletResponse.SC_NO_CONTENT);
            };
        }
    }

    public static class TestForm {

        private String foobar;

        public String getFoobar() {
            return foobar;
        }

        public void setFoobar(String foobar) {
            this.foobar = foobar;
        }
    }
}
