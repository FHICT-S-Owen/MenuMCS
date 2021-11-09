package com.digitalmenu.menuservice.category;

import com.digitalmenu.menuservice.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void deleteCategory(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ApiRequestException("Category with id " + categoryId + " does not exists");
        }
        else {
            categoryRepository.deleteById(categoryId);
        }
    }

    public void createCategory(Category category) {
        Optional<Category> categoryByName = categoryRepository.findCategoryByName(category.getName());
        if (categoryByName.isPresent()) {
            throw new EntityExistsException("Name already taken!");
        }
        categoryRepository.save(category);
    }

    public boolean updateCategory(Integer id, Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category actualCategory = optionalCategory.get();
            actualCategory.setName(category.getName());
            categoryRepository.save(actualCategory);
        }
        else
        {
            throw new ApiRequestException("There is no category found with id " + id);
        }
        return false;
    }

    public List<Category> getCategories(){
        if (categoryRepository.count() == 0)
        {
            throw new ApiRequestException("There are no categories found");
        }
        else {
            return categoryRepository.findAll();
        }
    }

    public Optional<Category> getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findCategoryByName(name);
        if (category.isPresent())
        {
            return category;
        }
        else {
            throw new ApiRequestException("There is no category found with name " + name);
        }
    }
}
