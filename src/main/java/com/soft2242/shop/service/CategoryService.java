package com.soft2242.shop.service;

import com.soft2242.shop.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft2242.shop.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ycshang
 * @since 2023-11-09
 */
public interface CategoryService extends IService<Category> {

    /**
     * 首页 - 分类列表
     *
     * @return
     */
    List<Category> getIndexCategoryList();

    /**
     * 分类tab页 - 分类列表以及各分类下的商品
     *
     * @return
     */
    List<CategoryVO> getCategoryList();
}
