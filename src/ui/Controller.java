package ui;

import core.*;
import database.exceptions.DatabaseError;
import database.repositories.ArterialPressurePSRepository;
import database.repositories.HeartRatePSRepository;
import database.repositories.SugarLevelPSRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import database.repositories.TaskPSRepository;
import database.repositories.MedicineReminderPSRepository;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import java.time.LocalDateTime;
import java.util.List;

public class Controller {

    // Sugar Level fields
    @FXML
    private TextField sugarLevelField;
    @FXML
    private TextField sugarDateField;
    @FXML
    private TextField sugarLevelOverField;

    @FXML
    private TableView<SugarLevel> sugarLevelTable;
    @FXML
    private TableColumn<SugarLevel, LocalDateTime> sugarDateColumn;
    @FXML
    private TableColumn<SugarLevel, Double> sugarLevelColumn;

    private SugarLevelPSRepository sugarLevelRepository;
    private ObservableList<SugarLevel> sugarLevelData = FXCollections.observableArrayList();

    // Heart Rate fields
    @FXML
    private TextField heartRateField;
    @FXML
    private TextField heartDateField;
    @FXML
    private TextField heartRateOverField;
    @FXML
    private TextField heartRateUnderField;

    @FXML
    private TableView<HeartRate> heartRateTable;
    @FXML
    private TableColumn<HeartRate, LocalDateTime> heartDateColumn;
    @FXML
    private TableColumn<HeartRate, Integer> heartRateColumn;

    @FXML
    private LineChart<Number, Number> heartRateChart;

    private HeartRatePSRepository heartRateRepository;
    private ObservableList<HeartRate> heartRateData = FXCollections.observableArrayList();

    // Arterial Pressure fields
    @FXML
    private TextField upperPressureField;
    @FXML
    private TextField lowerPressureField;
    @FXML
    private TextField arterialDateField;
    @FXML
    private TextField diastolicOverField;
    @FXML
    private TextField diastolicUnderField;
    @FXML
    private TextField systolicOverField;
    @FXML
    private TextField systolicUnderField;

    @FXML
    private TableView<ArterialPressure> arterialPressureTable;
    @FXML
    private TableColumn<ArterialPressure, LocalDateTime> arterialDateColumn;
    @FXML
    private TableColumn<ArterialPressure, Integer> systolicColumn;
    @FXML
    private TableColumn<ArterialPressure, Integer> diastolicColumn;

    @FXML
    private LineChart<Number, Number> arterialPressureChart;

    private ArterialPressurePSRepository arterialPressureRepository;
    private ObservableList<ArterialPressure> arterialPressureData = FXCollections.observableArrayList();

    @FXML
    private TextField monthField;
    @FXML
    private TextField yearField;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private TextField taskNameField;
    @FXML
    private TextField taskDescriptionField;
    @FXML
    private TextField medicineNameField;
    @FXML
    private TextField doseField;
    @FXML
    private ComboBox<MedicineUnit> unitComboBox;
    @FXML
    private TableColumn<MedicineReminder, LocalDateTime> reminderDateColumn;
    @FXML
    private TableColumn<MedicineReminder, String> reminderNameColumn;
    @FXML
    private TableColumn<MedicineReminder, Double> reminderDoseColumn;
    @FXML
    private TableColumn<MedicineReminder, MedicineUnit> reminderUnitColumn;
    @FXML
    private TableView<MedicineReminder> reminderTable;

    private TaskPSRepository taskRepository;
    private MedicineReminderPSRepository reminderRepository;
    private ObservableList<MedicineReminder> reminderData = FXCollections.observableArrayList();

    public Controller() {
        String host = "localhost";
        int port = 5432;
        String dbName = "postgres";
        String username = "postgres";
        String password = "12345";
        String connectionUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", username);
        connectionProperties.setProperty("password", password);

        sugarLevelRepository = new SugarLevelPSRepository(connectionUrl, connectionProperties);
        heartRateRepository = new HeartRatePSRepository(connectionUrl, connectionProperties);
        arterialPressureRepository = new ArterialPressurePSRepository(connectionUrl, connectionProperties);
        taskRepository = new TaskPSRepository(connectionUrl, connectionProperties);
        reminderRepository = new MedicineReminderPSRepository(connectionUrl, connectionProperties);
    }

    @FXML
    private void initialize() {
        // Initialize Sugar Level table columns
        sugarDateColumn.setCellValueFactory(new PropertyValueFactory<>("measurementDateTime"));
        sugarLevelColumn.setCellValueFactory(new PropertyValueFactory<>("sugarLevel"));
        loadSugarLevelData();
        sugarLevelTable.setItems(sugarLevelData);

        // Initialize Heart Rate table columns
        heartDateColumn.setCellValueFactory(new PropertyValueFactory<>("measurementDateTime"));
        heartRateColumn.setCellValueFactory(new PropertyValueFactory<>("heartRate"));
        loadHeartRateData();
        heartRateTable.setItems(heartRateData);

        // Initialize Heart Rate chart
        updateHeartRateChart(heartRateRepository.getAllHeartRates());

        // Initialize Arterial Pressure table columns
        arterialDateColumn.setCellValueFactory(new PropertyValueFactory<>("measurementDateTime"));
        systolicColumn.setCellValueFactory(new PropertyValueFactory<>("upperPressure"));
        diastolicColumn.setCellValueFactory(new PropertyValueFactory<>("lowerPressure"));
        loadArterialPressureData();
        arterialPressureTable.setItems(arterialPressureData);

        // Initialize Arterial Pressure chart
        updateArterialPressureChart(arterialPressureRepository.getAllArterialPressure());

        unitComboBox.setItems(FXCollections.observableArrayList(MedicineUnit.values()));
        reminderDateColumn.setCellValueFactory(new PropertyValueFactory<>("reminderDateTime"));
        reminderNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        reminderDoseColumn.setCellValueFactory(new PropertyValueFactory<>("dose"));
        reminderUnitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));

        reminderTable.setItems(reminderData);
    }

    // Sugar Level methods
    @FXML
    private void handleAddSugarLevel() {
        try {
            double sugarLevelValue = Double.parseDouble(sugarLevelField.getText());
            if (sugarLevelValue < 0) {
                showError("Sugar level cannot be negative.");
                return;
            }
            SugarLevel sugarLevel = new SugarLevel(LocalDateTime.now(), sugarLevelValue);
            sugarLevelRepository.addSugarLevel(sugarLevel);
            sugarLevelData.add(sugarLevel);
            sugarLevelField.clear();
            showInfo("Sugar level added successfully.");
        } catch (NumberFormatException e) {
            showError("Invalid sugar level input.");
        } catch (DatabaseError e) {
            showError("Error adding sugar level to the database.");
        }
    }

    @FXML
    private void handleDeleteSugarLevel() {
        SugarLevel selectedSugarLevel = sugarLevelTable.getSelectionModel().getSelectedItem();
        if (selectedSugarLevel != null) {
            sugarLevelRepository.removeSugarLevel(selectedSugarLevel);
            sugarLevelData.remove(selectedSugarLevel);
            showInfo("Sugar level deleted successfully.");
        } else {
            showError("No sugar level selected.");
        }
    }

    @FXML
    private void handleGetSugarLevelByDate() {
        try {
            LocalDate date = LocalDate.parse(sugarDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            sugarLevelData.clear();
            List<SugarLevel> sugarLevels = sugarLevelRepository.getSugarLevelByDate(date.atStartOfDay());
            sugarLevelData.addAll(sugarLevels);
        } catch (Exception e) {
            showError("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    @FXML
    private void handleGetAllSugarLevelOver() {
        try {
            double level = Double.parseDouble(sugarLevelOverField.getText());
            sugarLevelData.clear();
            List<SugarLevel> sugarLevels = sugarLevelRepository.getAllOver(level);
            sugarLevelData.addAll(sugarLevels);
        } catch (NumberFormatException e) {
            showError("Invalid level input.");
        }
    }

    private void loadSugarLevelData() {
        List<SugarLevel> sugarLevels = sugarLevelRepository.getAllSugarLevels();
        sugarLevelData.addAll(sugarLevels);
    }

    // Heart Rate methods
    @FXML
    private void handleAddHeartRate() {
        try {
            int heartRateValue = Integer.parseInt(heartRateField.getText());
            if (heartRateValue < 0) {
                showError("Heart rate cannot be negative.");
                return;
            }
            HeartRate heartRate = new HeartRate(LocalDateTime.now(), heartRateValue);
            heartRateRepository.addHeartRate(heartRate);
            heartRateData.add(heartRate);
            updateHeartRateChart(heartRateRepository.getAllHeartRates());
            heartRateField.clear();
            showInfo("Heart rate added successfully.");
        } catch (NumberFormatException e) {
            showError("Invalid heart rate input.");
        } catch (DatabaseError e) {
            showError("Error adding heart rate to the database.");
        }
    }

    @FXML
    private void handleDeleteHeartRate() {
        HeartRate selectedHeartRate = heartRateTable.getSelectionModel().getSelectedItem();
        if (selectedHeartRate != null) {
            heartRateRepository.removeHeartRate(selectedHeartRate);
            heartRateData.remove(selectedHeartRate);
            updateHeartRateChart(heartRateRepository.getAllHeartRates());
            showInfo("Heart rate deleted successfully.");
        } else {
            showError("No heart rate selected.");
        }
    }

    @FXML
    private void handleGetHeartRateByDate() {
        try {
            LocalDate date = LocalDate.parse(heartDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            heartRateData.clear();
            List<HeartRate> heartRates = heartRateRepository.getHeartRateByDate(date.atStartOfDay());
            heartRateData.addAll(heartRates);
            updateHeartRateChart(heartRates);
        } catch (Exception e) {
            showError("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    @FXML
    private void handleGetAllHeartRateOver() {
        try {
            int level = Integer.parseInt(heartRateOverField.getText());
            heartRateData.clear();
            List<HeartRate> heartRates = heartRateRepository.getAllOver(level);
            heartRateData.addAll(heartRates);
            updateHeartRateChart(heartRates);
        } catch (NumberFormatException e) {
            showError("Invalid level input.");
        }
    }

    @FXML
    private void handleGetAllHeartRateUnder() {
        try {
            int level = Integer.parseInt(heartRateUnderField.getText());
            heartRateData.clear();
            List<HeartRate> heartRates = heartRateRepository.getAllUnder(level);
            heartRateData.addAll(heartRates);
            updateHeartRateChart(heartRates);
        } catch (NumberFormatException e) {
            showError("Invalid level input.");
        }
    }

    private void loadHeartRateData() {
        List<HeartRate> heartRates = heartRateRepository.getAllHeartRates();
        heartRateData.addAll(heartRates);
        updateHeartRateChart(heartRates);
    }

    private void updateHeartRateChart(List<HeartRate> heartRates) {
        heartRateChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Heart Rate");

        for (int i = 0; i < heartRates.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, heartRates.get(i).getHeartRate()));
        }

        heartRateChart.getData().add(series);
    }

    // Arterial Pressure methods
    @FXML
    private void handleAddArterialPressure() {
        try {
            int upperPressureValue = Integer.parseInt(upperPressureField.getText());
            int lowerPressureValue = Integer.parseInt(lowerPressureField.getText());
            if (upperPressureValue < 0 || lowerPressureValue < 0) {
                showError("Pressure values cannot be negative.");
                return;
            }
            ArterialPressure pressure = new ArterialPressure(LocalDateTime.now(), upperPressureValue, lowerPressureValue);
            arterialPressureRepository.addArterialPressure(pressure);
            arterialPressureData.add(pressure);
            upperPressureField.clear();
            lowerPressureField.clear();
            updateArterialPressureChart(arterialPressureRepository.getAllArterialPressure());
            showInfo("Arterial pressure added successfully.");
        } catch (NumberFormatException e) {
            showError("Invalid pressure input.");
        } catch (DatabaseError e) {
            showError("Error adding arterial pressure to the database.");
        }
    }

    @FXML
    private void handleDeleteArterialPressure() {
        ArterialPressure selectedPressure = arterialPressureTable.getSelectionModel().getSelectedItem();
        if (selectedPressure != null) {
            arterialPressureRepository.removeArterialPressure(selectedPressure);
            arterialPressureData.remove(selectedPressure);
            updateArterialPressureChart(arterialPressureRepository.getAllArterialPressure());
            showInfo("Arterial pressure deleted successfully.");
        } else {
            showError("No arterial pressure selected.");
        }
    }

    @FXML
    private void handleGetArterialPressureByDate() {
        try {
            LocalDate date = LocalDate.parse(arterialDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            arterialPressureData.clear();
            List<ArterialPressure> pressures = arterialPressureRepository.getArterialPressureByDate(date.atStartOfDay());
            arterialPressureData.addAll(pressures);
            updateArterialPressureChart(pressures);
        } catch (Exception e) {
            showError("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    @FXML
    private void handleGetAllDiastolicOver() {
        try {
            int level = Integer.parseInt(diastolicOverField.getText());
            arterialPressureData.clear();
            List<ArterialPressure> pressures = arterialPressureRepository.getAllOverLower(level);
            arterialPressureData.addAll(pressures);
            updateArterialPressureChart(pressures);
        } catch (NumberFormatException e) {
            showError("Invalid level input.");
        }
    }

    @FXML
    private void handleGetAllDiastolicUnder() {
        try {
            int level = Integer.parseInt(diastolicUnderField.getText());
            arterialPressureData.clear();
            List<ArterialPressure> pressures = arterialPressureRepository.getAllUnderLower(level);
            arterialPressureData.addAll(pressures);
            updateArterialPressureChart(pressures);
        } catch (NumberFormatException e) {
            showError("Invalid level input.");
        }
    }

    @FXML
    private void handleGetAllSystolicOver() {
        try {
            int level = Integer.parseInt(systolicOverField.getText());
            arterialPressureData.clear();
            List<ArterialPressure> pressures = arterialPressureRepository.getAllOverUpper(level);
            arterialPressureData.addAll(pressures);
            updateArterialPressureChart(pressures);
        } catch (NumberFormatException e) {
            showError("Invalid level input.");
        }
    }

    @FXML
    private void handleGetAllSystolicUnder() {
        try {
            int level = Integer.parseInt(systolicUnderField.getText());
            arterialPressureData.clear();
            List<ArterialPressure> pressures = arterialPressureRepository.getAllUnderUpper(level);
            arterialPressureData.addAll(pressures);
            updateArterialPressureChart(pressures);
        } catch (NumberFormatException e) {
            showError("Invalid level input.");
        }
    }

    private void loadArterialPressureData() {
        List<ArterialPressure> pressures = arterialPressureRepository.getAllArterialPressure();
        arterialPressureData.addAll(pressures);
        updateArterialPressureChart(pressures);
    }

    private void updateArterialPressureChart(List<ArterialPressure> pressures) {
        arterialPressureChart.getData().clear();
        XYChart.Series<Number, Number> systolicSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> diastolicSeries = new XYChart.Series<>();
        systolicSeries.setName("Systolic");
        diastolicSeries.setName("Diastolic");

        for (int i = 0; i < pressures.size(); i++) {
            systolicSeries.getData().add(new XYChart.Data<>(i, pressures.get(i).getUpperPressure()));
            diastolicSeries.getData().add(new XYChart.Data<>(i, pressures.get(i).getLowerPressure()));
        }

        arterialPressureChart.getData().addAll(systolicSeries, diastolicSeries);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleShowCalendar() {
        try {
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());

            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate firstOfMonth = yearMonth.atDay(1);
            int daysInMonth = yearMonth.lengthOfMonth();

            calendarGrid.getChildren().clear();

            for (int day = 1; day <= daysInMonth; day++) {
                LocalDateTime date = firstOfMonth.plusDays(day - 1).atStartOfDay();
                Button dayButton = new Button(String.valueOf(day));
                dayButton.setOnAction(event -> handleDayClick(date));
                calendarGrid.add(dayButton, (day - 1) % 7, (day - 1) / 7);
            }
        } catch (NumberFormatException e) {
            showError("Invalid month or year input.");
        }
    }

    private void handleDayClick(LocalDateTime date) {
        showInfo("Selected Date: " + date);
        loadRemindersForDate(date);
    }

    @FXML
    private void handleAddTask() {
        try {
            String taskName = taskNameField.getText();
            String taskDescription = taskDescriptionField.getText();
            if (taskName.isEmpty() || taskDescription.isEmpty()) {
                showError("Task name and description cannot be empty.");
                return;
            }
            Task task = new Task(LocalDateTime.now(), taskName, taskDescription);
            taskRepository.addTask(task);
            taskNameField.clear();
            taskDescriptionField.clear();
            showInfo("Task added successfully.");
        } catch (Exception e) {
            showError("Error adding task to the database.");
        }
    }

    @FXML
    private void handleAddReminder() {
        try {
            String medicineName = medicineNameField.getText();
            double dose = Double.parseDouble(doseField.getText());
            MedicineUnit unit = unitComboBox.getValue();
            if (medicineName.isEmpty() || dose <= 0 || unit == null) {
                showError("Medicine name, dose, and unit must be provided.");
                return;
            }
            MedicineReminder reminder = new MedicineReminder(LocalDateTime.now(), medicineName, dose, unit);
            reminderRepository.addReminder(reminder);
            medicineNameField.clear();
            doseField.clear();
            unitComboBox.getSelectionModel().clearSelection();
            showInfo("Medicine reminder added successfully.");
        } catch (NumberFormatException e) {
            showError("Invalid dose input.");
        } catch (Exception e) {
            showError("Error adding reminder to the database.");
        }
    }

    private void loadRemindersForDate(LocalDateTime date) {
        reminderData.clear();
        List<MedicineReminder> reminders = reminderRepository.getReminderByDate(date);
        reminderData.addAll(reminders);
    }
}