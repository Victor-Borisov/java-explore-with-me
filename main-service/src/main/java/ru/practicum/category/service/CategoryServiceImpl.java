package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utils.PageableRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        List<Category> categories = categoryRepository.findAll(getPageable(from, size)).getContent();
        log.info("List of all categories retrieved");

        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long categoryId) {
        Category category = getByIdAndTrow(categoryId);
        log.info("Category with id: {} retrieved", categoryId);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = getByIdAndTrow(categoryDto.getId());
        category.setName(categoryDto.getName());
        log.info("Updated category {}", category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto));
        log.info("Added category {}", category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void delete(long categoryId) {
        getById(categoryId);
        categoryRepository.deleteById(categoryId);
        log.info("Category {} deleted", categoryId);
    }

    private Category getByIdAndTrow(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category {} not found", categoryId));
    }

    private Pageable getPageable(Integer from, Integer size) {
        return new PageableRequest(from, size, Sort.unsorted());
    }
}
