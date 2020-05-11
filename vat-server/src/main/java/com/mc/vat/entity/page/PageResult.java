package com.mc.vat.entity.page;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页结果
 * @author kai
 * @date 2020-05-10 21:56
 */
@Setter
@Getter
public class PageResult<T> {
    /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页数量
     */
    private int pageSize;
    /**
     * 记录总数
     */
    private long totalSize;
    /**
     * 页码总数
     */
    private int totalPage;
    /**
     * 响应数据
     */
    private List<T> items;

    public PageResult(PageInfo<T> pageInfo) {
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.totalSize = pageInfo.getTotal();
        this.totalPage = pageInfo.getPages();
        this.items = pageInfo.getList();
    }

}
