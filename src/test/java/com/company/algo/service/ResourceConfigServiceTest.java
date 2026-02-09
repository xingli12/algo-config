package com.company.algo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.algo.BaseTest;
import com.company.algo.domain.dto.ResourceConfigDTO;
import com.company.algo.domain.entity.ImageConfig;
import com.company.algo.domain.entity.ResourceConfig;
import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.exception.BusinessException;
import com.company.algo.repository.ImageConfigMapper;
import com.company.algo.repository.ResourceConfigMapper;
import com.company.algo.service.impl.ResourceConfigServiceImpl;
import com.company.algo.domain.vo.ResourceConfigVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 资源配置服务测试
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@DisplayName("资源配置服务测试")
public class ResourceConfigServiceTest extends BaseTest {

    @Mock
    private ResourceConfigMapper resourceConfigMapper;

    @Mock
    private ImageConfigMapper imageConfigMapper;

    @InjectMocks
    private ResourceConfigServiceImpl resourceConfigService;

    private ResourceConfig testResourceEntity;
    private ImageConfig testImageEntity;

    @BeforeEach
    void setUp() {
        testImageEntity = new ImageConfig();
        testImageEntity.setId(1L);
        testImageEntity.setImageName("test-image");
        testImageEntity.setImageTag("v1.0");
        testImageEntity.setStatus("ENABLED");

        testResourceEntity = new ResourceConfig();
        testResourceEntity.setId(1L);
        testResourceEntity.setAlgorithmName("Test Algorithm");
        testResourceEntity.setImageId(1L);
        testResourceEntity.setCpuLimit("2000m");
        testResourceEntity.setMemLimit("4Gi");
        testResourceEntity.setCpuRequest("1000m");
        testResourceEntity.setMemRequest("2Gi");
        testResourceEntity.setStatus("ACTIVE");
        testResourceEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("应该成功查询资源配置详情")
    void shouldQueryDetailSuccess() {
        // Given
        when(resourceConfigMapper.selectById(1L)).thenReturn(testResourceEntity);
        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);

        // When
        ResourceConfigVO result = resourceConfigService.queryDetail(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Algorithm", result.getAlgorithmName());
        assertNotNull(result.getImageUrl());
        verify(resourceConfigMapper).selectById(1L);
    }

    @Test
    @DisplayName("查询不存在的资源配置应该抛出异常")
    void shouldThrowExceptionWhenQueryNotFound() {
        // Given
        when(resourceConfigMapper.selectById(999L)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> resourceConfigService.queryDetail(999L));
        assertEquals(ErrorCode.R003.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("CPU 请求超过限制应该抛出异常")
    void shouldThrowExceptionWhenCpuRequestExceedsLimit() {
        // Given
        ResourceConfigDTO dto = new ResourceConfigDTO();
        dto.setAlgorithmName("Test Algorithm");
        dto.setImageId(1L);
        dto.setCpuLimit("2000m");
        dto.setCpuRequest("3000m"); // 超过限制
        dto.setMemLimit("4Gi");
        dto.setMemRequest("2Gi");

        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        lenient().when(resourceConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> resourceConfigService.create(dto));
        assertEquals(ErrorCode.R001.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("内存请求超过限制应该抛出异常")
    void shouldThrowExceptionWhenMemRequestExceedsLimit() {
        // Given
        ResourceConfigDTO dto = new ResourceConfigDTO();
        dto.setAlgorithmName("Test Algorithm");
        dto.setImageId(1L);
        dto.setCpuLimit("2000m");
        dto.setCpuRequest("1000m");
        dto.setMemLimit("4Gi");
        dto.setMemRequest("8Gi"); // 超过限制

        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        lenient().when(resourceConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> resourceConfigService.create(dto));
        assertEquals(ErrorCode.R001.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("引用已禁用的镜像应该抛出异常")
    void shouldThrowExceptionWhenImageDisabled() {
        // Given
        testImageEntity.setStatus("DISABLED");

        ResourceConfigDTO dto = new ResourceConfigDTO();
        dto.setAlgorithmName("Test Algorithm");
        dto.setImageId(1L);

        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> resourceConfigService.create(dto));
        assertEquals(ErrorCode.I003.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("创建 ACTIVE 配置时已存在 ACTIVE 配置应该抛出异常")
    void shouldThrowExceptionWhenActiveConfigExists() {
        // Given
        ResourceConfigDTO dto = new ResourceConfigDTO();
        dto.setAlgorithmName("Test Algorithm");
        dto.setImageId(1L);
        dto.setCpuLimit("2000m");
        dto.setMemLimit("4Gi");
        dto.setCpuRequest("1000m");
        dto.setMemRequest("2Gi");
        dto.setStatus("ACTIVE");

        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        when(resourceConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> resourceConfigService.create(dto));
        assertEquals(ErrorCode.R002.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("应该成功创建资源配置")
    void shouldCreateResourceConfigSuccess() {
        // Given
        ResourceConfigDTO dto = new ResourceConfigDTO();
        dto.setAlgorithmName("Test Algorithm");
        dto.setImageId(1L);
        dto.setCpuLimit("2000m");
        dto.setMemLimit("4Gi");
        dto.setCpuRequest("1000m");
        dto.setMemRequest("2Gi");
        dto.setStatus("ACTIVE");

        when(imageConfigMapper.selectById(1L)).thenReturn(testImageEntity);
        when(resourceConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(resourceConfigMapper.insert(any(ResourceConfig.class))).thenAnswer(invocation -> {
            ResourceConfig entity = invocation.getArgument(0);
            entity.setId(456L);
            return 1;
        });

        // When
        Long id = resourceConfigService.create(dto);

        // Then
        assertEquals(456L, id);
        verify(resourceConfigMapper).insert(any(ResourceConfig.class));
    }
}
