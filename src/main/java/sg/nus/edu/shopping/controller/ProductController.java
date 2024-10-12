package sg.nus.edu.shopping.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sg.nus.edu.shopping.interfacemethods.CategoryInterface;
import sg.nus.edu.shopping.interfacemethods.ProductInterface;
import sg.nus.edu.shopping.model.Category;
import sg.nus.edu.shopping.model.Product;
import sg.nus.edu.shopping.repository.ProductImageRepository;
import sg.nus.edu.shopping.service.CategoryImplementation;
import sg.nus.edu.shopping.service.ProductImplementation;

import java.util.Arrays;
import sg.nus.edu.shopping.repository.ProductRepository;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController{

    @Autowired
    private ProductInterface productInt;

    @Autowired
    private CategoryInterface categoryInt;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public void setProductInterface(ProductImplementation productImp) {
        this.productInt = productImp;
    }

    @Autowired
    public void setCategoryInterface(CategoryImplementation categoryImp) {
        this.categoryInt = categoryImp;
    }

    @GetMapping("/products")
    public String showProductsOnlyView(@RequestParam(value = "category", required = false) Integer categoryId,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Pageable pageable1 = PageRequest.of(page - 1, size);
        Page<Product> productPage;
        if (categoryId != null) {
            Category category = categoryInt.findByCategoryId(categoryId);
            productPage = productInt.getProductByCategory(category, pageable1);
            model.addAttribute("categoryId", categoryId); // 添加 categoryId 到模型
        } else {
            productPage = productInt.getProducts(pageable1);
            model.addAttribute("pageName", "mainPage"); // 添加 pageName 属性
        }

        if (page == 1 && "mainPage".equals(model.getAttribute("pageName"))) {
            List<Integer> hotImageIds = Arrays.asList(20, 30, 50);
            List<String> hotProductImages = productImageRepository.findFilenamesByIds(hotImageIds);
            model.addAttribute("hotProducts", hotProductImages);  // 热销商品图片
        }

        model.addAttribute("products", productPage.getContent()); // 当前页的产品
        model.addAttribute("currentPage", page); // 当前页码
        int totalPages = productPage.getTotalPages() > 0 ? productPage.getTotalPages() : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", productPage.getTotalElements()); // 总产品数量

        return "homePage";
    }

    @GetMapping("/7haven/products")
    public String showProducts(@RequestParam(value = "category", required = false) Integer categoryId,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               HttpSession sessionObj,
                               Model model) {
        String username = (String) sessionObj.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage;
        if (categoryId != null) {
            Category category = categoryInt.findByCategoryId(categoryId);
            productPage = productInt.getProductByCategory(category, pageable);
            model.addAttribute("categoryId", categoryId); // 添加 categoryId 到模型
        } else {
            productPage = productInt.getProducts(pageable);
            model.addAttribute("pageName", "mainPage"); // 添加 pageName 属性
        }

        if (page == 1 && "mainPage".equals(model.getAttribute("pageName"))) {
            List<Integer> hotImageIds = Arrays.asList(20, 30, 50);
            List<String> hotProductImages = productImageRepository.findFilenamesByIds(hotImageIds);
            model.addAttribute("hotProducts", hotProductImages);  // 热销商品图片
        }

        model.addAttribute("products", productPage.getContent()); // 当前页的产品
        model.addAttribute("currentPage", page); // 当前页码
        int totalPages = productPage.getTotalPages() > 0 ? productPage.getTotalPages() : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", productPage.getTotalElements()); // 总产品数量

        return "homePage";
    }


    @GetMapping("/7haven/products/search")
    public String searchProducts(@RequestParam("keyword") String keyword,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productRepository.searchProductsByKeyword(keyword, pageable);
        List<Product> products = productInt.searchProductsByKeyword(keyword);
        if (products.isEmpty()) {
            model.addAttribute("message", "No products found");
        }
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        int totalPages = productPage.getTotalPages() > 0 ? productPage.getTotalPages() : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "homePage";
    }

    @GetMapping("/7haven/cart")
    public String cart() {
        return "cartPage"; // 假设你有一个名为 cartPage.html 的模板
    }

    @GetMapping("/7haven/product/{id}")
    public String showProductDetails(@PathVariable("id") int productId, Model model) {
        Optional<Product> optProduct = productInt.findByProductId(productId);
        if (optProduct.isEmpty()) {
            model.addAttribute("errorMessage", "Product not found");
            return "errorPage"; // 假设有一个名为 errorPage.html 的模板
        }
        Product product = optProduct.get();
        model.addAttribute("product", product);
        model.addAttribute("coverImage", product.getCoverImagePath()); // For cover image
        model.addAttribute("additionalImages", product.getAdditionalImages()); // For additional images
        return "productDetails"; // 假设有一个名为 productDetails.html 的模板
    }

    @GetMapping("/7haven/contact")
    public String contact() {
        return "contactPage"; // Ensure you have a contactPage.html template
    }

    @GetMapping("/7haven/about")
    public String about() {
        return "aboutPage"; // Ensure you have an aboutPage.html template
    }

    @GetMapping("/7haven/delivery")
    public String delivery() {
        return "deliveryPage"; // Ensure you have a deliveryPage.html template
    }

    @GetMapping("/7haven/returns")
    public String returns() {
        return "returnsPage"; // Ensure you have a returnsPage.html template
    }

    @GetMapping("/7haven/faqs")
    public String faqs() {
        return "faqsPage"; // Ensure you have a faqsPage.html template
    }

        @Autowired
        private ProductRepository productrepository;
        @RequestMapping("/product")
        public String getProduct(Model model) {
            model.addAttribute("product",productrepository.findAll());
            return"purchesRecord";
        }





}
