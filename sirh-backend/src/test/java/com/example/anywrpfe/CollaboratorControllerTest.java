package com.example.anywrpfe;

import com.example.anywrpfe.controller.CollaboratorController;
import com.example.anywrpfe.dto.CollaboratorDTO;
import com.example.anywrpfe.repositories.RoleRepository;
import com.example.anywrpfe.services.CollaborateurService;
import com.example.anywrpfe.services.RoleService;
import com.example.anywrpfe.config.JwtService;
import com.example.anywrpfe.repositories.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CollaboratorController.class,
        excludeAutoConfiguration = {
                HibernateJpaAutoConfiguration.class
        }
)
public class CollaboratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollaborateurService collaborateurService;

    @MockBean
    private JwtService jwtService; // Mock JwtService

    @MockBean
    private TokenRepository tokenRepository; // Mock TokenRepository

    @MockBean
    private UserDetailsService userDetailsService; // Mock UserDetailsService

    @MockBean
    private RoleRepository roleRepository; // Mock RoleRepository

    @MockBean
    private RoleService roleService; // Mock RoleService

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetCollaboratorById_Found() throws Exception {
        // Arrange
        Long collaboratorId = 1L;
        CollaboratorDTO collaboratorDTO = CollaboratorDTO.builder()
                .id(collaboratorId)
                .firstname("Jane")
                .lastname("Doe")
                .email("jane.doe@example.com")
                .phone("0987654321")
                .age(28)
                .build();

        when(collaborateurService.getById(collaboratorId)).thenReturn(Optional.of(collaboratorDTO));

        // Act & Assert
        mockMvc.perform(get("/findByCollaborator/{id}", collaboratorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(collaboratorId))
                .andExpect(jsonPath("$.firstname").value("Jane"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("0987654321"))
                .andExpect(jsonPath("$.age").value(28));

        // Verify that the service method was called once with the correct ID
        verify(collaborateurService, times(1)).getById(collaboratorId);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"RH"})
    void testGetCollaboratorById_NotFound() throws Exception {
        // Arrange
        Long collaboratorId = 1L;

        when(collaborateurService.getById(collaboratorId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/findByCollaborator/{id}", collaboratorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify that the service method was called once with the correct ID
        verify(collaborateurService, times(1)).getById(collaboratorId);
    }
}
