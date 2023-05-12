package com.example.clientserverapplicationcontracts.client;

import com.example.clientserverapplicationcontracts.server.Contract;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@FxmlView("Contracts.fxml")
public class WebController {

    @FXML
    private TableView table;

    @Autowired
    public WebController() { }

    public List<ContractJavaFX> GetContractFromServer() throws IOException {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("SELECT CONTRACTS", headers);

        String personResultAsJsonStr =
                restTemplate.postForObject("http://localhost:9090/contracts", request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(personResultAsJsonStr);
        ObjectReader reader = objectMapper.readerFor(new TypeReference<List<Contract>>() {
        });
        List<Contract> contracts = reader.readValue(root);
        List<ContractJavaFX> contractsJavaFX = new ArrayList<>();
        for(var contract:contracts){
            contractsJavaFX.add(new ContractJavaFX(contract));
        }
        return contractsJavaFX;
    }

    public void UpdateTable(ActionEvent actionEvent) {
        List<ContractJavaFX> listContracts = null;
        try {
            listContracts = GetContractFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObservableList<ContractJavaFX> contractsOL = FXCollections.observableArrayList(listContracts);
        this.table.setItems(contractsOL);
        // UUID
        TableColumn<ContractJavaFX, UUID> UUIDColumn = new TableColumn<>("UUID");
        UUIDColumn.setCellValueFactory(new PropertyValueFactory<>("NUMBER"));
        this.table.getColumns().add(UUIDColumn);
        // CREATEDATE
        TableColumn<ContractJavaFX, Date> CREATEDATEColumn = new TableColumn<>("CREATE DATE");
        CREATEDATEColumn.setCellValueFactory(new PropertyValueFactory<>("CREATEDATE"));
        CREATEDATEColumn.setCellFactory(new ColumnFormatter<>(new SimpleDateFormat("dd.MM.yyyy")));
        this.table.getColumns().add(CREATEDATEColumn);
        // LASTUPDATEDATE
        TableColumn<ContractJavaFX, Date> LASTUPDATEDATEColumn = new TableColumn<>("LAST UPDATE DATE");
        LASTUPDATEDATEColumn.setCellValueFactory(new PropertyValueFactory<>("LASTUPDATEDATE"));
        LASTUPDATEDATEColumn.setCellFactory(new ColumnFormatter<>(new SimpleDateFormat("dd.MM.yyyy")));
        this.table.getColumns().add(LASTUPDATEDATEColumn);
        // CHECKBOX
        Callback<TableColumn<ContractJavaFX, Boolean>, TableCell<ContractJavaFX, Boolean>> booleanCellFactory =
                new Callback<TableColumn<ContractJavaFX, Boolean>, TableCell<ContractJavaFX, Boolean>>() {
                    @Override
                    public TableCell<ContractJavaFX, Boolean> call(TableColumn<ContractJavaFX, Boolean> p) {
                        return new BooleanCell();
                    }
                };
        TableColumn<ContractJavaFX, Boolean> Check60DaysColumn = new TableColumn<>("LESS THAN 60 DAYS");
        Check60DaysColumn.setCellValueFactory(new PropertyValueFactory<>("LESSTHAN60DAYS"));
        Check60DaysColumn.setCellFactory(booleanCellFactory);
        this.table.getColumns().add(Check60DaysColumn);

    }
    class BooleanCell extends TableCell<ContractJavaFX, Boolean> {
        private CheckBox checkBox;
        public BooleanCell() {
            checkBox = new CheckBox();
            checkBox.setDisable(true);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(isEditing())
                        commitEdit(newValue == null ? false : newValue);
                }
            });
            this.setGraphic(checkBox);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setEditable(true);
        }
        @Override
        public void startEdit() {
            super.startEdit();
            if (isEmpty()) {
                return;
            }
            checkBox.setDisable(false);
            checkBox.requestFocus();
        }
        @Override
        public void cancelEdit() {
            super.cancelEdit();
            checkBox.setDisable(true);
        }
        public void commitEdit(Boolean value) {
            super.commitEdit(value);
            checkBox.setDisable(true);
        }
        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                checkBox.setSelected(item);
            }
        }
    }
}
