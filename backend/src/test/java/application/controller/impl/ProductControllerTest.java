package application.controller.impl;

import application.model.Product;
import application.service.ProductRepository;
import application.utils.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    private ProductController controller;

    @Autowired
    private MockMvc mockMvc;

    private ProductRepository repository;

    @Before
    public void setUp() {
        repository = mock(ProductRepository.class);
        controller = new ProductController(repository);
    }

    @Test
    public void getAllProducts_whenSuccess_thenAListOfProductsReturned()
            throws Exception {
        //Arrange
        List<Product> expectedProductList = new ArrayList<>();
        expectedProductList.add( new Product("Beans", 4.0));
        when(repository.findAll()).thenReturn(expectedProductList);

        //Act
        List<Product> resultProductsList = controller.getAllProducts();

        //Assert
        assertEquals(expectedProductList.toString(), resultProductsList.toString());
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Beans"));
    }

    @Test
    public void newProduct_whenSuccess_createdNewProduct()
            throws Exception {
        //Arrange
        Product expectedProduct = new Product("Beans", 4.0);
        when(repository.save(expectedProduct)).thenReturn(expectedProduct);

        //Act
        Product resultProduct = controller.newProduct(expectedProduct);

        //Assert
        assertEquals(expectedProduct.toString(), resultProduct.toString());
    }

    @Test
    public void getSingleProduct_whenSuccess_thenAProductIsReturned()
            throws Exception {
        //Arrange
        Long id = 1L;
        Product expectedProduct = new Product("Beans", 4.0);
        when(repository.findById(id)).thenReturn(java.util.Optional.of(expectedProduct));

        //Act
        Product resultProduct = controller.getSingleProduct(id);

        //Assert
        assertEquals(expectedProduct.toString(), resultProduct.toString());
    }

    @Test(expected = ProductNotFoundException.class)
    public void getSingleProduct_whenWrongId_thenAErrorIsThrown()
            throws Exception {
        //Arrange
        Long id = 3L;
        when(repository.findById(id)).thenThrow(new ProductNotFoundException(id));

        //Act & Assert
        controller.getSingleProduct(id);
    }

    @Test
    public void updateProduct_whenSuccess_thenAProductIsReturned()
            throws Exception {
        //Arrange
        Long id = 1L;
        Product expectedProduct = new Product("Beans", 4.0);
        when(repository.findById(id)).thenReturn(java.util.Optional.of(expectedProduct));
        expectedProduct.setName("Not Beans");
        expectedProduct.setPrice(5.0);
        when(repository.save(expectedProduct)).thenReturn(expectedProduct);
        //Act
        Product resultProduct = controller.updateProduct(new Product("Beans", 4.0), id);

        //Assert
        assertEquals(expectedProduct.toString(), resultProduct.toString());

    }

    @Test(expected = ProductNotFoundException.class)
    public void updateProduct_whenWrongId_thenAErrorIsThrown()
            throws Exception {
        //Arrange
        Long id = 2L;
        Product expectedProduct = new Product("Beans", 4.0);
        when(repository.findById(id)).thenThrow(new ProductNotFoundException(id));
        //Act & Assert
        controller.updateProduct(expectedProduct, id);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteProduct_whenWrongId_thenAErrorIsThrown()
            throws Exception {
        //Arrange
        Long id = 1L;
        Product expectedProduct = new Product("Beans", 4.0);
        doAnswer(invocation -> { throw new EmptyResultDataAccessException(String.format("No entity with id %s exists!", id), 1); })
            .when(repository).deleteById(id);
        //Act & Assert
        controller.deleteProduct(id);

    }

}
