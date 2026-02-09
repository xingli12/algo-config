package com.company.algo.util.converter;

import com.company.algo.domain.entity.ImageConfig;
import com.company.algo.domain.vo.ImageConfigVO;
import org.springframework.beans.BeanUtils;

/**
 * 镜像配置转换器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class ImageConfigConverter {

    /**
     * Entity 转换为 VO
     */
    public static ImageConfigVO toVO(ImageConfig entity) {
        if (entity == null) {
            return null;
        }
        ImageConfigVO vo = new ImageConfigVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
