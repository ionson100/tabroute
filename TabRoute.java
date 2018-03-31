package sample.tabroute;


import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

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
            // если это кнопка то по енеру нажимаем
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
