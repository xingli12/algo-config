package com.company.algo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.algo.domain.dto.ResourceConfigDTO;
import com.company.algo.domain.entity.ImageConfig;
import com.company.algo.domain.entity.ResourceConfig;
import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.domain.vo.ResourceConfigVO;
import com.company.algo.exception.BusinessException;
import com.company.algo.repository.ImageConfigMapper;
import com.company.algo.repository.ResourceConfigMapper;
import com.company.algo.service.ResourceConfigService;
import com.company.algo.util.converter.ResourceConfigConverter;
import com.company.algo.util.validation.ResourceFormatValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 资源配置服务实现类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Slf4j
@Service
public class ResourceConfigServiceImpl implements ResourceConfigService {

    @Resource
    private ResourceConfigMapper resourceConfigMapper;

    @Resource
    private ImageConfigMapper imageConfigMapper;

    @Override
    public IPage<ResourceConfigVO> queryPage(Integer page, Integer size) {
        Page<ResourceConfig> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<ResourceConfig> queryWrapper = new LambdaQueryWrapper<ResourceConfig>()
                .orderByDesc(ResourceConfig::getUpdatedAt);

        IPage<ResourceConfig> resultPage = resourceConfigMapper.selectPage(pageParam, queryWrapper);

        // 转换为 VO，并填充镜像地址
        return resultPage.convert(entity -> {
            ResourceConfigVO vo = ResourceConfigConverter.toVO(entity);
            // 查询镜像地址
            ImageConfig imageConfig = imageConfigMapper.selectById(entity.getImageId());
            if (imageConfig != null) {
                vo.setImageUrl(imageConfig.getRegistryUrl() + "/" +
                        imageConfig.getImageName() + ":" + imageConfig.getImageTag());
            }
            return vo;
        });
    }

    @Override
    public ResourceConfigVO queryDetail(Long id) {
        ResourceConfig entity = resourceConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.R003);
        }

        ResourceConfigVO vo = ResourceConfigConverter.toVO(entity);

        // 查询镜像地址
        ImageConfig imageConfig = imageConfigMapper.selectById(entity.getImageId());
        if (imageConfig != null) {
            vo.setImageUrl(imageConfig.getRegistryUrl() + "/" +
                    imageConfig.getImageName() + ":" + imageConfig.getImageTag());
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(ResourceConfigDTO dto) {
        // 校验镜像是否存在且已启用
        ImageConfig imageConfig = imageConfigMapper.selectById(dto.getImageId());
        if (imageConfig == null) {
            throw new BusinessException(ErrorCode.I004);
        }
        if (!"ENABLED".equals(imageConfig.getStatus())) {
            throw new BusinessException(ErrorCode.I003);
        }

        // 校验资源请求 ≤ 限制
        validateResourceRequest(dto);

        // 校验每个算法仅允许一个 ACTIVE 配置
        if ("ACTIVE".equals(dto.getStatus()) || dto.getStatus() == null) {
            LambdaQueryWrapper<ResourceConfig> queryWrapper = new LambdaQueryWrapper<ResourceConfig>()
                    .eq(ResourceConfig::getAlgorithmName, dto.getAlgorithmName())
                    .eq(ResourceConfig::getStatus, "ACTIVE");
            Long count = resourceConfigMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.R002);
            }
        }

        // 转换并保存
        ResourceConfig entity = new ResourceConfig();
        BeanUtils.copyProperties(dto, entity);

        // 设置默认值
        if (entity.getStatus() == null) {
            entity.setStatus("ACTIVE");
        }

        resourceConfigMapper.insert(entity);
        log.info("创建资源配置成功: id={}, algorithmName={}", entity.getId(), entity.getAlgorithmName());
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ResourceConfigDTO dto) {
        // 检查是否存在
        ResourceConfig existingEntity = resourceConfigMapper.selectById(id);
        if (existingEntity == null) {
            throw new BusinessException(ErrorCode.R003);
        }

        // 校验镜像是否存在且已启用
        ImageConfig imageConfig = imageConfigMapper.selectById(dto.getImageId());
        if (imageConfig == null) {
            throw new BusinessException(ErrorCode.I004);
        }
        if (!"ENABLED".equals(imageConfig.getStatus())) {
            throw new BusinessException(ErrorCode.I003);
        }

        // 校验资源请求 ≤ 限制
        validateResourceRequest(dto);

        // 如果要设置为 ACTIVE，检查是否已有 ACTIVE 配置
        if ("ACTIVE".equals(dto.getStatus()) && !"ACTIVE".equals(existingEntity.getStatus())) {
            LambdaQueryWrapper<ResourceConfig> queryWrapper = new LambdaQueryWrapper<ResourceConfig>()
                    .eq(ResourceConfig::getAlgorithmName, dto.getAlgorithmName())
                    .eq(ResourceConfig::getStatus, "ACTIVE")
                    .ne(ResourceConfig::getId, id);
            Long count = resourceConfigMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.R002);
            }
        }

        // 更新
        ResourceConfig entity = new ResourceConfig();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        resourceConfigMapper.updateById(entity);
        log.info("更新资源配置成功: id={}, algorithmName={}", id, entity.getAlgorithmName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ResourceConfig entity = resourceConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.R003);
        }

        resourceConfigMapper.deleteById(id);
        log.info("删除资源配置成功: id={}, algorithmName={}", id, entity.getAlgorithmName());
    }

    @Override
    public void deploy(Long id) {
        ResourceConfig entity = resourceConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.R003);
        }

        // TODO: 实际调用资源配置下发系统
        log.info("触发资源配置下发: id={}, algorithmName={}", id, entity.getAlgorithmName());
    }

    @Override
    public void schedule(Long id, String scheduleCron) {
        ResourceConfig entity = resourceConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.R003);
        }

        // TODO: 实际创建定时调度任务
        log.info("创建定时调度任务: id={}, algorithmName={}, cron={}",
                id, entity.getAlgorithmName(), scheduleCron);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(Long id, String targetVersion) {
        ResourceConfig entity = resourceConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.R003);
        }

        // TODO: 实现回滚逻辑
        // 1. 将当前配置保存为历史版本
        // 2. 恢复目标版本的配置
        // 3. 更新 current_version 字段

        log.info("回滚资源配置: id={}, algorithmName={}, targetVersion={}",
                id, entity.getAlgorithmName(), targetVersion);
    }

    /**
     * 校验资源请求 ≤ 限制
     */
    private void validateResourceRequest(ResourceConfigDTO dto) {
        // CPU 请求 ≤ 限制
        if (StringUtils.hasText(dto.getCpuLimit()) && StringUtils.hasText(dto.getCpuRequest())) {
            if (!ResourceFormatValidator.isValidCpuValue(dto.getCpuRequest())
                    || !ResourceFormatValidator.isValidCpuValue(dto.getCpuLimit())) {
                throw new BusinessException(ErrorCode.C001);
            }

            int cpuRequest = ResourceFormatValidator.parseCpuValue(dto.getCpuRequest());
            int cpuLimit = ResourceFormatValidator.parseCpuValue(dto.getCpuLimit());

            if (cpuRequest > cpuLimit) {
                throw new BusinessException(ErrorCode.R001);
            }
        }

        // 内存请求 ≤ 限制
        if (StringUtils.hasText(dto.getMemLimit()) && StringUtils.hasText(dto.getMemRequest())) {
            if (!ResourceFormatValidator.isValidMemoryValue(dto.getMemRequest())
                    || !ResourceFormatValidator.isValidMemoryValue(dto.getMemLimit())) {
                throw new BusinessException(ErrorCode.C001);
            }

            int memRequest = ResourceFormatValidator.parseMemoryValue(dto.getMemRequest());
            int memLimit = ResourceFormatValidator.parseMemoryValue(dto.getMemLimit());

            if (memRequest > memLimit) {
                throw new BusinessException(ErrorCode.R001);
            }
        }
    }
}
