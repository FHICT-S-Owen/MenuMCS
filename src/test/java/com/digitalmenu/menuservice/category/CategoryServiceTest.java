package com.digitalmenu.menuservice.category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.persistence.EntityExistsException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository ;
    private CategoryService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CategoryService(categoryRepository);
    }

    @Test
    void canGetCategories() {
        // when
        underTest.getCategories();
        // then
        verify(categoryRepository).findAll();
    }

    @Test
    void canCreateCategory() {
        // given
        Category category = new Category(
                1,
                "Meat"
        );

        // when
        underTest.createCategory(category);

        // then
        ArgumentCaptor<Category> dishArgumentCaptor =
                ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository)
                .save(dishArgumentCaptor.capture());

        Category capturedCategory = dishArgumentCaptor.getValue();

        assertThat(capturedCategory).isEqualTo(category);
    }

    @Test
    void willThrowWhenCategoryIsTaken() {
        // given
        Category category = new Category(
                1,
                "Meat"
        );

        given(categoryRepository.findCategoryByName(anyString()))
                .willReturn(java.util.Optional.of(category));
        // when
        // then
        assertThatThrownBy(() -> underTest.createCategory(category))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("Name already taken!");
        verify(categoryRepository, never()).save(any());
    }
}