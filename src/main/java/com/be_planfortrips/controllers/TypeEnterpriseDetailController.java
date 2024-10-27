package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TypeEnterpriseDetailDto;
import com.be_planfortrips.dto.response.TypeEnterpriseDetailResponse;
import com.be_planfortrips.services.interfaces.ITypeEnterpriseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/type-enterprise-details")
public class TypeEnterpriseDetailController {

    private final ITypeEnterpriseDetailService typeEnterpriseDetailService;

    @GetMapping("/all")
    public ResponseEntity<List<TypeEnterpriseDetailResponse>> getAllTypeEnterpriseDetails() {
        List<TypeEnterpriseDetailResponse> details = typeEnterpriseDetailService.getAllTypeEnterpriseDetails();
        return ResponseEntity.ok(details);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<TypeEnterpriseDetailResponse> getTypeEnterpriseDetailById(@PathVariable Long id) {
        TypeEnterpriseDetailResponse response = typeEnterpriseDetailService.getTypeEnterpriseDetailById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<TypeEnterpriseDetailResponse> createTypeEnterpriseDetail(@RequestBody TypeEnterpriseDetailDto dto) {
        TypeEnterpriseDetailResponse response = typeEnterpriseDetailService.createTypeEnterpriseDetail(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<TypeEnterpriseDetailResponse> updateTypeEnterpriseDetail(
            @PathVariable Long id,
            @RequestBody TypeEnterpriseDetailDto dto
    ) {
        TypeEnterpriseDetailResponse response = typeEnterpriseDetailService.updateTypeEnterpriseDetail(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteTypeEnterpriseDetail(@PathVariable Long id) {
        typeEnterpriseDetailService.deleteTypeEnterpriseDetail(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
