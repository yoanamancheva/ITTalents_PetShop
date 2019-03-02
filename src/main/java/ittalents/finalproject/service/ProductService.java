package ittalents.finalproject.service;

import ittalents.finalproject.model.pojos.dto.ProductReviewsDTO;
import ittalents.finalproject.model.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    public List<ProductReviewsDTO> getReviewsForAllProducts() {
        return productRepository.findAll().stream().map(product ->
                new ProductReviewsDTO(product.getId(), product.getName(), product.getCategory(),
                        product.getPrice(), product.getQuantity(),
                        product.getManifacturer(), product.getReviews()))
                .collect(Collectors.toList());
    }
}
