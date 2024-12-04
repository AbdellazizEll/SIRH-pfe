package com.example.anywrpfe;

import com.example.anywrpfe.controller.CollaboratorController;
import com.example.anywrpfe.dto.LightCollaboratorDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.services.CollaborateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollaboratorController.class)
public class CollaboratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollaborateurService collaborateurService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void testGetUsers() throws Exception {
        // Arrange
        Collaborateur collaboratorDTO = new Collaborateur();
        collaboratorDTO.setId(1L);
        collaboratorDTO.setFirstname("John");
        collaboratorDTO.setLastname("Doe");

        List<LightCollaboratorDTO> collaborators = List.of(LightCollaboratorDTO.fromEntity(collaboratorDTO));

        when(collaborateurService.ListCollaborators(anyString(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(collaborators));

        // Act & Assert
        mockMvc.perform(get("/getAllCollaborators")
                        .param("name", "")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page.content[0].firstname").value("John"))
                .andExpect(jsonPath("$.data.page.content[0].lastname").value("Doe"));

        verify(collaborateurService, times(1)).ListCollaborators(anyString(), anyInt(), anyInt());
    }
}
