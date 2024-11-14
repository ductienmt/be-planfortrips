package com.be_planfortrips.controllers;

import com.be_planfortrips.entity.TypeEnterprise;
import com.be_planfortrips.services.interfaces.ITypeEnterpriseService;
import jakarta.validation.Valid;
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
    @GetMapping("/all")
    public ResponseEntity<List<TypeEnterprise>> getAllTypeEnterprises() {
        List<TypeEnterprise> typeEnterprises = typeEnterpriseService.getAllTypeEnterprises();
        return ResponseEntity.ok(typeEnterprises);
    }

    // Lấy loại doanh nghiệp theo ID
    @GetMapping("getById/{id}")
    public ResponseEntity<TypeEnterprise> getTypeEnterpriseById(@PathVariable Long id) {
        TypeEnterprise typeEnterprise = typeEnterpriseService.getTypeEnterpriseById(id);
        return ResponseEntity.ok(typeEnterprise);
    }

    // Tạo loại doanh nghiệp mới
    @PostMapping("/create")
    public ResponseEntity<TypeEnterprise> createTypeEnterprise(@Valid @RequestBody TypeEnterprise typeEnterprise) {
        TypeEnterprise createdTypeEnterprise = typeEnterpriseService.createTypeEnterprise(typeEnterprise);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTypeEnterprise);
    }

    // Cập nhật loại doanh nghiệp
    @PutMapping("update/{id}")
    public ResponseEntity<TypeEnterprise> updateTypeEnterprise(@PathVariable Long id,
                                                              @Valid @RequestBody TypeEnterprise typeEnterprise) {
        TypeEnterprise updatedTypeEnterprise = typeEnterpriseService.updateTypeEnterprise(id, typeEnterprise);
        return ResponseEntity.ok(updatedTypeEnterprise);
    }

    // Xóa loại doanh nghiệp
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteTypeEnterprise(@PathVariable Long id) {
        typeEnterpriseService.deleteTypeEnterpriseById(id);
        return ResponseEntity.noContent().build();
    }
}
