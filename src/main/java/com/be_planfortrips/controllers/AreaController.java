package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.AreaDto;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.AreaResponse;
import com.be_planfortrips.services.interfaces.IAreaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("${api.prefix}/areas")
public class AreaController {
    @Autowired
    private IAreaService areaService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllAreas() {
        try {
            List<AreaResponse> areas = this.areaService.getAll();
            if (areas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.<Void>builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("Không có khu vực.")
                                .build()
                );
            }

            return ResponseEntity.ok(
                    ApiResponse.<List<AreaResponse>>builder()
                            .code(HttpStatus.OK.value())
                            .data(areas)
                            .message("Lấy danh sách khu vực thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Lấy danh sách khu vực thất bại.")
                            .build()
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createArea(@Valid @RequestBody AreaDto areaDto) {
        try {
            AreaResponse areaResponse = this.areaService.createArea(areaDto);
            return ResponseEntity.ok(
                    ApiResponse.<AreaResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(areaResponse)
                            .message("Tạo khu vực thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Tạo khu vực thất bại.")
                            .build()
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateArea(@RequestParam("id") String id,@Valid @RequestBody AreaDto areaDto) {
        try {
            AreaResponse areaResponse = this.areaService.updateArea(id, areaDto);
            return ResponseEntity.ok(
                    ApiResponse.<AreaResponse>builder()
                            .code(HttpStatus.OK.value())
                            .data(areaResponse)
                            .message("Cập nhật khu vực thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Cập nhật khu vực thất bại.")
                            .build()
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteArea(@RequestParam("id") String id) {
        try {
            this.areaService.deleteArea(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Xóa khu vực thành công.")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Xóa khu vực thất bại.")
                            .build()
            );
        }
    }

}
