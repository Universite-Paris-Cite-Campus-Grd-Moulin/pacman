package gui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Menu extends VBox{

    private Button jouer;
    private Button parametres;
    private Button quitter;



    public Menu(){
        jouer = new Button("Jouer");
        parametres =new  Button ("parametres");
        quitter = new Button ("Quitter");

        /*jouer.setOnAction(e ->{
            // que faut-il actionner quand on appui sur jouer ?
        });

        parametres.setOnAction(e ->{
            // que faut-il actionner quand on appui sur jouer ?
        });


         */

        getChildren().addAll(jouer,parametres,quitter);
    }

    public Button getJouerButton() {
        return jouer;
    }

    public Button getQuitterButton(){
        return quitter;
    }



}