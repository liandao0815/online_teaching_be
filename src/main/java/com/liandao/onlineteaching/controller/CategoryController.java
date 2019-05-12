package com.liandao.onlineteaching.controller;

import com.liandao.onlineteaching.entity.Category;
import com.liandao.onlineteaching.service.CategoryService;
import com.liandao.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/category")
    public Map<String, Object> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return ResponseUtil.success(null);
    }

    @GetMapping("/category/list")
    public Map<String, Object> getCategoryList() {
        List<Category> categoryList = categoryService.getCategoryList();
        return ResponseUtil.success(categoryList);
    }

    @DeleteMapping("/admin/category/{id}")
    public Map<String, Object> deleteCategory(@PathVariable("id") int id ) {
        categoryService.deleteCategory(id);
        return ResponseUtil.success(null);
    }

    @PatchMapping("/admin/category/{id}")
    public Map<String, Object> updateCategory(@PathVariable("id") int id, @RequestBody Category category) {
        categoryService.updateCategory(id, category);
        return ResponseUtil.success(null);
    }
}
