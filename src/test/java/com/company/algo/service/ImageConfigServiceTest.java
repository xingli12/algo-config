package com.company.algo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.algo.BaseTest;
import com.company.algo.domain.dto.ImageConfigDTO;
import com.company.algo.domain.entity.ImageAlgoRel;
import com.company.algo.domain.entity.ImageConfig;
import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.exception.BusinessException;
import com.company.algo.repository.ImageAlgoRelMapper;
import com.company.algo.repository.ImageConfigMapper;
import com.company.algo.repository.ResourceConfigMapper;
import com.company.algo.service.impl.ImageConfigServiceImpl;
import com.company.algo.domain.vo.ImageConfigVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 镜像配置服务测试
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@DisplayName("镜像配置服务测试")
public class ImageConfigServiceTest extends BaseTest {

    @Mock
    private ImageConfigMapper imageConfigMapper;

    @Mock
    private ImageAlgoRelMapper imageAlgoRelMapper;

    @Mock
    private ResourceConfigMapper resourceConfigMapper;

    @InjectMocks
    private ImageConfigServiceImpl imageConfigService;

    private ImageConfig testImageEntity;

    @BeforeEach
    void setUp() {
        testImageEntity = new ImageConfig();
        testImageEntity.setId(1L);
        testImageEntity.setImageName("test-image");
        testImageEntity.setImageTag("v1.0");
        testImageEntity.setRegistryUrl("registry.example.com/test-image:v1.0");
        testImageEntity.setStatus("ENABLED");
        testImageEntity.setCreatedAt(LocalDateTime.now());
        testImageEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("应该成功查询镜像配置详情")
    void shouldQueryDetailSuccess() {
        // Given
        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        when(imageAlgoRelMapper.selectList(any())).thenReturn(Collections.emptyList());

        // When
        ImageConfigVO result = imageConfigService.queryDetail(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test-image", result.getImageName());
        verify(imageConfigMapper).selectById(1L);
    }

    @Test
    @DisplayName("查询不存在的镜像配置应该抛出异常")
    void shouldThrowExceptionWhenQueryNotFound() {
        // Given
        when(imageConfigMapper.selectById(999L)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> imageConfigService.queryDetail(999L));
        assertEquals(ErrorCode.I004.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("创建镜像时镜像名称和标签已存在应该抛出异常")
    void shouldThrowExceptionWhenImageNameAndTagExists() {
        // Given
        ImageConfigDTO dto = new ImageConfigDTO();
        dto.setImageName("test-image");
        dto.setImageTag("v1.0");
        dto.setRegistryUrl("registry.example.com/test-image:v1.0");

        when(imageConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> imageConfigService.create(dto));
        assertEquals(ErrorCode.I001.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("应该成功创建镜像配置")
    void shouldCreateImageConfigSuccess() {
        // Given
        ImageConfigDTO dto = new ImageConfigDTO();
        dto.setImageName("new-image");
        dto.setImageTag("v2.0");
        dto.setRegistryUrl("registry.example.com/new-image:v2.0");

        when(imageConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(imageConfigMapper.insert(any(ImageConfig.class))).thenAnswer(invocation -> {
            ImageConfig entity = invocation.getArgument(0);
            entity.setId(789L);
            return 1;
        });

        // When
        Long id = imageConfigService.create(dto);

        // Then
        assertEquals(789L, id);
        verify(imageConfigMapper).insert(any(ImageConfig.class));
    }

    @Test
    @DisplayName("删除被引用的镜像应该抛出异常")
    void shouldThrowExceptionWhenImageHasDependencies() {
        // Given
        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        when(resourceConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> imageConfigService.delete(1L));
        assertEquals(ErrorCode.I002.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("应该成功删除镜像配置")
    void shouldDeleteImageConfigSuccess() {
        // Given
        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        when(resourceConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(imageConfigMapper.deleteById(1L)).thenReturn(1);

        // When
        imageConfigService.delete(1L);

        // Then
        verify(imageConfigMapper).deleteById(1L);
    }

    @Test
    @DisplayName("应该成功切换镜像状态")
    void shouldToggleStatusSuccess() {
        // Given
        testImageEntity.setStatus("ENABLED");
        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        when(imageConfigMapper.updateById(any(ImageConfig.class))).thenReturn(1);

        // When
        imageConfigService.toggleStatus(1L, "DISABLED");

        // Then
        verify(imageConfigMapper).updateById(any(ImageConfig.class));
    }
}
