package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.springframework.test.util.ReflectionTestUtils;

public class MenuProductFixture {

    public static MenuProduct 메뉴상품(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct(menu, product, quantity);
        ReflectionTestUtils.setField(menuProduct, "seq", seq);
        return menuProduct;
    }

}
