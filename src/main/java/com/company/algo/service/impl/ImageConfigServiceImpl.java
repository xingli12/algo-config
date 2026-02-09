package com.company.algo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.algo.domain.dto.ImageConfigDTO;
import com.company.algo.domain.entity.ImageAlgoRel;
import com.company.algo.domain.entity.ImageConfig;
import com.company.algo.domain.enums.ErrorCode;
import com.company.algo.domain.vo.ImageConfigVO;
import com.company.algo.exception.BusinessException;
import com.company.algo.repository.ImageAlgoRelMapper;
import com.company.algo.repository.ImageConfigMapper;
import com.company.algo.repository.ResourceConfigMapper;
import com.company.algo.service.ImageConfigService;
import com.company.algo.util.converter.ImageConfigConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 镜像配置服务实现类
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@Service
public class ImageConfigServiceImpl implements ImageConfigService {


    private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(ImageConfigServiceImpl.class);
    @Resource
    private ImageConfigMapper imageConfigMapper;

    @Resource
    private ImageAlgoRelMapper imageAlgoRelMapper;

    @Resource
    private ResourceConfigMapper resourceConfigMapper;

    @Override
    public List<ImageConfigVO> listAll() {
        LambdaQueryWrapper<ImageConfig> queryWrapper = new LambdaQueryWrapper<ImageConfig>()
                .orderByDesc(ImageConfig::getCreatedAt);
        List<ImageConfig> entities = imageConfigMapper.selectList(queryWrapper);

        return entities.stream()
                .map(ImageConfigConverter::toVO)
                .peek(vo -> {
                    // 查询关联的算法名称
                    LambdaQueryWrapper<ImageAlgoRel> relQueryWrapper = new LambdaQueryWrapper<ImageAlgoRel>()
                            .eq(ImageAlgoRel::getImageId, vo.getId());
                    List<ImageAlgoRel> rels = imageAlgoRelMapper.selectList(relQueryWrapper);
                    List<String> algorithmNames = rels.stream()
                            .map(ImageAlgoRel::getAlgorithmName)
                            .collect(Collectors.toList());
                    vo.setAlgorithmNames(algorithmNames);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ImageConfigVO queryDetail(Long id) {
        ImageConfig entity = imageConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.I004);
        }

        ImageConfigVO vo = ImageConfigConverter.toVO(entity);

        // 查询关联的算法名称
        LambdaQueryWrapper<ImageAlgoRel> relQueryWrapper = new LambdaQueryWrapper<ImageAlgoRel>()
                .eq(ImageAlgoRel::getImageId, id);
        List<ImageAlgoRel> rels = imageAlgoRelMapper.selectList(relQueryWrapper);
        List<String> algorithmNames = rels.stream()
                .map(ImageAlgoRel::getAlgorithmName)
                .collect(Collectors.toList());
        vo.setAlgorithmNames(algorithmNames);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(ImageConfigDTO dto) {
        // 校验镜像名称+标签唯一性
        LambdaQueryWrapper<ImageConfig> queryWrapper = new LambdaQueryWrapper<ImageConfig>()
                .eq(ImageConfig::getImageName, dto.getImageName())
                .eq(ImageConfig::getImageTag, dto.getImageTag());
        Long count = imageConfigMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.I001);
        }

        // 转换并保存镜像配置
        ImageConfig entity = new ImageConfig();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus("ENABLED");
        imageConfigMapper.insert(entity);

        // 保存关联的算法
        if (!CollectionUtils.isEmpty(dto.getAlgorithmNames())) {
            for (String algorithmName : dto.getAlgorithmNames()) {
                ImageAlgoRel rel = new ImageAlgoRel();
                rel.setImageId(entity.getId());
                rel.setAlgorithmName(algorithmName);
                imageAlgoRelMapper.insert(rel);
            }
        }

        log.info("创建镜像配置成功: id=" + entity.getId() + ", imageName=" + entity.getImageName() + ", imageTag=" + entity.getImageTag());
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ImageConfigDTO dto) {
        // 检查是否存在
        ImageConfig existingEntity = imageConfigMapper.selectById(id);
        if (existingEntity == null) {
            throw new BusinessException(ErrorCode.I004);
        }

        // 校验镜像名称+标签唯一性（排除自己）
        if (!existingEntity.getImageName().equals(dto.getImageName())
                || !existingEntity.getImageTag().equals(dto.getImageTag())) {
            LambdaQueryWrapper<ImageConfig> queryWrapper = new LambdaQueryWrapper<ImageConfig>()
                    .eq(ImageConfig::getImageName, dto.getImageName())
                    .eq(ImageConfig::getImageTag, dto.getImageTag())
                    .ne(ImageConfig::getId, id);
            Long count = imageConfigMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.I001);
            }
        }

        // 更新镜像配置
        ImageConfig entity = new ImageConfig();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        imageConfigMapper.updateById(entity);

        // 更新关联的算法（先删除旧的，再插入新的）
        LambdaQueryWrapper<ImageAlgoRel> relQueryWrapper = new LambdaQueryWrapper<ImageAlgoRel>()
                .eq(ImageAlgoRel::getImageId, id);
        imageAlgoRelMapper.delete(relQueryWrapper);

        if (!CollectionUtils.isEmpty(dto.getAlgorithmNames())) {
            for (String algorithmName : dto.getAlgorithmNames()) {
                ImageAlgoRel rel = new ImageAlgoRel();
                rel.setImageId(id);
                rel.setAlgorithmName(algorithmName);
                imageAlgoRelMapper.insert(rel);
            }
        }

        log.info("更新镜像配置成功: id=" + id + ", imageName=" + entity.getImageName() + ", imageTag=" + entity.getImageTag());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, String status) {
        ImageConfig entity = imageConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.I004);
        }

        entity.setStatus(status);
        imageConfigMapper.updateById(entity);
        log.info("切换镜像配置状态成功: id=" + id + ", status=" + status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 检查是否存在
        ImageConfig entity = imageConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.I004);
        }

        // 检查是否被 resource_config 引用
        LambdaQueryWrapper<com.company.algo.domain.entity.ResourceConfig> queryWrapper =
                new LambdaQueryWrapper<com.company.algo.domain.entity.ResourceConfig>()
                        .eq(com.company.algo.domain.entity.ResourceConfig::getImageId, id);
        Long count = resourceConfigMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.I002);
        }

        // 删除镜像配置（级联删除 image_algo_rel 由数据库外键处理）
        imageConfigMapper.deleteById(id);
        log.info("删除镜像配置成功: id=" + id + ", imageName=" + entity.getImageName() + ", imageTag=" + entity.getImageTag());
    }

    @Override
    public void deploy(Long id) {
        ImageConfig entity = imageConfigMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.I004);
        }

        // TODO: 实际调用镜像部署系统
        log.info("触发镜像部署: id=" + id + ", imageName=" + entity.getImageName() + ", imageTag=" + entity.getImageTag());
    }
}
