package kitchenpos.utils.fixture;

import kitchenpos.product.domain.Product;

public class ProductFixtureFactory {
    public static Product createProduct(String name, int price) {
        return new Product(name, price);
    }
}
