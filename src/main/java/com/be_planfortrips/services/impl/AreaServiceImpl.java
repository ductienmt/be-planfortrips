package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.AreaDto;
import com.be_planfortrips.dto.response.AreaResponse;
import com.be_planfortrips.entity.Area;
import com.be_planfortrips.mappers.impl.AreaMapper;
import com.be_planfortrips.repositories.AreaRepository;
import com.be_planfortrips.services.interfaces.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AreaServiceImpl implements IAreaService {
    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public List<AreaResponse> getAll() {
        return this.areaRepository.findAll().stream().map(areaMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public AreaResponse createArea(AreaDto areaDto) {
        Area area = this.areaRepository.save(areaMapper.toEntity(areaDto));
        return this.areaMapper.toResponse(area);
    }

    @Override
    public AreaResponse updateArea(String id, AreaDto areaDto) {
        Area area = this.areaRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực với id: " + id));
        this.areaMapper.updateEntityFromDto(areaDto, area);
        this.areaRepository.saveAndFlush(area);
        return this.areaMapper.toResponse(area);
    }

    @Override
    public void deleteArea(String id) {
        Area area = this.areaRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực với id: " + id));
        area.setStatus(false);
        this.areaRepository.save(area);
    }

    @Override
    public void changeStatusArea(String id, Integer status) {
        Area area = this.areaRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực với id: " + id));
        if (status != 1 && status != 0) {
            throw new RuntimeException("Trạng thái không hợp lệ.");
        }
        if (status == 1) {
            area.setStatus(true);
        } else {
            area.setStatus(false);
        }
        this.areaRepository.save(area);
    }
}
