package com.company.algo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.algo.aspect.AuditLog;
import com.company.algo.domain.dto.AlgoConfigDTO;
import com.company.algo.domain.entity.AlgoConfig;
import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.domain.vo.AlgoConfigVO;
import com.company.algo.exception.BusinessException;
import com.company.algo.repository.AlgoConfigMapper;
import com.company.algo.service.AlgoConfigService;
import com.company.algo.util.converter.AlgoConfigConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 算法配置服务实现类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Slf4j
@Service
public class AlgoConfigServiceImpl implements AlgoConfigService {

    @Resource
    private AlgoConfigMapper algoConfigMapper;

    @Override
    public IPage<AlgoConfigVO> queryPage(Integer page, Integer size, String keyword) {
        Page<AlgoConfig> pageParam = new Page<>(page, size);
        IPage<AlgoConfig> resultPage;

        if (StringUtils.hasText(keyword)) {
            resultPage = algoConfigMapper.selectPageWithKeyword(pageParam, keyword);
        } else {
            LambdaQueryWrapper<AlgoConfig> queryWrapper = new LambdaQueryWrapper<AlgoConfig>()
                    .orderByDesc(AlgoConfig::getUpdatedAt);
            resultPage = algoConfigMapper.selectPage(pageParam, queryWrapper);
        }

        // 转换为 VO
        return resultPage.convert(AlgoConfigConverter::toVO);
    }

    @Override
    public AlgoConfigVO queryDetail(Long id) {
        AlgoConfig entity = algoConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.A004);
        }
        return AlgoConfigConverter.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(module = "ALGO", operation = "CREATE")
    public Long create(AlgoConfigDTO dto) {
        // 校验 Recipe ID 唯一性
        LambdaQueryWrapper<AlgoConfig> queryWrapper = new LambdaQueryWrapper<AlgoConfig>()
                .eq(AlgoConfig::getRecipeId, dto.getRecipeId());
        Long count = algoConfigMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.A001);
        }

        // 校验 DC 配置必填
        if (dto.getAggregateEnabled() != null && dto.getAggregateEnabled() == 1) {
            if (!StringUtils.hasText(dto.getDcConfigJson())) {
                throw new BusinessException(ErrorCode.A003);
            }
        }

        // 转换并保存
        AlgoConfig entity = new AlgoConfig();
        BeanUtils.copyProperties(dto, entity);

        // 设置默认值
        if (entity.getAggregateEnabled() == null) {
            entity.setAggregateEnabled(0);
        }
        if (entity.getSendKov() == null) {
            entity.setSendKov(0);
        }
        if (entity.getSendLis() == null) {
            entity.setSendLis(0);
        }
        if (entity.getSendLithops() == null) {
            entity.setSendLithops(0);
        }
        if (entity.getEnabled() == null) {
            entity.setEnabled(1);
        }

        algoConfigMapper.insert(entity);
        log.info("创建算法配置成功: id={}, recipeId={}", entity.getId(), entity.getRecipeId());
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(module = "ALGO", operation = "UPDATE")
    public void update(Long id, AlgoConfigDTO dto) {
        // 检查是否存在
        AlgoConfig existingEntity = algoConfigMapper.selectById(id);
        if (existingEntity == null) {
            throw new BusinessException(ErrorCode.A004);
        }

        // 校验 Recipe ID 唯一性（排除自己）
        if (!existingEntity.getRecipeId().equals(dto.getRecipeId())) {
            LambdaQueryWrapper<AlgoConfig> queryWrapper = new LambdaQueryWrapper<AlgoConfig>()
                    .eq(AlgoConfig::getRecipeId, dto.getRecipeId())
                    .ne(AlgoConfig::getId, id);
            Long count = algoConfigMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.A001);
            }
        }

        // 校验 DC 配置必填
        if (dto.getAggregateEnabled() != null && dto.getAggregateEnabled() == 1) {
            if (!StringUtils.hasText(dto.getDcConfigJson())) {
                throw new BusinessException(ErrorCode.A003);
            }
        }

        // 更新
        AlgoConfig entity = new AlgoConfig();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        algoConfigMapper.updateById(entity);
        log.info("更新算法配置成功: id={}, recipeId={}", id, entity.getRecipeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(module = "ALGO", operation = "ENABLE")
    public void toggleStatus(Long id, Integer enabled) {
        AlgoConfig entity = algoConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.A004);
        }

        entity.setEnabled(enabled);
        algoConfigMapper.updateById(entity);
        log.info("切换算法配置状态成功: id={}, enabled={}", id, enabled);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(module = "ALGO", operation = "DELETE")
    public void delete(Long id) {
        AlgoConfig entity = algoConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.A004);
        }

        algoConfigMapper.deleteById(id);
        log.info("删除算法配置成功: id={}, recipeId={}", id, entity.getRecipeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AuditLog(module = "ALGO", operation = "CREATE")
    public void uploadFile(Long id, String filePath) {
        AlgoConfig entity = algoConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.A004);
        }

        entity.setAlgorithmFilePath(filePath);
        algoConfigMapper.updateById(entity);
        log.info("上传算法文件成功: id={}, filePath={}", id, filePath);
    }
}
