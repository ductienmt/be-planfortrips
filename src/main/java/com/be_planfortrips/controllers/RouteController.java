package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.RouteDTO;
import com.be_planfortrips.dto.response.CarResponse;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.RouteResponse;
import com.be_planfortrips.services.interfaces.IRouteService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/routes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteController {
    IRouteService iRouteService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoute(@RequestBody @Valid RouteDTO RouteDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            RouteResponse RouteResponse = iRouteService.createRoute(RouteDTO);
            return ResponseEntity.ok(RouteResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{code}")
    public ResponseEntity<?> updateRoute(@PathVariable String code, @RequestBody @Valid RouteDTO RouteDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            RouteResponse RouteResponse = iRouteService.updateRoute(code, RouteDTO);
            return ResponseEntity.ok(RouteResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("all")
    public ResponseEntity<?> getCarCompanies(@RequestParam int page,
                                             @RequestParam int limit,
                                             @RequestParam(defaultValue = "") String oriName,
                                             @RequestParam(defaultValue = "") String desName) {
        try {
            PageRequest request = PageRequest.of(page, limit, Sort.by("id").ascending());
            int totalPage = 0;
            Page<RouteResponse> RouteResponses = iRouteService.getRoutes(request, oriName, desName);
            totalPage = RouteResponses.getTotalPages();
            TListResponse<RouteResponse> listResponse = new TListResponse<>();
            listResponse.setListResponse(RouteResponses.toList());
            listResponse.setTotalPage(totalPage);
            return ResponseEntity.ok(listResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCarCompanyById(@PathVariable String id) throws Exception {
        iRouteService.deleteRouteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getCarCompanyById(@PathVariable String id) {
        try {
            RouteResponse response = iRouteService.getByRouteId(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getCityByRouteId")
    public ResponseEntity<?> getCityByRouteId(@RequestParam String routeId) {
        try {
            return ResponseEntity.ok(iRouteService.getCityByRouteId(routeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
