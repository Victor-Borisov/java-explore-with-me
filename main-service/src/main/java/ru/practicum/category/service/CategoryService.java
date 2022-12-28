package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long categoryId);

    CategoryDto update(CategoryDto categoryDto);

    CategoryDto add(NewCategoryDto newCategoryDto);

    void delete(Long categoryId);
}
