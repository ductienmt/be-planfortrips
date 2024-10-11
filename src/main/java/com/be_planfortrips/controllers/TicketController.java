package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TicketDTO;
import com.be_planfortrips.dto.response.TicketResponse;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.services.interfaces.ITicketService;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/tickets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TicketController {
    ITicketService iTicketService;
    @PostMapping()
    public ResponseEntity<?> createTicket(@RequestBody @Valid TicketDTO ticketDTO,
                                        @RequestParam String ids,
                                        BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<Integer> seatIds = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            ticketDTO.setSeatIds(seatIds);
            TicketResponse TicketResponse = iTicketService.createTicket(ticketDTO);
            return ResponseEntity.ok(TicketResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable Integer id,@RequestBody @Valid TicketDTO TicketDTO,
                                        BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            TicketResponse TicketResponse = iTicketService.updateTicket(id,TicketDTO);
            return ResponseEntity.ok(TicketResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping()
    public ResponseEntity<?> getCarCompanies(@RequestParam int page,
                                             @RequestParam int limit){
        try {
            PageRequest request = PageRequest.of(page,limit, Sort.by("id").ascending());
            int totalPage = 0;
            Page<TicketResponse> TicketResponses = iTicketService.getTickets(request);
            totalPage = TicketResponses.getTotalPages();
            TListResponse<TicketResponse> listResponse= new TListResponse<>();
            listResponse.setListResponse(TicketResponses.toList());
            listResponse.setTotalPage(totalPage);
            return ResponseEntity.ok(listResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarCompanyById(@PathVariable Integer id) throws Exception {
        iTicketService.deleteTicketById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarCompanyById(@PathVariable Integer id){
        try {
            TicketResponse response = iTicketService.getByTicketId(id);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
