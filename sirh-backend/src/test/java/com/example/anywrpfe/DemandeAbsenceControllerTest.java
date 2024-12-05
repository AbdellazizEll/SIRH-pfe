package com.example.anywrpfe;

import com.example.anywrpfe.controller.DemandeAbsenceController;
import com.example.anywrpfe.dto.AbsenceDTO;
import com.example.anywrpfe.dto.DemandeAbsenceDTO;
import com.example.anywrpfe.entities.Enum.Motif;
import com.example.anywrpfe.requests.DemandeAbsenceRequest;
import com.example.anywrpfe.services.AbsenceService;
import com.example.anywrpfe.services.DemandeAbsenceService;
import com.example.anywrpfe.config.JwtService; // Assurez-vous que JwtService est dans le bon package
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DemandeAbsenceController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive les filtres de sécurité si nécessaire
@TestPropertySource(properties = {
        "spring.mail.host=localhost",
        "spring.mail.port=25",
        "spring.mail.username=username",
        "spring.mail.password=password",
        "spring.mail.properties.mail.smtp.auth=false",
        "spring.mail.properties.mail.smtp.starttls.enable=false"
})
public class DemandeAbsenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DemandeAbsenceService demandeAbsenceService;

    @MockBean
    private AbsenceService absenceService;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private JwtService jwtService; // Mock de JwtService

    @Test
    void testGetAllMotifs() throws Exception {
        // Arrange
        AbsenceDTO absenceDTO = AbsenceDTO.builder()
                .absence(1L)
                .typeAbs(Motif.SICK_LEAVE)
                .build();

        List<AbsenceDTO> motifs = List.of(absenceDTO);

        when(demandeAbsenceService.getAllMotifs()).thenReturn(motifs);

        // Act & Assert
        mockMvc.perform(get("/demandeAbsence/getAllMotifs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].typeAbs").value("SICK_LEAVE"));

        verify(demandeAbsenceService, times(1)).getAllMotifs();
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testCreateDemandeAbsence() throws Exception {
        // Arrange
        DemandeAbsenceDTO savedAbsence = DemandeAbsenceDTO.builder()
                .id(1L)
                .comment("Need a leave")
                .build();

        when(demandeAbsenceService.AddDemandeAbs(any(DemandeAbsenceRequest.class), any())).thenReturn(savedAbsence);

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "test content".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/demandeAbsence/create")
                        .file(file)
                        .param("dateDebut", "2023-01-01")
                        .param("dateFin", "2023-01-05")
                        .param("comment", "Need a leave")
                        .param("motif", "SICK_LEAVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.data.id").value(1L))
                .andExpect(jsonPath("$.message").value("Demande d'absence a été créée avec succès"));

        verify(demandeAbsenceService, times(1)).AddDemandeAbs(any(DemandeAbsenceRequest.class), any());
    }
}
