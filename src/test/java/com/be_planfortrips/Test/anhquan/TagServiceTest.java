package com.be_planfortrips.Test.anhquan;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.be_planfortrips.dto.TagDTO;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.entity.Tag;
import com.be_planfortrips.mappers.impl.TagMapper;
import com.be_planfortrips.repositories.TagRepository;
import com.be_planfortrips.services.impl.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    private TagDTO tagDTO;
    private Tag tag;
    private TagResponse tagResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Giả lập dữ liệu mẫu
        tagDTO = new TagDTO();
        tagDTO.setName("Test Tag");

        tag = new Tag();
        tag.setName("Test Tag");

        tagResponse = new TagResponse();
        tagResponse.setName("Test Tag");
    }

    @Test
    @Transactional
    public void testCreateTag() throws Exception {
        // Giả lập hành vi
        when(tagRepository.existsByName(tagDTO.getName())).thenReturn(false);
        when(tagMapper.toEntity(tagDTO)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toResponse(tag)).thenReturn(tagResponse);

        // Gọi phương thức và kiểm tra kết quả
        TagResponse result = tagService.createTag(tagDTO);
        assertNotNull(result);
        assertEquals("Test Tag", result.getName());

        // Kiểm tra các phương thức đã được gọi
        verify(tagRepository).existsByName(tagDTO.getName());
        verify(tagRepository).save(tag);
        verify(tagMapper).toResponse(tag);
    }

    @Test
    public void testCreateTagWithExistingName() throws Exception {
        // Giả lập hành vi khi tên tag đã tồn tại
        when(tagRepository.existsByName(tagDTO.getName())).thenReturn(true);

        // Kiểm tra ngoại lệ khi tên tag đã tồn tại
        assertThrows(AppException.class, () -> tagService.createTag(tagDTO));
    }

    @Test
    public void testGetTags() {
        // Tạo PageRequest
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Giả lập một list các tag
        Page<Tag> tagsPage = new PageImpl<>(Arrays.asList(tag));

        // Giả lập hành vi của findAll trong tagRepository
        when(tagRepository.findAll(pageRequest)).thenReturn(tagsPage);

        // Giả lập hành vi chuyển đổi từ Tag thành TagResponse
        TagResponse expectedTagResponse = new TagResponse();
        expectedTagResponse.setName("Test Tag Response");
        when(tagMapper.toResponse(tag)).thenReturn(expectedTagResponse);

        // Gọi phương thức và lấy kết quả
        Page<TagResponse> result = tagService.getTags(pageRequest);

        // Kiểm tra kết quả trả về có đúng như mong muốn
        assertNotNull(result, "Kết quả không được null");
        assertEquals(1, result.getTotalElements(), "Số lượng phần tử không đúng");
        assertEquals("Test Tag Response", result.getContent().get(0).getName(), "Tên tag trong response không đúng");

        // Kiểm tra các phương thức đã được gọi
        verify(tagRepository).findAll(pageRequest);
        verify(tagMapper).toResponse(tag);
    }

}
