package com.be_planfortrips.TypeEnterpriseDetailController;

import com.be_planfortrips.controllers.TypeEnterpriseDetailController;
import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.TypeEnterpriseDetailResponse;
import com.be_planfortrips.services.interfaces.ITypeEnterpriseDetailService;
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
public class TypeEnterpriseDetailControllerTest {

    @Mock
    private ITypeEnterpriseDetailService typeEnterpriseDetailService;

    @InjectMocks
    private TypeEnterpriseDetailController typeEnterpriseDetailController;

    private TypeEnterpriseDetailDto typeEnterpriseDetailDto;
    private TypeEnterpriseDetailResponse typeEnterpriseDetailResponse;

    @BeforeEach
    void setUp() {
        typeEnterpriseDetailDto = new TypeEnterpriseDetailDto("Transport", "Transport services", 1L);
        typeEnterpriseDetailResponse = new TypeEnterpriseDetailResponse(1L, "Transport", "Transport services", 1L);
    }

    @Test
    void testGetAllTypeEnterpriseDetails() {
        List<TypeEnterpriseDetailResponse> detailsList = Arrays.asList(typeEnterpriseDetailResponse);
        when(typeEnterpriseDetailService.getAllTypeEnterpriseDetails()).thenReturn(detailsList);

        ResponseEntity<List<TypeEnterpriseDetailResponse>> response = typeEnterpriseDetailController.getAllTypeEnterpriseDetails();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(detailsList, response.getBody());
        verify(typeEnterpriseDetailService).getAllTypeEnterpriseDetails();
    }

    @Test
    void testGetTypeEnterpriseDetailById() {
        Long id = 1L;
        when(typeEnterpriseDetailService.getTypeEnterpriseDetailById(id)).thenReturn(typeEnterpriseDetailResponse);

        ResponseEntity<TypeEnterpriseDetailResponse> response = typeEnterpriseDetailController.getTypeEnterpriseDetailById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeEnterpriseDetailResponse, response.getBody());
        verify(typeEnterpriseDetailService).getTypeEnterpriseDetailById(id);
    }

    @Test
    void testCreateTypeEnterpriseDetail() {
        when(typeEnterpriseDetailService.createTypeEnterpriseDetail(typeEnterpriseDetailDto)).thenReturn(typeEnterpriseDetailResponse);

        ResponseEntity<TypeEnterpriseDetailResponse> response = typeEnterpriseDetailController.createTypeEnterpriseDetail(typeEnterpriseDetailDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(typeEnterpriseDetailResponse, response.getBody());
        verify(typeEnterpriseDetailService).createTypeEnterpriseDetail(typeEnterpriseDetailDto);
    }

    @Test
    void testUpdateTypeEnterpriseDetail() {
        Long id = 1L;
        when(typeEnterpriseDetailService.updateTypeEnterpriseDetail(id, typeEnterpriseDetailDto)).thenReturn(typeEnterpriseDetailResponse);

        ResponseEntity<TypeEnterpriseDetailResponse> response = typeEnterpriseDetailController.updateTypeEnterpriseDetail(id, typeEnterpriseDetailDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeEnterpriseDetailResponse, response.getBody());
        verify(typeEnterpriseDetailService).updateTypeEnterpriseDetail(id, typeEnterpriseDetailDto);
    }

    @Test
    void testDeleteTypeEnterpriseDetail() {
        Long id = 1L;

        ResponseEntity<Void> response = typeEnterpriseDetailController.deleteTypeEnterpriseDetail(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(typeEnterpriseDetailService).deleteTypeEnterpriseDetail(id);
    }
}