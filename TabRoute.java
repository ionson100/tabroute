package sample.tabroute;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TabRoute {

    boolean run = true;
    int focusIndexItem = 0;
    List<Node> nodeList = new ArrayList<>();


    public void addNode(Node node) {

        nodeList.add(node);
        node.setOnKeyPressed(event -> {
            // отлавливаем клик таб
            if (event.getCode() == KeyCode.TAB) {
                if (TabRoute.this.focusIndexItem < TabRoute.this.nodeList.size() - 1) {

                    // пропускаем скрытые и не активне контролы
                    for (int i = TabRoute.this.focusIndexItem + 1; i < TabRoute.this.nodeList.size(); i++) {
                        Node n = TabRoute.this.nodeList.get(i);
                        if (n.isVisible() == true && n.isDisabled() == false) {
                            focusIndexItem = i;
                            System.out.println(i);
                            Platform.runLater(() -> n.requestFocus());
                            break;
                        }
                    }
                } else {
                    // закольцовываем переходы если подошли к концу списка
                    focusIndexItem = 0;
                    Platform.runLater(() -> TabRoute.this.nodeList.get(TabRoute.this.focusIndexItem).requestFocus());
                }
            }
            // если это кнопка то по ENTER нажимаем
            if (event.getCode() == KeyCode.ENTER && event.getSource() instanceof Button) {
                ((Button) event.getSource()).fire();
            }

        });
        int e = nodeList.size();
        // если контрол в списке помечен как принимающий фокус, начинаем путешествовать с него, срабатывает один раз
        node.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (run) {
                if (newPropertyValue) {
                    TabRoute.this.focusIndexItem = e - 1;
                    run = false;
                }
            }
        });
        // если мышкой встали на контрол, перемещаем фокус в него. в дальнейшем путешествуем  отсюда
        node.setOnMouseClicked(event -> TabRoute.this.focusIndexItem = e - 1);
    }
}

public class Controller implements Initializable {

    TabRoute tabRoute;
    public Button bt2;
    public Button bt1;
    public TextField tf1;
    public TextField tf2;
    public TextField tf3;
    public TextField tf4;
    public TextField tf5;
    public TextField tf6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        bt2.setOnAction(event -> Platform.exit());

        bt1.setOnAction(event -> Platform.runLater(() -> tf3.requestFocus()));

        // использование: Создаем Объект TabRoute
        // и добавляем в него контролы, фокус перехода  по умолчанию от начала с писка в конец,
        // если котрол помечен fоcusable переходы начинаются с него

        Platform.runLater(() -> tf3.requestFocus());
        tabRoute = new TabRoute();
        tabRoute.addNode(tf1);
        tabRoute.addNode(tf2);
        tabRoute.addNode(tf3);
        tabRoute.addNode(tf4);
        tabRoute.addNode(tf5);
        tabRoute.addNode(tf6);
        tabRoute.addNode(bt1);
        tabRoute.addNode(bt2);

    }
}

