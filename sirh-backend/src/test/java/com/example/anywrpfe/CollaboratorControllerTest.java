package com.example.anywrpfe;

import com.example.anywrpfe.controller.CollaboratorController;
import com.example.anywrpfe.dto.CollaboratorDTO;

import com.example.anywrpfe.services.CollaborateurService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Annotation to initialize only the web layer
@WebMvcTest(CollaboratorController.class)
public class CollaboratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CollaborateurService collaborateurService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private CollaboratorController collaboratorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
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
