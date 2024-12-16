package com.be_planfortrips.TypeEnterpriseController;

import com.be_planfortrips.controllers.TypeEnterpriseController;
import com.be_planfortrips.entity.TypeEnterprise;
import com.be_planfortrips.services.interfaces.ITypeEnterpriseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeEnterpriseControllerTest {

    @Mock
    private ITypeEnterpriseService typeEnterpriseService;

    @InjectMocks
    private TypeEnterpriseController typeEnterpriseController;

    private TypeEnterprise typeEnterprise;

    @BeforeEach
    void setUp() {
        typeEnterprise = new TypeEnterprise();
        typeEnterprise.setId(1L);
        typeEnterprise.setNameType("Transport");
        typeEnterprise.setDescription("Transport services");
    }

    @Test
    void testGetAllTypeEnterprises() {
        List<TypeEnterprise> typeEnterpriseList = Arrays.asList(typeEnterprise);
        when(typeEnterpriseService.getAllTypeEnterprises()).thenReturn(typeEnterpriseList);

        ResponseEntity<List<TypeEnterprise>> response = typeEnterpriseController.getAllTypeEnterprises();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeEnterpriseList, response.getBody());
        verify(typeEnterpriseService).getAllTypeEnterprises();
    }

    @Test
    void testGetTypeEnterpriseById() {
        Long id = 1L;
        when(typeEnterpriseService.getTypeEnterpriseById(id)).thenReturn(typeEnterprise);

        ResponseEntity<TypeEnterprise> response = typeEnterpriseController.getTypeEnterpriseById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeEnterprise, response.getBody());
        verify(typeEnterpriseService).getTypeEnterpriseById(id);
    }

    @Test
    void testCreateTypeEnterprise() {
        when(typeEnterpriseService.createTypeEnterprise(typeEnterprise)).thenReturn(typeEnterprise);

        ResponseEntity<TypeEnterprise> response = typeEnterpriseController.createTypeEnterprise(typeEnterprise);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(typeEnterprise, response.getBody());
        verify(typeEnterpriseService).createTypeEnterprise(typeEnterprise);
    }

    @Test
    void testUpdateTypeEnterprise() {
        Long id = 1L;
        when(typeEnterpriseService.updateTypeEnterprise(id, typeEnterprise)).thenReturn(typeEnterprise);

        ResponseEntity<TypeEnterprise> response = typeEnterpriseController.updateTypeEnterprise(id, typeEnterprise);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeEnterprise, response.getBody());
        verify(typeEnterpriseService).updateTypeEnterprise(id, typeEnterprise);
    }

    @Test
    void testDeleteTypeEnterprise() {
        Long id = 1L;

        ResponseEntity<Void> response = typeEnterpriseController.deleteTypeEnterprise(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(typeEnterpriseService).deleteTypeEnterpriseById(id);
    }
}