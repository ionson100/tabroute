package sample.tabroute;


import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

public class TabRoute {

    // интерфей, при помощинего можем передать поведение для контрола при нажиме ентер,если контрол находится в фокусе
    public interface IAction{
        void action(Node node);
    }


   private boolean run = true;
   private int focusIndexItem = 0;
   private List<Node> nodeList = new ArrayList<>();
   private Map<Node,IAction> iActionMap=new HashMap<>();
   private Node currnode;


    public void addNode(Node node) {

        if(node==null) return;
        nodeList.add(node);
        node.setOnKeyPressed(event -> {
            // отлавливаем клик таб
            if (event.getCode() == KeyCode.TAB) {
                if (TabRoute.this.focusIndexItem < TabRoute.this.nodeList.size() - 1) {

                    // пропускаем скрытые и не активне контролы
                    for (int i = TabRoute.this.focusIndexItem + 1; i < TabRoute.this.nodeList.size(); i++) {
                        currnode = TabRoute.this.nodeList.get(i);
                        if (currnode.isVisible() == true && currnode.isDisabled() == false) {
                            focusIndexItem = i;
                            System.out.println(i);
                            Platform.runLater(() -> currnode.requestFocus());
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
            if (event.getCode() == KeyCode.ENTER ) {
                if(event.getSource() instanceof Button)
                    ((Button) event.getSource()).fire();
                if(currnode!=null&&iActionMap.containsKey(currnode)){
                    iActionMap.get(currnode).action(currnode);
                }
            }


        });
        int e = nodeList.size();
        // если контрол в списке помечен как принимающий фокус, начинаем путешествовать с него, срабатывает один раз
        node.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
              if (run) {
            if (newPropertyValue) {
                TabRoute.this.focusIndexItem = e - 1;
                 currnode=node;
                  run = false;
            }
             }
        });
        // если мышкой встали на контрол, перемещаем фокус в него. в дальнейшем путешествуем  отсюда
        node.setOnMouseClicked(event -> {
            TabRoute.this.focusIndexItem = e - 1;
            currnode=node;
        });
    }


    public void addNode(Node  node,IAction iAction){
        if(node==null) return;
        if(iAction!=null){
            iActionMap.put(node,iAction);
        }
        addNode(node);
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
    public GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bt2.setOnAction(event -> Platform.exit());
        bt1.setOnAction(event -> Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tf1.requestFocus();
            }
        }));
        // использование: Создаем Объект TabRoute
        // и добавляем в него контролы, фокус перехода  по умолчанию от начала с писка в конец,
        // если котрол помечен fоcusable переходы начинаются с него

        Platform.runLater(() -> tf2.requestFocus());
        tabRoute = new TabRoute();
        tabRoute.addNode(tf1);
        tabRoute.addNode(tf2, node -> bt2.fire());
        tabRoute.addNode(tf3);
        tabRoute.addNode(tf4);
        tabRoute.addNode(tf5);
        tabRoute.addNode(tf6);
        tabRoute.addNode(bt1);
        tabRoute.addNode(bt2);

    }
}


