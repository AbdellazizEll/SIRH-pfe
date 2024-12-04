package com.example.anywrpfe;


import com.example.anywrpfe.controller.CollaboratorController;
import com.example.anywrpfe.dto.LightCollaboratorDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.services.CollaborateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@RequiredArgsConstructor
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(CollaboratorControllerTest.class)
public class CollaboratorControllerTest {

    private  MockMvc mockMvc;


    @Mock
    private final CollaborateurService collaborateurService;

    @InjectMocks
    private CollaboratorController collaboratorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(collaboratorController).build();
    }

    @Test
    void testGetUsers() throws Exception {
        // Arrange
        Collaborateur collaboratorDTO = new Collaborateur();
        collaboratorDTO.setId(1L);
        collaboratorDTO.setFirstname("John");
        collaboratorDTO.setLastname("Doe");

        List<LightCollaboratorDTO> collaborators = List.of(LightCollaboratorDTO.fromEntity(collaboratorDTO));

        when(collaborateurService.ListCollaborators(anyString(), anyInt(), anyInt()))
                .thenReturn((Page<LightCollaboratorDTO>) collaborators);

        // Act & Assert
        mockMvc.perform(get("/getAllCollaborators")
                        .param("name", "")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page[0].firstname").value("John"))
                .andExpect(jsonPath("$.data.page[0].lastname").value("Doe"));

        verify(collaborateurService, times(1)).ListCollaborators(anyString(), anyInt(), anyInt());
    }
}
