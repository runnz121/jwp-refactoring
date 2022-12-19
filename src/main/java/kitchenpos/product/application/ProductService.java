package kitchenpos.product.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = productRepository.save(request.toEntity());
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return ProductResponse.toList(productRepository.findAll());
    }

    public List<MenuProduct> findMenuProducts(List<MenuProductRequest> menuProducts) {
        List<MenuProduct> menuProductList = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProducts) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            menuProductList.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProductList;
    }
}
