package com.roozbeh.webcrawler.controller;

import com.roozbeh.webcrawler.dal.entity.Product;
import com.roozbeh.webcrawler.model.ProductModel;
import com.roozbeh.webcrawler.model.UrlModel;
import com.roozbeh.webcrawler.service.Aggregator;
import com.roozbeh.webcrawler.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rezaeian
 */
@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Slf4j
public class ProductController {

    private ProductService productService;
    private Aggregator aggregator;

    @PostMapping("/")
    @ApiOperation(value = "Persist Product in to DB")
    public ResponseEntity<Product> saveProduct(@RequestBody ProductModel product) {
        Product saved = productService.findOrSave(product);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/url")
    @ApiOperation(value = "Fetch Product Detail by Its Url")
    public ProductModel fetchProductDetailsByItsUrl(@RequestBody UrlModel urlModel) throws IOException {
        return aggregator.fetchProductDetails(urlModel.getUrl());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Fetch Product Detail by given Id")
    public Product fetchProductById(@PathVariable String id){
        return productService.findProductById(id);
    }

    @GetMapping("/url")
    @ApiOperation(value = "Fetch All Product Urls")
    public List<UrlModel> findAllProductUrls() throws IOException {
        return aggregator.findAllProductUrls().stream().map(url -> new UrlModel(url)).collect(Collectors.toList());
    }
}
