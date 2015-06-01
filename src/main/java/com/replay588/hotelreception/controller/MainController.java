package com.replay588.hotelreception.controller;


import com.replay588.hotelreception.entity.Customer;
import com.replay588.hotelreception.entity.HotelRoom;
import com.replay588.hotelreception.entity.Reservation;
import com.replay588.hotelreception.util.AlertDialogs;
import com.replay588.hotelreception.util.comparators.CustomerNameComparatorASC;
import com.replay588.hotelreception.util.comparators.CustomerNameComparatorDESC;
import com.replay588.hotelreception.util.hib.ReservationDaoImpl;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main controller for app
 * @author r588
 */
public class MainController implements Initializable{
    /**
     * Reference to components in main_scene.fxml
     */
    @FXML
    private TextField nameTF;
    @FXML
    private TextField roomNumberTF;
    @FXML
    private DatePicker checkInDP;
    @FXML
    private DatePicker checkOutDP;
    @FXML
    private Button reserveBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button hotelRoomBtn;
    @FXML
    private Button calculateBtn;
    @FXML
    private ComboBox<Integer> numOfAdultsComBox;
    @FXML
    private ComboBox<Integer> numOfChildrenComBox;
    @FXML
    private CheckBox approvedChB;
    @FXML
    private Label priceLabel;
    //Reservation Table
    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, IntegerProperty> reservationID;
    @FXML
    private TableColumn<Reservation, StringProperty> reservationCustomer;
    @FXML
    private TableColumn<Reservation, ObjectProperty<LocalDate>> reservationCheckIn;
    @FXML
    private TableColumn<Reservation, ObjectProperty<LocalDate>> reservationCheckOut;
    @FXML
    private TableColumn<Reservation, IntegerProperty> reservationAdults;
    @FXML
    private TableColumn<Reservation, IntegerProperty> reservationChildren;
    @FXML
    private TableColumn<Reservation, DoubleProperty> reservationPrice;
    @FXML
    private TableColumn<Reservation, IntegerProperty> reservationHotelRoom;
    @FXML
    private TableColumn<Reservation, BooleanProperty> reservationApproved;
    @FXML
    private TableView<HotelRoom> roomsTable;
    @FXML
    private ComboBox<Integer> roomsNumComBox;

    //Menu Items
    @FXML
    private MenuItem sortASCMIt;
    @FXML
    private MenuItem sortDESCMIt;
    @FXML
    private MenuItem updateMIt;
    @FXML
    private MenuItem addRoomMIt;
    @FXML
    private MenuItem aboutMIt;

    //Search
    @FXML
    public TextField searchTF;

    private Stage mainWindow;

    private ObservableList<Integer> roomsNumList = FXCollections.observableArrayList(1, 2, 3, 4);

    private ObservableList<Integer> adultsNumList = FXCollections.observableArrayList(1,2,3,4);

    private ObservableList<Integer> childrenNumList = FXCollections.observableArrayList(1,2,3,4);

    private List<Reservation> reservations = new ArrayList<>();

    private ObservableList<Reservation> allReservations;

    private List<Customer> customers = new ArrayList<>();

    private List<HotelRoom> hotelRooms = new ArrayList<>();
    /**
     * Field for exchanging data with dialogs
     */
    private HotelRoom selectedHotelRoom;

    private Customer selectedCustomer;

    private Reservation selectedReservation;

    // Databases connector
    private ReservationDaoImpl reservationDaoImpl;

    public void initialize(URL location, ResourceBundle resources) {
        //Database connection
        reservationDaoImpl = new ReservationDaoImpl();
        //Update from database
        readFromDataBaseOrUpdateReservationsTable(true);

        //Buttons
        resetBtn.setOnAction(event -> resetAll());
        reserveBtn.setOnAction(event -> reserve());
        hotelRoomBtn.setOnAction(event -> startSelectRoomDialog(getMainWindow()));
        calculateBtn.setOnAction(event -> calculatePrice());

        //Table
        reservationID.setCellValueFactory(new PropertyValueFactory<>("ReservationId"));
        reservationCustomer.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        reservationCheckIn.setCellValueFactory(new PropertyValueFactory<>("CheckIn"));
        reservationCheckOut.setCellValueFactory(new PropertyValueFactory<>("CheckOut"));
        reservationAdults.setCellValueFactory(new PropertyValueFactory<>("NumOfAdults"));
        reservationChildren.setCellValueFactory(new PropertyValueFactory<>("NumOfChildren"));
        reservationHotelRoom.setCellValueFactory(new PropertyValueFactory<>("HotelRoomNumber"));
        reservationPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        reservationApproved.setCellValueFactory(new PropertyValueFactory<>("Approved"));

        reservationTable.setOnMouseClicked(this::tableOnClick);


        //ComboBox
        numOfAdultsComBox.setItems(adultsNumList);
        numOfAdultsComBox.setValue(1);
        numOfChildrenComBox.setItems(childrenNumList);
        numOfChildrenComBox.setValue(0);

        //Menu items
        addRoomMIt.setOnAction(event -> startAddNewRoomDialog(getMainWindow()));
        sortASCMIt.setOnAction(event -> sort(ASC));
        sortDESCMIt.setOnAction(event -> sort(DESC));
        updateMIt.setOnAction(event -> readFromDataBaseOrUpdateReservationsTable(false));

        initAutoSearch();
        getAllCustomersAndInitAutoCompletion();
    }

    /**
     * Read info from Databases and initialise all Lists or update table
     * @param readFromDB, if true readFromDB, if false update table without reading
     */
    private void readFromDataBaseOrUpdateReservationsTable(boolean readFromDB) {
        if (readFromDB) {
            clearReservationsTable();
            reservations = reservationDaoImpl.getAllReservations();
            allReservations = FXCollections.observableArrayList(reservations);
            customers = reservationDaoImpl.getAllCustomers();
            hotelRooms = reservationDaoImpl.getAllHotelRooms();
            reservationTable.getItems().addAll(allReservations);
        } else {
            clearReservationsTable();
            reservationTable.getItems().addAll(allReservations);
        }
    }

    private void clearReservationsTable() {
        reservationTable.getItems().clear();
    }

    /**
     * Realization of search on all fields of the table
     */
    private void initAutoSearch() {;
        searchTF.setPromptText("find...");
        searchTF.textProperty().addListener(observable -> {
            if (searchTF.textProperty().get().isEmpty()) {
                reservationTable.setItems(allReservations);
            }

            ObservableList<Reservation> filteredResList = FXCollections.observableArrayList();
            ObservableList<TableColumn<Reservation, ?>> columns = reservationTable.getColumns();

            for (int i = 0; i < allReservations.size(); i++) {
                for (int j = 0; j < columns.size(); j++) {
                    TableColumn column = columns.get(j);
                    String cellValue = column.getCellData(allReservations.get(i)).toString();
                    cellValue = cellValue.toLowerCase();
                    if (cellValue.contains(searchTF.textProperty().get().toLowerCase())) {
                        filteredResList.add(allReservations.get(i));
                        break;
                    }
                }
            }
            reservationTable.setItems(filteredResList);
        });
    }

    /**
     * Read info from customer table in DB, and  bind AutoCompletion
     */
    private void getAllCustomersAndInitAutoCompletion() {
        customers = reservationDaoImpl.getAllCustomers();
        updateAutoCompletion();
    }

    /**
     * Update AutoCompletion
     * @see <a href="http://controlsfx.bitbucket.org/org/controlsfx/control/textfield/TextFields.html">Controlsfx TextFields</a>
     */
    private void updateAutoCompletion() {
        TextFields.bindAutoCompletion(nameTF, customers);
    }

    /**
     * The logic of reservations
     */
    private void reserve() {
        Long reservationId;
        if (!selectedReservationPresent()) {
            selectedReservation = setApprovedAndAvailability(createReservation());
        }
        reservationId = reservationDaoImpl.saveOrUpdateReservation(setApprovedAndAvailability(selectedReservation));
//        reservations.add(selectedReservation);
        allReservations.add(selectedReservation);

        readFromDataBaseOrUpdateReservationsTable(false);
        AlertDialogs.showNotifications("Reservations change",
                String.format("Reservations number %s, added!", reservationId),
                getMainWindow());
        resetAll();
    }

    /**
     * Returns true if the reservation has already been created
     */
    private boolean selectedReservationPresent() {
        return selectedReservation != null;
    }

    /**
     * Set approved for reservation, and availability for room
     * @param reservation
     * @return reservation
     */
    public Reservation setApprovedAndAvailability(Reservation reservation) {
        saveOrUpdateHotelRoom(reservation.getHotelRoom(), false);
        return reservation.setApproved(checkApprovedChB());
    }

    /**
     * Save hotelRoom or update availability and wriate updates to database
     * @param hotelRoom
     * @param availability
     */
    private void saveOrUpdateHotelRoom(HotelRoom hotelRoom, boolean availability) {
        reservationDaoImpl.saveOrUpdateHotelRoom(hotelRoom.setAvailability(availability));
        if (availability) {
            hotelRooms.add(hotelRoom);
        } else {
            hotelRooms.remove(hotelRoom);
        }
    }

    /**
     * Create new reservation
     * @return new reservation if all parameters are correct
     */
    private Reservation createReservation() {
        Reservation result = null;
        String customerName = nameTF.getText();
        LocalDate checkInDate = checkInDP.getValue();
        LocalDate checkOutDate = checkOutDP.getValue();
        Integer numOfAdults = numOfAdultsComBox.getSelectionModel().getSelectedItem();
        Integer numOfChildren = numOfChildrenComBox.getSelectionModel().getSelectedItem();

        if (checkAllParameters(customerName, checkInDate, checkOutDate) &&
                selectedRoomPresent()) {
            selectedCustomer = searchOrAddNewCustomer(customerName);
            if (!customers.contains(selectedCustomer)) {
                reservationDaoImpl.saveOrUpdateCustomer(selectedCustomer);
            }
            result = new Reservation(selectedCustomer,
                    selectedHotelRoom,
                    checkInDate, checkOutDate, numOfAdults,numOfChildren);
        }
        return result;
    }

    /**
     * Search or create new customer for reservation
     * @param customerName
     * @return customer
     */
    private Customer searchOrAddNewCustomer(@NotNull String customerName) {
        Customer result = null;
        for (Customer customer : customers) {
            if (customer.getName().equals(customerName)) {
                result = customer;
            }
        }
        if (result == null) {
            result = new Customer(customerName);
            updateAutoCompletion();
        }
        return result;
    }

    /**
     * Returns true if the room selected, or showErrorDialog
     */
    private boolean selectedRoomPresent() {
        if (selectedHotelRoom != null) {
            return true;
        } else {
            AlertDialogs.showErrorDialog("Please, select Hotel Room!", getMainWindow());
        }
        return false;
    }

    /**
     * Returns true if all parameters are correct, orr showErrorDialog
     * @param customerName
     * @param checkIn
     * @param checkOut
     */
    private boolean checkAllParameters(String customerName, LocalDate checkIn, LocalDate checkOut) {
        boolean result = false;
        if (fieldsIsEmpty(customerName, checkIn, checkOut)) {
            AlertDialogs.showErrorDialog("All fields must be filled!",
                    getMainWindow());
        } else if (!isValidCustomerName(customerName)) {
            AlertDialogs.showErrorDialog("The customer name should be not less than 3 characters and no more than 15!",
                    getMainWindow());
        } else if (!isValidDates(checkIn, checkOut)) {
            AlertDialogs.showErrorDialog("The checkOut Date must be less than checkIn Date!",
                    getMainWindow());
        } else result = true;

        return result;
    }

    /**
     * Returns true if all fields are empty
     * @param customerName
     * @param checkIn
     * @param checkOut
     */
    private boolean fieldsIsEmpty(String customerName, LocalDate checkIn, LocalDate checkOut) {
        return customerName == null
                || checkIn == null
                || checkOut == null;
    }

    /**
     * Returns true if customer name is correct
     * @param customerName
     */
    private static final int MIN_CUSTOMER_NAME_LENGTH = 3;
    private static final int MAX_CUSTOMER_NAME_LENGTH = 15;
    private boolean isValidCustomerName(String customerName) {
        return customerName != null && !customerName.trim().isEmpty() &&
                customerName.length() >= MIN_CUSTOMER_NAME_LENGTH &&
                customerName.length() <= MAX_CUSTOMER_NAME_LENGTH;
    }

    /**
     * Returns true if dates indicated in the right order
     * @param checkIn
     * @param checkOut
     */
    private boolean isValidDates(LocalDate checkIn, LocalDate checkOut) {
        int result = checkOut.compareTo(checkIn);
        return result > -1;
    }

    /**
     * Returns true if the reservation is confirmed
     */
    private boolean checkApprovedChB() {
        return approvedChB.selectedProperty().get();
    }

    /**
     * Clears all fields and set default values
     */
    private void resetAll() {
        nameTF.clear();
        checkInDP.setValue(null);
        checkOutDP.setValue(null);
        roomNumberTF.setText(null);
        numOfAdultsComBox.setValue(1);
        numOfChildrenComBox.setValue(0);
        approvedChB.setIndeterminate(false);
        priceLabel.setText("");
        resetReservation();
    }

    /**
     * Removes previously created reservations
     */
    private void resetReservation() {
        selectedCustomer = null;
        selectedHotelRoom = null;
        selectedReservation = null;
    }

    /**
     * Start a dialog to select the room
     * @param stage
     */
    private void startSelectRoomDialog(Stage stage) {
        javafx.scene.control.Dialog<HotelRoom> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Select HotelRoom");
        dialog.setHeaderText("Please, enter all the necessary information!");

        ButtonType addButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        roomsNumComBox = new ComboBox<>();
        roomsNumComBox.getItems().addAll(FXCollections.observableArrayList(1, 2, 3, 4));
        roomsNumComBox.setPromptText("rooms number");

        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(event -> searchRoomsAndUpdateRoomsTable());
        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(event -> updateRoomsTable());

        HBox searchPanel = new HBox();
        searchPanel.setPadding(new Insets(10, 10, 10, 10));
        searchPanel.setSpacing(10);
        searchPanel.getChildren().addAll(roomsNumComBox, searchBtn, updateBtn);

        //Table
        roomsTable = new TableView<>();

        TableColumn numberColumn = new TableColumn<>("Number");
        numberColumn.setMinWidth(120);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn numOfRoomsColumn = new TableColumn<>("Number Of Rooms");
        numOfRoomsColumn.setMinWidth(125);
        numOfRoomsColumn.setCellValueFactory(new PropertyValueFactory<>("NumOfRooms"));

        TableColumn priceColumn = new TableColumn<>("Price For One Day");
        priceColumn.setMinWidth(135);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("PriceForOneDay"));

        roomsTable.getColumns().addAll(numberColumn, numOfRoomsColumn, priceColumn);

        roomsTable.getItems().addAll(getFilteredRoomsByAvailability(hotelRooms));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(searchPanel, roomsTable);

        dialog.getDialogPane().setContent(vBox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return roomsTable.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<HotelRoom> result = dialog.showAndWait();
        result.ifPresent(hotelRoom -> {
            try {
                selectedHotelRoom = hotelRoom;
                roomNumberTF.setText(String.valueOf(hotelRoom.getNumber()));
            } catch (NullPointerException e) {
                AlertDialogs.showErrorDialog("Please, select one room from table",
                        getMainWindow());
            }
        });
    }

    private void updateRoomsTable() {
        clearRoomsTable();
        roomsTable.getItems().addAll(getFilteredRoomsByAvailability(hotelRooms));
    }

    /**
     * Filtered rooms by availability
     * @param allHotelRooms
     * @return filtered List<HotelRoom>
     */
    private List<HotelRoom> getFilteredRoomsByAvailability(List<HotelRoom> allHotelRooms) {
        return allHotelRooms.stream()
                .filter(HotelRoom::isAvailability)
                .collect(Collectors.toList());
    }

    private void clearRoomsTable() {
        roomsTable.getItems().clear();
    }
    private void searchRoomsAndUpdateRoomsTable() {
        List<HotelRoom> filteredRoomsList = getFilteredRoomsListByNumberRooms(roomsNumComBox.getSelectionModel().getSelectedItem(), hotelRooms);
        clearRoomsTable();
        roomsTable.getItems().addAll(filteredRoomsList);
    }

    /**
     * Filtered rooms by selected parameters
     * @param numOfRooms
     * @param mainList
     * @return filtered List<HotelRoom>
     */
    private List<HotelRoom> getFilteredRoomsListByNumberRooms(Integer numOfRooms, List<HotelRoom> mainList) {
        List<HotelRoom> result = mainList.stream()
                .filter(room -> room.isAvailability() && room.getNumOfRooms() >= numOfRooms)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            AlertDialogs.showErrorDialog("Rooms with such parameters was not found!",
                    getMainWindow());
        }

        return result;
    }

    /**
     * Start a dialog to add new room
     * @param stage
     */
    private void startAddNewRoomDialog(Stage stage) {
        javafx.scene.control.Dialog<HotelRoom> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Add new HotelRoom");
        dialog.setHeaderText("Please, enter all the necessary information!");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField numberTF = new TextField();
        numberTF.setPromptText("set number");

        ComboBox<Integer> roomNumComBox = new ComboBox<Integer>(roomsNumList);
        roomNumComBox.setValue(1);

        TextField priceTF = new TextField();
        priceTF.setPromptText("set price");

        grid.add(new Label("Number: "), 0, 0);
        grid.add(numberTF, 1, 0);
        grid.add(new Label("Number of Rooms: "),0,1);
        grid.add(roomNumComBox, 1, 1);
        grid.add(new Label("Price: "),0,2);
        grid.add(priceTF, 1, 2);
        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Integer number = (Integer) getNumFromTF(numberTF, false);
                Integer numOfRooms = roomNumComBox.getSelectionModel().getSelectedItem();
                Double price = (Double) getNumFromTF(priceTF, true);

                return new HotelRoom(number, numOfRooms, price);
            } return null;
        });

        Optional<HotelRoom> result = dialog.showAndWait();
        result.ifPresent(hotelRoom -> {
            Integer number = hotelRoom.getNumber();
            if (isValidRoomNumber(number)) {
                saveOrUpdateHotelRoom(hotelRoom, true);
                AlertDialogs.showNotifications("Rooms changed",
                        String.format("Room number %s, added", number),
                        getMainWindow());
            } else {
                startAddNewRoomDialog(getMainWindow());
            }
        });
    }

    /**
     * Returns true if room with this number do not exist
     * @param number
     */
    private boolean isValidRoomNumber(int number) {
        boolean result = true;
        for (HotelRoom hotelRoom : hotelRooms) {
            if (hotelRoom.getNumber() == number) {
                result = false;
                AlertDialogs.showErrorDialog("HotelRoom with this number already exists",
                        getMainWindow());
                break;
            }
        }
        return result;
    }

    /**
     * Get number from text fields and validates input parameters
     * @param numberTF
     * @param isDouble
     * @return number
     */
    private Number getNumFromTF(TextField numberTF, boolean isDouble) {
        Number result = null;
        String str = numberTF.getText().trim();
        try {
            if (isDouble) result = Double.valueOf(str);
            else result = Integer.valueOf(str);

        } catch (NumberFormatException e) {
            AlertDialogs.showErrorDialog("Please enter the correct number in relevant fields", getMainWindow());
        }
        return result;
    }

    private void tableOnClick(MouseEvent mouseEvent) {
        int selectedIndex = reservationTable.getSelectionModel().getSelectedIndex();
        Reservation reservation = reservationTable.getItems().get(selectedIndex);
        Long reservationId = reservation.getReservationId();

        ContextMenu menu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            Action response = Dialogs.create()
                    .owner(getMainWindow())
                    .title("Delete reservation")
                    .message(String.format("Are you sure you want to delete the reservation number %s?", reservationId))
                    .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                    .showConfirm();
            if (response == Dialog.ACTION_OK) {
                deleteReservation(reservation);
                AlertDialogs.showNotifications("Reservations change",
                        String.format("Reservations number %s, deleted!", reservationId),
                        getMainWindow());
            }
        });
        MenuItem approvedItem = new MenuItem("Approved");
        approvedItem.setOnAction(event -> {
            Action response = Dialogs.create()
                    .owner(getMainWindow())
                    .title("Approved reservation")
                    .message(String.format("Are you sure you want to approved the reservation number %s?", reservationId))
                    .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                    .showConfirm();
            if (response == Dialog.ACTION_OK) {
                reservationDaoImpl.saveOrUpdateReservation(reservation.setApproved(true));
                readFromDataBaseOrUpdateReservationsTable(false);
                AlertDialogs.showNotifications("Reservations change",
                        String.format("Reservations number %s, approved!", reservationId),
                        getMainWindow());
            }
        });

        menu.getItems().addAll(approvedItem, deleteItem);
        reservationTable.setContextMenu(menu);
    }

    /**
     * Delete reservation and update all info in database
     * @param reservation
     */
    private void deleteReservation(Reservation reservation) {
        reservationDaoImpl.deleteReservation(reservation.getReservationId());
        saveOrUpdateHotelRoom(reservation.getHotelRoom(), true);
        allReservations.remove(reservation);
        reservationTable.getItems().remove(reservation);
    }

    /**
     * Calculate reservations price
     */
    private void calculatePrice() {
        Double allPrice;
        if (selectedReservationPresent()) {
            allPrice = selectedReservation.getCountOfFullPrice();
        } else {
            selectedReservation = createReservation();
            allPrice = selectedReservation.getCountOfFullPrice();
        }
        priceLabel.setText(String.valueOf(allPrice));
    }

    /**
     * Sort table
     * @see com.replay588.hotelreception.util.comparators
     * @param order
     */
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    private void sort(String order) {
        ObservableList<Reservation> items = reservationTable.getItems();
        if (order.equals(ASC)) {
            Collections.sort(items, new CustomerNameComparatorASC());
        }
        if (order.equals(DESC)) {
            Collections.sort(items, new CustomerNameComparatorDESC());
        }
    }

    public Stage getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }
}
