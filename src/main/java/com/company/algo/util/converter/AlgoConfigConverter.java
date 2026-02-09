package com.company.algo.util.converter;

import com.company.algo.domain.entity.AlgoConfig;
import com.company.algo.domain.vo.AlgoConfigVO;
import org.springframework.beans.BeanUtils;

/**
 * 算法配置转换器
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class AlgoConfigConverter {

    /**
     * Entity 转换为 VO
     */
    public static AlgoConfigVO toVO(AlgoConfig entity) {
        if (entity == null) {
            return null;
        }
        AlgoConfigVO vo = new AlgoConfigVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
