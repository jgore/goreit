package pl.goreit.blog.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import pl.goreit.api.generated.CreateOrderRequest;
import pl.goreit.api.generated.OrderLineRequest;
import pl.goreit.api.generated.OrderResponse;
import pl.goreit.blog.domain.CategoryName;
import pl.goreit.blog.domain.DomainException;
import pl.goreit.blog.domain.model.Category;
import pl.goreit.blog.domain.model.Product;
import pl.goreit.blog.domain.service.OrderService;
import pl.goreit.blog.infrastructure.mongo.CategoryRepo;
import pl.goreit.blog.infrastructure.mongo.ProductRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
@Profile("!prod")
public class TestHelperController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private OrderService orderService;

    @PostMapping("addOrder")
    @ApiOperation(value = "add order")
    public OrderResponse addOrder(@RequestParam("userId") String userId,
                                  @RequestParam("orderProductNumber") Integer orderProductNumber,
                                  @RequestParam("amount") Integer amount) throws DomainException {

        Product korepetycje0 = productRepo.findByTitle("korepetycje").get();
        List<OrderLineRequest> orderLineRequests = new ArrayList<>();

        for (int i = 0; i < orderProductNumber; i++) {
            OrderLineRequest orderProductView = new OrderLineRequest(korepetycje0.getTitle(), amount);
            orderLineRequests.add(orderProductView);
        }

        return orderService.create(new CreateOrderRequest(userId, "admin", true, true, orderLineRequests));
    }


    @GetMapping("/addProduct/")
    @ApiOperation(value = "add 100 products")
    public void addProducts(@RequestParam("amount") Integer amount) {

        for (int count = 0; count < amount; count++) {
            Product product = new Product(CategoryName.IT, "1234", "korepetycje", "Pomoc w programowaniu", BigDecimal.valueOf(150), 10,null);
            productRepo.save(product);
        }


    }

    @GetMapping("/addCategories/")
    @ApiOperation(value = "add ALL categories")
    public void addCategories() {

        List<CategoryName> categoryNames = Arrays.asList(CategoryName.values());

        categoryNames.forEach(categoryName -> {
            Category category = new Category(categoryName.name());
            categoryRepo.save(category);
        });

    }


}
