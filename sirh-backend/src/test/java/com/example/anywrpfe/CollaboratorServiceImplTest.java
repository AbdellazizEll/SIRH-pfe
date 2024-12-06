package com.example.anywrpfe;

import com.example.anywrpfe.dto.CollaboratorDTO;
import com.example.anywrpfe.entities.Collaborateur;
import com.example.anywrpfe.repositories.CollaborateurRepository;
import com.example.anywrpfe.services.implementations.CollaborateurServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollaboratorServiceImplTest {
    @InjectMocks
    private CollaborateurServiceImpl collaborateurService;

    @Mock
    private CollaborateurRepository collaborateurRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById_Found() {
        // Arrange
        Long collaboratorId = 1L;
        Collaborateur collaborateur = new Collaborateur();
        collaborateur.setId(collaboratorId);
        collaborateur.setFirstname("Jane");
        collaborateur.setLastname("Doe");
        collaborateur.setEmail("jane.doe@example.com");
        collaborateur.setPhone("0987654321");
        collaborateur.setAge(28);
        // Set other necessary fields if needed

        CollaboratorDTO expectedDTO = CollaboratorDTO.builder()
                .id(collaboratorId)
                .firstname("Jane")
                .lastname("Doe")
                .email("jane.doe@example.com")
                .phone("0987654321")
                .age(28)
                .build();

        when(collaborateurRepository.findById(collaboratorId)).thenReturn(Optional.of(collaborateur));

        // Act
        Optional<CollaboratorDTO> result = collaborateurService.getById(collaboratorId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedDTO.getId(), result.get().getId());
        assertEquals(expectedDTO.getFirstname(), result.get().getFirstname());
        assertEquals(expectedDTO.getLastname(), result.get().getLastname());
        assertEquals(expectedDTO.getEmail(), result.get().getEmail());
        assertEquals(expectedDTO.getPhone(), result.get().getPhone());
        assertEquals(expectedDTO.getAge(), result.get().getAge());

        // Verify interactions
        verify(collaborateurRepository, times(1)).findById(collaboratorId);
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        Long collaboratorId = 1L;

        when(collaborateurRepository.findById(collaboratorId)).thenReturn(Optional.empty());

        // Act
        Optional<CollaboratorDTO> result = collaborateurService.getById(collaboratorId);

        // Assert
        assertFalse(result.isPresent());

        // Verify interactions
        verify(collaborateurRepository, times(1)).findById(collaboratorId);
    }
}
