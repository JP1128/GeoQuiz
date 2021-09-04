package edu.uga.cs1302.quiz;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PopOver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportPage extends HBox {
    // Constant Properties
    private static final int WIDTH = 800;
    private static final int HEIGHT = 720;

    // ReportPage
    private final JFXTreeTableView<QuizResult> jfxTable = new JFXTreeTableView<>();
    private final PopOver detailPop = new PopOver();

    public ReportPage() {
        getStylesheets().add(ResourceManager.REPORT_PAGE_CSS);
        setId("report-page");

        setPrefSize(WIDTH, HEIGHT);

        initTable();

        HBox.setHgrow(jfxTable, Priority.ALWAYS);
        HBox.setMargin(jfxTable, new Insets(30));

        getChildren().add(jfxTable);
    } // ReportPage

    @SuppressWarnings("unchecked")
    private void initTable() {
        jfxTable.setId("report-table");
        jfxTable.setShowRoot(false);

        final ObservableList<QuizResult> quizResults = FXCollections.observableList(Util.readResults());
        quizResults.sort((result1, result2) -> result2.getDate().compareTo(result1.getDate()));

        final JFXTreeTableColumn<QuizResult, String> usernameCol = setUpColumn("USERNAME", 0.45, Pos.CENTER_LEFT);
        usernameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue().getUsername()));

        final JFXTreeTableColumn<QuizResult, Integer> correctCol = setUpColumn("CORRECT", 0.14, Pos.CENTER);
        correctCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getValue().getRight()).asObject());

        final JFXTreeTableColumn<QuizResult, Integer> totalCol = setUpColumn("TOTAL", 0.10, Pos.CENTER);
        totalCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getValue().getTotal()).asObject());

        final JFXTreeTableColumn<QuizResult, String> dateCol = setUpColumn("DATE", 0.306, Pos.CENTER);
        dateCol.setCellValueFactory(cell -> {
            Date date = cell.getValue().getValue().getDate();
            return new SimpleStringProperty(new SimpleDateFormat("HH:mm:ss MM-dd-yyyy").format(date));
        });

        final TreeItem<QuizResult> root = new RecursiveTreeItem<>(quizResults, RecursiveTreeObject::getChildren);

        jfxTable.setRowFactory(ttv -> {
            JFXTreeTableRow<QuizResult> row = new JFXTreeTableRow<>();
            row.setId("report-table-row");
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null) {
                    if (!detailPop.isShowing()) {
                        showDetail(row.getItem(), row, event.getScreenX(), event.getScreenY());
                    } // if

                } // if
            });
            return row;
        });

        initDetail();

        jfxTable.getColumns().setAll(usernameCol, correctCol, totalCol, dateCol);
        jfxTable.setPlaceholder(new Label("No saved game data to display"));
        jfxTable.setRoot(root);
    } // initTable

    @SuppressWarnings("deprecation")
    private <E> JFXTreeTableColumn<QuizResult, E> setUpColumn(String headerName, double frac, Pos pos) {
        final JFXTreeTableColumn<QuizResult, E> column = new JFXTreeTableColumn<>(headerName);
        column.setId("report-table-columns");
        column.setCellFactory(columnFactory -> new JFXTreeTableCell<QuizResult, E>() {
            @Override
            protected void updateItem(E item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.toString());
                } // if
                setAlignment(pos);
            } // updateItem
        });

        column.prefWidthProperty().bind(jfxTable.widthProperty().multiply(frac));
        column.setResizable(false);
        column.setEditable(false);
        column.setSortable(false);
        column.impl_setReorderable(false);

        return column;
    } // setUpColumn

    private void initDetail() {
        detailPop.setId("detail-pop-over");
        detailPop.setAutoHide(true);
        detailPop.setAutoFix(true);
        detailPop.setArrowSize(0);
        detailPop.setAnimated(true);
    } // initDetail

    private void showDetail(QuizResult quizResult, Node owner, double x, double y) {
        if (detailPop.isShowing()) {
            detailPop.hide();
        } // if

        detailPop.setAutoHide(true);
        detailPop.setAutoFix(true);
        detailPop.setTitle(quizResult.getUsername());

        final JFXListView<QuizResult.QuestionResult> detailList = new JFXListView<>();
        detailList.getItems().addAll(quizResult.getQuestionResults());

        detailList.setCellFactory(param -> new JFXListCell<QuizResult.QuestionResult>() {
            @Override
            protected void updateItem(QuizResult.QuestionResult item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item.toString());
                    if (item.isCorrect()) {
                        setId("correct-cell");
                    } else {
                        setId("incorrect-cell");
                    } // if-else

                    detailList.prefWidthProperty()
                            .bind(Bindings.max(detailList.getWidth(), this.widthProperty()).add(50));
                } // if-else

            } // updateItem
        });

        detailList.setExpanded(true);
        detailList.setEditable(false);

        detailPop.setContentNode(detailList);
        detailPop.show(owner, x, y);
    } // showDetail

} // ReportPage
