import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.Person;
import model.SampleData;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class PersonController implements Initializable {

    @FXML private MenuItem exitMenu;
    @FXML private MenuItem aboutMenu;
    @FXML private Button addNewUserButton;
    @FXML private Button deleteButton;
    @FXML private AnchorPane formAnchorPane;
    @FXML private ListView<Person> listView;
    @FXML private TextField firstnameTextField;
    @FXML private TextField lastnameTextField;
    @FXML private TextArea notesTextArea;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private AnchorPane viewAnchorPane;
    @FXML private Circle imgDisplay;
    @FXML private Label fullNameLabel;
    @FXML private ImageView editButton;
    @FXML private Button updateButton;
    @FXML private Button addButton;
    @FXML private TextField firstNameViewTF;
    @FXML private TextField lastNameViewTF;
    @FXML private TextField birthdateVIewTF;
    @FXML private TextField genderViewTF;

    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    private final ObservableList<Person> personList = FXCollections.observableArrayList(Person.extractor);
    // Observable objects returned by extractor (applied to each list element) are listened for changes and
    // transformed into "update" change of ListChangeListener.

    private Person selectedPerson;
    private final BooleanProperty modifiedProperty = new SimpleBooleanProperty(false);
    private ChangeListener<Person> personChangeListener;

    ObservableList<String> genderCombo = FXCollections.observableArrayList("Female", "Male", "Prefer not to mention");


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Disable the Remove/Edit buttons if nothing is selected in the ListView control
        deleteButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        addNewUserButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());

        // if no item is selected
        addButton.setDisable(false);
        updateButton.setDisable(true);

        //addButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull()
                //.or (firstnameTextField.textProperty().isEmpty().or(lastnameTextField.textProperty().isEmpty())));

        SampleData.fillSampleData(personList);

        // Use a sorted list; sort by lastname; then by firstname
        SortedList<Person> sortedList = new SortedList<>(personList);

        // sort by lastname first, then by firstname; ignore notes
        sortedList.setComparator((p1, p2) -> {
            int result = p1.getFirstname().compareToIgnoreCase(p2.getFirstname());
            if (result == 0) {
                result = p1.getLastname().compareToIgnoreCase(p2.getLastname());
            }
            return result;
        });

        listView.setItems(sortedList);

        listView.getSelectionModel().selectedItemProperty().addListener(personChangeListener = (observable, oldValue, newValue) -> {
            System.out.println("Selected item: " + newValue);
            // newValue can be null if nothing is selected
            selectedPerson = newValue;

            // Boolean property modifiedProperty tracks whether the user has changed any of the
            //three text controls in the form. We reset this flag after each ListView selection and use
            //this property in a bind expression to control the Update button’s disable property.
            modifiedProperty.set(false);


            if (newValue != null) {
                // Populate controls with selected Person
                formAnchorPane.setVisible(false);
                viewAnchorPane.setVisible(true);
                fullNameLabel.setText(selectedPerson.getFirstname() + " " + selectedPerson.getLastname());
                firstNameViewTF.setText(selectedPerson.getFirstname());
                lastNameViewTF.setText(selectedPerson.getLastname());
                birthdateVIewTF.setText(selectedPerson.getDateOfBirth().format(formatters));
                genderViewTF.setText(selectedPerson.getGender());
                Image img = new Image(selectedPerson.getImage(),false);
                imgDisplay.setFill(new ImagePattern(img));

            } else {
                formAnchorPane.setVisible(true);
                viewAnchorPane.setVisible(false);
                firstnameTextField.setText("");
                lastnameTextField.setText("");
                datePicker.setValue(null);
                genderComboBox.setValue("");
            }
        });

        // Pre-select the first item
        listView.getSelectionModel().selectFirst();
        genderComboBox.setItems(genderCombo);

    }

    @FXML
    void handleKeyAction(KeyEvent event) {
        modifiedProperty.set(true);
    }

    @FXML
    void handleNewUpdate(ActionEvent event) {
        modifiedProperty.set(true);
    }

    @FXML
    void onClickAbout(ActionEvent event) {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION,"AlyNa Rahim");
        informationAlert.setTitle("About the App");
        informationAlert.setHeaderText("About the app - Copyrights");
        informationAlert.setContentText("Created on: 19-March-2021\n" +
                "By: AlyNa Rahim\n" +
                "Contact: +92 3352992280\n" +
                "Email: alyna.rahim_2021@ucentralasia.org");
        informationAlert.showAndWait();
    }



    @FXML
    void onClickEdit(MouseEvent event) {
        formAnchorPane.setVisible(true);
        viewAnchorPane.setVisible(false);
        updateButton.setDisable(false);
        addButton.setDisable(true);

        firstnameTextField.setText(selectedPerson.getFirstname());
        lastnameTextField.setText(selectedPerson.getLastname());
        datePicker.setValue(selectedPerson.getDateOfBirth());
        genderComboBox.setValue(selectedPerson.getGender());

    }

    @FXML
    void onClickNewUser(ActionEvent event) {
        formAnchorPane.setVisible(true);
        viewAnchorPane.setVisible(false);
        updateButton.setDisable(true);
        addButton.setDisable(false);

        firstnameTextField.setText("");
        lastnameTextField.setText("");
        datePicker.setValue(null);
        genderComboBox.setValue("");
    }

    @FXML
    void onClickAdd(ActionEvent event) {
        Person person = new Person(firstnameTextField.getText(), lastnameTextField.getText(), notesTextArea.getText(),
                datePicker.getValue(), genderComboBox.getValue());
        if(person.getFirstname().isEmpty() || person.getLastname().isEmpty()){
            Alert emptyAlert = new Alert(Alert.AlertType.INFORMATION);
            emptyAlert.setTitle("Warning");
            emptyAlert.setHeaderText("Blank Entry");
            emptyAlert.setContentText("Please enter valid first and last name");
            emptyAlert.showAndWait();
        } else {
            personList.add(person);
        }
    }

    @FXML
    void onClickUpdate(ActionEvent event) {
        System.out.println("Update " + selectedPerson);
        Person person = listView.getSelectionModel().getSelectedItem();
        listView.getSelectionModel().selectedItemProperty().removeListener(personChangeListener);
        person.setFirstname(firstnameTextField.getText());
        person.setLastname(lastnameTextField.getText());
        person.setNotes(notesTextArea.getText());
        person.setDateOfBirth(datePicker.getValue());
        person.setGender(genderComboBox.getValue());

        listView.getSelectionModel().selectedItemProperty().addListener(personChangeListener);
        modifiedProperty.set(false);

        formAnchorPane.setVisible(false);
        viewAnchorPane.setVisible(true);
        fullNameLabel.setText(selectedPerson.getFirstname() + " " + selectedPerson.getLastname());
        firstNameViewTF.setText(selectedPerson.getFirstname());
        lastNameViewTF.setText(selectedPerson.getLastname());
        birthdateVIewTF.setText(selectedPerson.getDateOfBirth().format(formatters));
        genderViewTF.setText(selectedPerson.getGender());
    }

    @FXML
    void onClickDelete(ActionEvent event) {
        //TODO update database

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you sure you want to delete this item?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.out.println("Remove " + selectedPerson);
            personList.remove(selectedPerson);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    void onClickExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

}
