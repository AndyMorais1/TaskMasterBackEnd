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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskListPermissionIntegrationTests {

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

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isCreated());

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
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseContent);
        return node.get("token").asText();
    }
    
    private Long getUserId(String token) throws Exception {
         // Since we don't have a direct /me endpoint, we can infer or use search by username if we knew it.
         // But better, let's use the token to get the user ID via a protected endpoint or just return the ID from creation if possible.
         // Wait, the create endpoint returns the user object including ID.
         return null; 
    }
    
    private Long createUserAndGetId(String username, String email) throws Exception {
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
        
        String responseContent = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseContent);
        return node.get("id").asLong();
    }
    
    private String login(String username) throws Exception {
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
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseContent);
        return node.get("token").asText();
    }

    @Test
    void testShareAndPermissions() throws Exception {
        String user1Name = "user1_" + UUID.randomUUID();
        String user1Email = "user1_" + UUID.randomUUID() + "@example.com";
        Long user1Id = createUserAndGetId(user1Name, user1Email);
        String user1Token = login(user1Name);

        String user2Name = "user2_" + UUID.randomUUID();
        String user2Email = "user2_" + UUID.randomUUID() + "@example.com";
        Long user2Id = createUserAndGetId(user2Name, user2Email);
        String user2Token = login(user2Name);

        // User 1 creates a list
        String createListBody = """
                {
                  "name": "My List",
                  "description": "Test List"
                }
                """;
        
        MvcResult listResult = mockMvc.perform(post("/api/v1/task-lists?userid=" + user1Id)
                        .header("Authorization", "Bearer " + user1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createListBody))
                .andExpect(status().isCreated())
                .andReturn();
        
        Long listId = objectMapper.readTree(listResult.getResponse().getContentAsString()).get("id").asLong();

        // User 2 tries to access the list (should fail)
        mockMvc.perform(get("/api/v1/task-lists/" + listId + "?userId=" + user2Id)
                        .header("Authorization", "Bearer " + user2Token))
                .andExpect(status().isForbidden());

        // User 1 shares list with User 2 (READ)
        String shareBody = """
                {
                  "email": "%s",
                  "permission": "READ"
                }
                """.formatted(user2Email);
        
        mockMvc.perform(post("/api/v1/task-lists/" + listId + "/share?ownerId=" + user1Id)
                        .header("Authorization", "Bearer " + user1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shareBody))
                .andExpect(status().isOk());

        // User 2 tries to access the list (should succeed)
        mockMvc.perform(get("/api/v1/task-lists/" + listId + "?userId=" + user2Id)
                        .header("Authorization", "Bearer " + user2Token))
                .andExpect(status().isOk());
                
        // User 2 tries to add a task (should fail)
        String createTaskBody = """
                {
                  "name": "Task from User 2",
                  "priority": "PRIORITY_MEDIUM",
                  "listId": %d
                }
                """.formatted(listId);
        
        mockMvc.perform(post("/api/v1/tasks?userId=" + user2Id)
                        .header("Authorization", "Bearer " + user2Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTaskBody))
                .andExpect(status().isForbidden());
                
        // User 1 updates permission to WRITE
        String shareWriteBody = """
                {
                  "email": "%s",
                  "permission": "WRITE"
                }
                """.formatted(user2Email);
        
        mockMvc.perform(post("/api/v1/task-lists/" + listId + "/share?ownerId=" + user1Id)
                        .header("Authorization", "Bearer " + user1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shareWriteBody))
                .andExpect(status().isOk());
                
        // User 2 tries to add a task (should succeed)
        mockMvc.perform(post("/api/v1/tasks?userId=" + user2Id)
                        .header("Authorization", "Bearer " + user2Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTaskBody))
                .andExpect(status().isCreated());
    }
}
