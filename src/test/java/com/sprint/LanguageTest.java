package com.sprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LanguageTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test01_getAll_success() throws Exception {
        mockMvc.perform(get("/languages")
                .param("projection", "languageDetail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].languageId").exists())
                .andExpect(jsonPath("$.content[0].name").exists());
    }

    @Test
    public void test02_getAll_unprojected() throws Exception {
        mockMvc.perform(get("/languages"))
                .andExpect(status().isOk());
    }

    @Test
    public void test05_post_validLanguage() throws Exception {
        mockMvc.perform(post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Esperanto",
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void test06_post_blankLanguageName() throws Exception {
        mockMvc.perform(post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "",
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test07_post_missingLanguageName() throws Exception {
        mockMvc.perform(post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test08_post_missingLastUpdate() throws Exception {
        mockMvc.perform(post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Hindi"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test09_post_languageNameTooLong() throws Exception {
        mockMvc.perform(post("/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "ThisLanguageNameIsWayTooLongForTheDatabase",
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test10_put_success() throws Exception {
        mockMvc.perform(put("/languages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "English",
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test11_put_notFound() throws Exception {
        mockMvc.perform(put("/languages/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Unknown",
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test12_put_blankLanguageName() throws Exception {
        mockMvc.perform(put("/languages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "",
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test13_put_missingLastUpdate() throws Exception {
        mockMvc.perform(put("/languages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Spanish"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test14_put_languageNameTooLong() throws Exception {
        mockMvc.perform(put("/languages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "ThisLanguageNameIsWayTooLongForTheDatabase",
                          "lastUpdate": "2026-04-29T14:41:11.015Z"
                        }
                        """))
                .andExpect(status().is4xxClientError());
    }
    
}
