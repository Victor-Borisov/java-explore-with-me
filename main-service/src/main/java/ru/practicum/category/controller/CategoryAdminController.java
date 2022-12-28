package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Called add");

        return categoryService.add(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Called update");

        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long categoryId) {
        log.info("Called delete");

        categoryService.delete(categoryId);
    }
}

