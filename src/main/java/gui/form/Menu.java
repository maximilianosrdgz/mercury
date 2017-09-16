package gui.form;

import dao.ClientDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

public class Menu extends Application {

    //private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/
        /*
        ClassPathXmlApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("Application Context loaded");
        //ClientDAO dao = (ClientDAO) ctx.getBean("clientDAO");
        //System.out.println(dao);
        String[] beans = ctx.getBeanDefinitionNames();
        for (int i = 0; i < beans.length; i++) {
            System.out.println(beans[i]);
        }*/
        // load main form in to VBox (Root)
        //VBox mainPane = (VBox) FXMLLoader.load(getClass().getResource("/menu.fxml" ));
        VBox mainPane = (VBox) SpringFxmlLoader.load("/menu.fxml");
        // add main form into the scene
        Scene scene = new Scene(mainPane);

        primaryStage.setTitle("Mercury");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);    // make the main form fit to the screen
        primaryStage.show();
    }
}
