package com.github.firstlord.financeservice.service;

import com.github.firstlord.financeservice.dto.category.CategoryCreateDTO;
import com.github.firstlord.financeservice.dto.category.CategoryResponseDTO;
import com.github.firstlord.financeservice.dto.category.CategoryUpdateDTO;
import com.github.firstlord.financeservice.dto.mapper.CategoryMapper;
import com.github.firstlord.financeservice.exception.IllegalArgumentException;
import com.github.firstlord.financeservice.exception.ResourceNotFoundException;
import com.github.firstlord.financeservice.model.Category;
import com.github.firstlord.financeservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryResponseDTO create(CategoryCreateDTO dto, String userId) {
        // проверяем, нет ли уже такой категории
        Category existing = categoryRepository.findByTitle(dto.getTitle()).orElse(null);
        if (existing != null) {
            throw new IllegalArgumentException("Category with title '" + dto.getTitle() + "' already exists");
        }

        Category category = categoryMapper.fromCreateDTO(dto);
        category.setUserId(userId);

        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    public List<CategoryResponseDTO> getAll(String userId) {
        List<Category> categories = categoryRepository.findAllByUserId(userId);
        List<CategoryResponseDTO> result = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            CategoryResponseDTO dto = categoryMapper.toDTO(c);
            result.add(dto);
        }

        return result;
    }

    public CategoryResponseDTO getById(UUID id, String userId) {
        Category category = findByIdAndCheck(id, userId);
        return categoryMapper.toDTO(category);
    }

    public CategoryResponseDTO update(UUID id, CategoryUpdateDTO dto, String userId) {
        Category category = findByIdAndCheck(id, userId);

        // если название поменялось, проверяем не занято ли оно
        if (!category.getTitle().equals(dto.getTitle())) {
            Category existing = categoryRepository.findByTitle(dto.getTitle()).orElse(null);
            if (existing != null) {
                throw new IllegalArgumentException("Category with title '" + dto.getTitle() + "' already exists");
            }
        }

        category.setTitle(dto.getTitle());
        category.setType(dto.getType());

        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    public void delete(UUID id, String userId) {
        Category category = findByIdAndCheck(id, userId);
        categoryRepository.delete(category);
    }

    private Category findByIdAndCheck(UUID id, String userId) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        if (!category.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found");
        }

        return category;
    }
}