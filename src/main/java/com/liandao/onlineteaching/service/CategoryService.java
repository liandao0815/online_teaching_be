package com.liandao.onlineteaching.service;

import com.liandao.onlineteaching.config.CustomException;
import com.liandao.onlineteaching.dao.CategoryMapper;
import com.liandao.onlineteaching.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public void createCategory(Category categoryParams) {
        Category newCategory = new Category();

        if (categoryParams.getName() == null || categoryParams.getName().isEmpty())
            throw new CustomException("分类名称不能为空");

        Category category = categoryMapper.findByName(categoryParams.getName());
        if(category != null)
            throw new CustomException("该分类已存在，请修改后重试");

        newCategory.setName(categoryParams.getName());
        categoryMapper.create(newCategory);
    }

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    @Transactional
    public void deleteCategory(int id) {
        categoryMapper.delete(id);
    }

    @Transactional
    public void updateCategory(int id, Category categoryParams) {
        Category newCategory = new Category();

        if (categoryParams.getName() == null || categoryParams.getName().isEmpty())
            throw new CustomException("分类名称不能为空");
        if(this.existCategoryName(id, categoryParams.getName()))
            throw new CustomException("该分类已存在，请修改后重试");

        newCategory.setId(id);
        newCategory.setName(categoryParams.getName());
        categoryMapper.update(newCategory);
    }

    private boolean existCategoryName(int id, String categoryName) {
        Category categoryByName = categoryMapper.findByName(categoryName);

        if(categoryByName == null) {
            return false;
        } else {
            Category categoryById = categoryMapper.findById(id);
            return !categoryName.equals(categoryById.getName());
        }
    }
}
