package com.company.algo.domain.vo;

import java.io.Serializable;

/**
 * 算法配置查询 VO
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
public class AlgoConfigQueryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码（从1开始）
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 关键字（搜索算法名称、Recipe ID、Product ID）
     */
    private String keyword;

    /**
     * 启用状态（可选）
     */
    private Integer enabled;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
