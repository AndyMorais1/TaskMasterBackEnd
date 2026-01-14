package com.taskmaster.taskmaster;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmaster.taskmaster.model.Permission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskListShareManagementIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String createUser(String username, String email) throws Exception {
        String registerBody = """
                {
                  "username": "%s",
                  "password": "password123",
                  "email": "%s"
                }
                """.formatted(username, email);

        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isCreated())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token") != null ? 
               objectMapper.readTree(response).get("token").asText() : null; // Register doesn't return token usually, login does.
    }
    
    private String loginUser(String username) throws Exception {
        String loginBody = """
                {
                  "username": "%s",
                  "password": "password123"
                }
                """.formatted(username);

        MvcResult result = mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn();
        
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    private Long getUserId(String token) throws Exception {
        // Assuming we can get user info or we just registered and know the ID from response if modified
        // Or we can fetch "me" or similar.
        // Let's use the register response which returns the user DTO including ID.
        return null; // Helper method limitation.
    }
    
    // Better helper that returns ID and Token
    private UserInfo createAndLoginUser(String prefix) throws Exception {
        String username = prefix + UUID.randomUUID();
        String email = prefix + UUID.randomUUID() + "@example.com";
        
        MvcResult registerResult = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "password": "password123",
                                  "email": "%s"
                                }
                                """.formatted(username, email)))
                .andExpect(status().isCreated())
                .andReturn();
        
        Long id = objectMapper.readTree(registerResult.getResponse().getContentAsString()).get("id").asLong();
        
        String token = loginUser(username);
        return new UserInfo(id, username, email, token);
    }
    
    record UserInfo(Long id, String username, String email, String token) {}

    @Test
    void testShareUnshareAndCollaborators() throws Exception {
        UserInfo owner = createAndLoginUser("owner");
        UserInfo collaborator = createAndLoginUser("collab");

        // 1. Create List
        String createListBody = """
                {
                  "name": "My Shared List"
                }
                """;
        
        MvcResult listResult = mockMvc.perform(post("/api/v1/task-lists?userid=" + owner.id)
                        .header("Authorization", "Bearer " + owner.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createListBody))
                .andExpect(status().isCreated())
                .andReturn();
        
        Long listId = objectMapper.readTree(listResult.getResponse().getContentAsString()).get("id").asLong();

        // 2. Share List
        String shareBody = """
                {
                  "email": "%s",
                  "permission": "READ"
                }
                """.formatted(collaborator.email);

        mockMvc.perform(post("/api/v1/task-lists/" + listId + "/share?ownerId=" + owner.id)
                        .header("Authorization", "Bearer " + owner.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shareBody))
                .andExpect(status().isOk());

        // 3. Verify Collaborators
        mockMvc.perform(get("/api/v1/task-lists/" + listId + "/collaborators?userId=" + owner.id)
                        .header("Authorization", "Bearer " + owner.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(collaborator.email))
                .andExpect(jsonPath("$[0].permission").value("READ"));

        // 4. Unshare List
        mockMvc.perform(delete("/api/v1/task-lists/" + listId + "/share?ownerId=" + owner.id + "&email=" + collaborator.email)
                        .header("Authorization", "Bearer " + owner.token))
                .andExpect(status().isNoContent());

        // 5. Verify Collaborators Empty
        mockMvc.perform(get("/api/v1/task-lists/" + listId + "/collaborators?userId=" + owner.id)
                        .header("Authorization", "Bearer " + owner.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
