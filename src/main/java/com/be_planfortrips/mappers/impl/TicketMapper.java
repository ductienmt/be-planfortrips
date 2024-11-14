package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.TicketDTO;
import com.be_planfortrips.dto.TicketDTO;
import com.be_planfortrips.dto.response.TicketResponse;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.Ticket;
import com.be_planfortrips.entity.Ticket;
import com.be_planfortrips.entity.Vehicle;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TicketMapper implements MapperInterface<TicketResponse, Ticket, TicketDTO> {
    ModelMapper modelMapper;
    @Override
    public Ticket toEntity(TicketDTO ticketDTO) {
        TypeMap<TicketDTO, Ticket> typeMap = modelMapper.getTypeMap(TicketDTO.class, Ticket.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(TicketDTO.class, Ticket.class);
            typeMap.addMappings(mapper -> mapper.skip(Ticket::setId));
            typeMap.addMappings(mapper -> mapper.map(
                    src -> {
                        String statusString = src.getStatus();
                        return statusString != null ? Status.valueOf(statusString.toUpperCase()) : null;
                    },
                    Ticket::setStatus));
        }
        Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
        return ticket;    }

    @Override
    public TicketResponse toResponse(Ticket ticket) {
        modelMapper.typeMap(Ticket.class, TicketResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getId(), TicketResponse::setTicketId);
//            mapper.map(src -> src.getStatus().toString().toUpperCase(), TicketResponse::setStatus);
            mapper.map(src -> src.getUser().getId(), TicketResponse::setUser_id);
        });
        return modelMapper.map(ticket,TicketResponse.class);
    }

    @Override
    public void updateEntityFromDto(TicketDTO ticketDTO, Ticket ticket) {

    }
}
