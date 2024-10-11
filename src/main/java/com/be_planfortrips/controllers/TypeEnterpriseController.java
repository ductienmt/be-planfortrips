package com.be_planfortrips.controllers;

import com.be_planfortrips.entity.TypeEnterprise;
import com.be_planfortrips.services.interfaces.ITypeEnterpriseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/type-enterprises")
public class TypeEnterpriseController {

    private final ITypeEnterpriseService typeEnterpriseService;

    // Lấy tất cả loại doanh nghiệp
    @GetMapping
    public ResponseEntity<List<TypeEnterprise>> getAllTypeEnterprises() {
        List<TypeEnterprise> typeEnterprises = typeEnterpriseService.getAllTypeEnterprises();
        return ResponseEntity.ok(typeEnterprises);
    }

    // Lấy loại doanh nghiệp theo ID
    @GetMapping("/{id}")
    public ResponseEntity<TypeEnterprise> getTypeEnterpriseById(@PathVariable Long id) {
        TypeEnterprise typeEnterprise = typeEnterpriseService.getTypeEnterpriseById(id);
        return ResponseEntity.ok(typeEnterprise);
    }

    // Tạo loại doanh nghiệp mới
    @PostMapping
    public ResponseEntity<TypeEnterprise> createTypeEnterprise(@RequestBody TypeEnterprise typeEnterprise) {
        TypeEnterprise createdTypeEnterprise = typeEnterpriseService.createTypeEnterprise(typeEnterprise);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTypeEnterprise);
    }

    // Cập nhật loại doanh nghiệp
    @PutMapping("/{id}")
    public ResponseEntity<TypeEnterprise> updateTypeEnterprise(@PathVariable Long id, @RequestBody TypeEnterprise typeEnterprise) {
        TypeEnterprise updatedTypeEnterprise = typeEnterpriseService.updateTypeEnterprise(id, typeEnterprise);
        return ResponseEntity.ok(updatedTypeEnterprise);
    }

    // Xóa loại doanh nghiệp
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeEnterprise(@PathVariable Long id) {
        typeEnterpriseService.deleteTypeEnterpriseById(id);
        return ResponseEntity.noContent().build();
    }
}
