package kitchenpos.ui;

import static kitchenpos.utils.generator.MenuFixtureGenerator.generateMenu;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.utils.BaseTest;
import kitchenpos.utils.generator.MenuGroupFixtureGenerator;
import kitchenpos.utils.generator.ProductFixtureGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:MenuGroup")
@Import({MenuGroupFixtureGenerator.class, ProductFixtureGenerator.class})
class MenuRestControllerTest extends BaseTest {

    private final MenuGroupFixtureGenerator menuGroupFixtureGenerator;
    private final ProductFixtureGenerator productFixtureGenerator;

    public MenuRestControllerTest(
        MenuGroupFixtureGenerator menuGroupFixtureGenerator,
        ProductFixtureGenerator productFixtureGenerator
    ) {
        this.menuGroupFixtureGenerator = menuGroupFixtureGenerator;
        this.productFixtureGenerator = productFixtureGenerator;
    }

    private static final String MENU_API_URL_TEMPLATE = "/api/menus";

    @Test
    @DisplayName("메뉴 목록을 조회 한다.")
    public void getMenus() throws Exception {
        // When
        ResultActions resultActions = mockMvcUtil.get(MENU_API_URL_TEMPLATE);

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].id").exists())
            .andExpect(jsonPath("$.[*].name").exists())
            .andExpect(jsonPath("$.[*].price").exists())
            .andExpect(jsonPath("$.[*].menuGroupId").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].seq").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].menuId").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].productId").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].quantity").exists())
        ;
    }

    @Test
    @DisplayName("메뉴를 추가한다.")
    public void createMenu() throws Exception {
        // Given
        final int generateProductsCount = 2;
        final MenuGroup savedMenuGroup = menuGroupFixtureGenerator.savedMenuGroup();
        final List<Product> savedProducts = productFixtureGenerator.savedProducts(generateProductsCount);
        final Menu menu = generateMenu(savedMenuGroup, savedProducts);

        // When
        ResultActions resultActions = mockMvcUtil.post(MENU_API_URL_TEMPLATE, menu);

        // Then
        Menu createMenuResponse = (Menu) mockMvcUtil.as(resultActions, Menu.class);

        List<Long> givenProductIds = savedProducts.stream()
            .map(Product::getId)
            .collect(Collectors.toList());

        assertThat(createMenuResponse.getMenuProducts())
            .extracting(MenuProduct::getProductId)
            .containsExactlyInAnyOrderElementsOf(givenProductIds);

        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.menuGroupId").value(savedMenuGroup.getId()))
            .andExpect(jsonPath("$.menuProducts[*].seq").exists())
            .andExpect(jsonPath("$.menuProducts[*].menuId").exists())
            .andExpect(jsonPath("$.menuProducts[*].productId").exists())
            .andExpect(jsonPath("$.menuProducts[*].quantity").exists())
        ;
    }
}
