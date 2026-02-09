package com.company.algo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.algo.BaseTest;
import com.company.algo.domain.dto.AlgoConfigDTO;
import com.company.algo.domain.entity.AlgoConfig;
import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.exception.BusinessException;
import com.company.algo.repository.AlgoConfigMapper;
import com.company.algo.service.impl.AlgoConfigServiceImpl;
import com.company.algo.domain.vo.AlgoConfigVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 算法配置服务测试
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@DisplayName("算法配置服务测试")
public class AlgoConfigServiceTest extends BaseTest {

    @Mock
    private AlgoConfigMapper algoConfigMapper;

    @InjectMocks
    private AlgoConfigServiceImpl algoConfigService;

    private AlgoConfig testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new AlgoConfig();
        testEntity.setId(1L);
        testEntity.setRecipeId("RCP-CD-001");
        testEntity.setEqpType("CD");
        testEntity.setAlgorithmName("Test Algorithm");
        testEntity.setAlgorithmVersion("v1.0");
        testEntity.setEnabled(1);
        testEntity.setCreatedAt(LocalDateTime.now());
        testEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("应该成功查询算法配置详情")
    void shouldQueryDetailSuccess() {
        // Given
        when(algoConfigMapper.selectById(1L)).thenReturn(testEntity);

        // When
        AlgoConfigVO result = algoConfigService.queryDetail(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("RCP-CD-001", result.getRecipeId());
        verify(algoConfigMapper).selectById(1L);
    }

    @Test
    @DisplayName("查询不存在的算法配置应该抛出异常")
    void shouldThrowExceptionWhenQueryNotFound() {
        // Given
        when(algoConfigMapper.selectById(999L)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> algoConfigService.queryDetail(999L));
        assertEquals(ErrorCode.A004.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("创建算法配置时 Recipe ID 已存在应该抛出异常")
    void shouldThrowExceptionWhenRecipeIdExists() {
        // Given
        AlgoConfigDTO dto = new AlgoConfigDTO();
        dto.setRecipeId("RCP-CD-001");
        dto.setEqpType("CD");
        dto.setAlgorithmName("Test Algorithm");
        dto.setAlgorithmVersion("v1.0");

        when(algoConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> algoConfigService.create(dto));
        assertEquals(ErrorCode.A001.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("开启聚合但未填写 DC 配置应该抛出异常")
    void shouldThrowExceptionWhenDcConfigMissing() {
        // Given
        AlgoConfigDTO dto = new AlgoConfigDTO();
        dto.setRecipeId("RCP-CD-002");
        dto.setEqpType("CD");
        dto.setAlgorithmName("Test Algorithm");
        dto.setAlgorithmVersion("v1.0");
        dto.setAggregateEnabled(1);
        dto.setDcConfigJson(null);

        when(algoConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> algoConfigService.create(dto));
        assertEquals(ErrorCode.A003.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("应该成功创建算法配置")
    void shouldCreateAlgoConfigSuccess() {
        // Given
        AlgoConfigDTO dto = new AlgoConfigDTO();
        dto.setRecipeId("RCP-CD-002");
        dto.setEqpType("CD");
        dto.setAlgorithmName("Test Algorithm");
        dto.setAlgorithmVersion("v1.0");
        dto.setAggregateEnabled(0);

        when(algoConfigMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(algoConfigMapper.insert(any(AlgoConfig.class))).thenAnswer(invocation -> {
            AlgoConfig entity = invocation.getArgument(0);
            entity.setId(123L);
            return 1;
        });

        // When
        Long id = algoConfigService.create(dto);

        // Then
        assertEquals(123L, id);
        verify(algoConfigMapper).insert(any(AlgoConfig.class));
    }

    @Test
    @DisplayName("应该成功删除算法配置")
    void shouldDeleteAlgoConfigSuccess() {
        // Given
        when(algoConfigMapper.selectById(1L)).thenReturn(testEntity);
        when(algoConfigMapper.deleteById(1L)).thenReturn(1);

        // When
        algoConfigService.delete(1L);

        // Then
        verify(algoConfigMapper).deleteById(1L);
    }

    @AfterEach
    void tearDown() {
        // 清理
    }
}
