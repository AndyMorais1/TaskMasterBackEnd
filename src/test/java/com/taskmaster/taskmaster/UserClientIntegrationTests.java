package com.taskmaster.taskmaster;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserClientIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerLoginAndAccessProtectedEndpoint() throws Exception {
        String email = "test" + UUID.randomUUID() + "@example.com";
        String username = "testuser" + UUID.randomUUID();

        String registerBody = """
                {
                  "username": "%s",
                  "password": "password123",
                  "email": "%s"
                }
                """.formatted(username, email);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isCreated())
                .andReturn();
        
        String registerResponse = registerResult.getResponse().getContentAsString();
        Long userId = objectMapper.readTree(registerResponse).get("id").asLong();

        String loginBody = """
                {
                  "username": "%s",
                  "password": "password123"
                }
                """.formatted(username);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseContent);
        String token = node.get("token").asText();

        mockMvc.perform(get("/api/v1/task-lists?userId=" + userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}

