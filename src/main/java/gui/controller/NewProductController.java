package gui.controller;

import dao.ProductDAO;
import javafx.event.ActionEvent;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@NoArgsConstructor
@Controller
public class NewProductController {
    
    private ProductDAO productDAO;
    
    @Autowired
    public NewProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void saveProduct(ActionEvent actionEvent) {
    }
}
