/*
 * Ce fichier est le point d'entrée principal pour l'interface graphique du jeu. Il initialise
 * la fenêtre de jeu, charger les scènes nécessaires, et gérer les transitions entre les
 * différentes vues (comme le menu principal, le jeu lui-même, etc.).
 */

package gui;

import enums.FxmlFiles;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import config.MazeConfig;
import model.MazeState;
import model.PacMan;

import java.io.IOException;
import java.util.Objects;

// Classe principale de l'application qui étend javafx.application.Application.
// Cette classe est le point d'entrée pour l'interface utilisateur du jeu.

public class App extends Application {
    // Attributs et champs de la classe
    public static Rectangle2D screen = Screen.getPrimary().getBounds(); // Les dimensions de l'écran du joueur ( utile si on a envie de mettre le jeu en plein écran )\
     static Stage primaryStage = null;

     public static Scene gameScene ;
      public static ImageView imageView ;
    SceneManager menu ;

    //private Scenes current ; // Qui permet d'afficher les scènes dans le stage et de switcher entre les pages
    // Changer l'implementation du menu au lieu de Vbox on utilisera des fichiers FXML

    // La méthode start est appelée automatiquement lorsque l'application démarre.
    @Override
    public void start(Stage primaryStage) {
        App.primaryStage = primaryStage; // Initialisation du stage

        // Configuration de la fenêtre
        primaryStage.getIcons().clear();
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Logo.png")))); 
        // Initialisation de la fenêtre principale et configuration de ses propriétés.
        primaryStage.setTitle("JUNGLE PACMAN");
        primaryStage.setResizable(false);

        // Création de la scène de démarrage
        Canvas zoneDesign = new Canvas(875, 600);
        Scene gameScene = new Scene(new AnchorPane(zoneDesign));
        this.gameScene = gameScene ;
        /* AnchorPane est un type spécifique de Pane qui utilise un système de positionnement basé sur les ancres (anchors).
            Il permet de positionner les éléments enfants. */


        // Load and display an animated GIF qui sera temporairement affiché avant le début du jeu
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/JunglePacman.gif")))); //GIF animation d'entrée dans le jeu
        this.imageView = imageView ;
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);
        AnchorPane.setBottomAnchor(imageView, 0.0);
        AnchorPane anchorPane = (AnchorPane) gameScene.getRoot();
        anchorPane.getChildren().add(imageView);

        primaryStage.setScene(gameScene); // Définition de la scène initiale dans la fenêtre principale.
        primaryStage.setAlwaysOnTop(true);
        primaryStage.requestFocus();
        primaryStage.show();// Affichage de la fenêtre principale.

        primaryStage.setScene(gameScene); // On ajoute la scène au stage
        primaryStage.sizeToScene(); // On adapte la taille de la fenêtre à la scène
        primaryStage.setAlwaysOnTop(false);
        primaryStage.requestFocus();// On met le focus sur la fenêtre
        primaryStage.show(); // On affiche la fenêtre

        PauseTransition delay = new PauseTransition(Duration.millis(3000)); // On qffiche temporairement le gif JunglePacman
        delay.setOnFinished(event -> {
            //Appel vers le premier fichier fxml : Menu.fxml
            this.menu = new SceneManager(primaryStage);
            this.menu.callInitialScene(FxmlFiles.MENU);
        });
        // Jouer l'animation de démarrage et passer ensuite au menu principal.
        delay.play();
    }

    // Méthode pour configurer les actions des boutons du menu.
    private void configureMenuButtons(Menu menu) {
        // Bouton pour démarrer un nouveau jeu
        menu.getJouerButton().setOnAction(e -> {
            try {
                var root = new Pane();
                var gameScenes = new Scene(root);
                var pacmanController = new PacmanController(PacMan.INSTANCE);
                gameScenes.setOnKeyPressed(pacmanController::keyPressedHandler);
                gameScenes.setOnKeyReleased(pacmanController::keyReleasedHandler);
                // Utiliser le constructeur de MazeConfig avec le chemin du fichier
                var mazeConfigs = new MazeConfig("mazeConfig.txt");
                var maze = new MazeState(mazeConfigs);
                var gameView = new GameView(maze, root, 70.0);
                
                // Suite de la configuration de la scène
                primaryStage.setScene(gameScenes);
                primaryStage.show();
                gameView.animate();
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Affichage de l'erreur dans la console
                // Gestion de l'exception IOException
            }
        });
    
        // Bouton pour ouvrir les paramètres
        menu.getParametresButton().setOnAction(e -> {
            ParametresMenu parametresMenu = new ParametresMenu(menu);
            primaryStage.setScene(new Scene(parametresMenu, 875, 600)); // Affiche la page des paramètres
            primaryStage.show();
        });
    
        // Bouton pour quitter l'application
        menu.getQuitterButton().setOnAction(e -> System.exit(0));
    }
    

        
    }
}
