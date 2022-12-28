package ru.practicum.category.service;

import lombok.AllArgsConstructor;
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

@AllArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        List<Category> categories = categoryRepository.findAll(getPageable(from, size)).getContent();
        log.info("List of all categories retrieved");

        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        Category category = getByIdAndTrow(categoryId);
        log.info("Category with id: {} retrieved", categoryId);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        checkIfExistsWithSameNameAndAnotherId(categoryDto.getName(), categoryDto.getId());
        Category category = getByIdAndTrow(categoryDto.getId());
        category.setName(categoryDto.getName());
        log.info("Updated category {}", category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        checkIfExistsWithSameName(newCategoryDto.getName());
        Category category = categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto));
        log.info("Added category {}", category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void delete(Long categoryId) {
        getById(categoryId);
        categoryRepository.deleteById(categoryId);
        log.info("Category {} deleted", categoryId);
    }

    private Category getByIdAndTrow(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category {} not found", categoryId));
    }

    void checkIfExistsWithSameNameAndAnotherId(String name, Long categoryId) {
        Optional<Category> categoryWithSameName = categoryRepository.findByName(name);
        if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getId().equals(categoryId)) {
            throw new ConflictException("Category with the same name and another id already exists");
        }
    }

    void checkIfExistsWithSameName(String name) {
        Optional<Category> categoryWithSameName = categoryRepository.findByName(name);
        if (categoryWithSameName.isPresent()) {
            throw new ConflictException("Category with the same name already exists");
        }
    }

    private Pageable getPageable(Integer from, Integer size) {
        return new PageableRequest(from, size, Sort.unsorted());
    }
}
