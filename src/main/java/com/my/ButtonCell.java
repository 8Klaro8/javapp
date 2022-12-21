package com.my;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Pair;

// class ButtonCell extends ListCell<String> {

public class ButtonCell {

    ComboBox<Pair<String, Image>> comboBox = new ComboBox<>(); 
    
    public ComboBox<Pair<String, Image>> getComboBox() {

        comboBox.setCellFactory(new Callback<ListView<Pair<String, Image>>, ListCell<Pair<String, Image>>>() {
            @Override
            public ListCell<Pair<String, Image>> call(ListView<Pair<String, Image>> param) {
                return new ListCell<Pair<String, Image>>() {
                    private final ImageView imageView = new ImageView();
        
                    @Override
                    protected void updateItem(Pair<String, Image> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            imageView.setImage(item.getValue());
                            imageView.setFitHeight(60);
                            imageView.setFitWidth(60);
                            setText(null); // <-- set text to null to hide the text
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        comboBox.setButtonCell(new ListCell<Pair<String, Image>>() {
            private final ImageView imageView = new ImageView();
        
            @Override
            protected void updateItem(Pair<String, Image> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(item.getValue());
                    imageView.setFitHeight(60);
                    imageView.setFitWidth(60);
                    setText(null); // <-- set text to null to hide the text
                    setGraphic(imageView);
                }
            }
        });

        return comboBox;





    //     comboBox.setCellFactory(new Callback<ListView<Image>, ListCell<Image>>() {
    //         @Override
    //         public ListCell<Image> call(ListView<Image> param) {
    //             return new ListCell<Image>() {
    //                 private final ImageView imageView = new ImageView();
        
    //                 @Override
    //                 protected void updateItem(Image item, boolean empty) {
    //                     super.updateItem(item, empty);
    //                     if (empty || item == null) {
    //                         setText(null);
    //                         setGraphic(null);
    //                     } else {
    //                         imageView.setImage(item);
    //                         imageView.setFitHeight(60);
    //                         imageView.setFitWidth(60);
    //                         setText(null);
    //                         setGraphic(imageView);
    //                     }
    //                 }
    //             };
    //         }
    //     });

    //     // sets the default value of the combobox to the chosen icon
    //     comboBox.setButtonCell(new ListCell<Image>() {
    //         private final ImageView imageView = new ImageView();
        
    //         @Override
    //         protected void updateItem(Image item, boolean empty) {
    //             super.updateItem(item, empty);
    //             if (empty || item == null) {
    //                 setText(null);
    //                 setGraphic(null);
    //             } else {
    //                 imageView.setImage(item);
    //                 imageView.setFitHeight(60);
    //                 imageView.setFitWidth(60);
    //                 setText(null);
    //                 setGraphic(imageView);
    //             }
    //         }
    //     });

    //     return comboBox;
    }
}