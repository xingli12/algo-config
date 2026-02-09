package com.company.algo.util.converter;

import com.company.algo.domain.entity.ResourceConfig;
import com.company.algo.domain.vo.ResourceConfigVO;
import org.springframework.beans.BeanUtils;

/**
 * 资源配置转换器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class ResourceConfigConverter {

    /**
     * Entity 转换为 VO
     */
    public static ResourceConfigVO toVO(ResourceConfig entity) {
        if (entity == null) {
            return null;
        }
        ResourceConfigVO vo = new ResourceConfigVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
