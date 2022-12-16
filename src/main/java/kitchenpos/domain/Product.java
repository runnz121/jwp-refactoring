package kitchenpos.domain;

import static javax.persistence.GenerationType.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private Name name;
    @Embedded
    private Price price;

    public Product() {
    }

    public Product(Name name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

}
