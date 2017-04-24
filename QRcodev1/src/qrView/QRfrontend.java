package qrView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import qrConverter.QRconverter;

public class QRfrontend extends Stage {

	private GridPane rootPane;
	private TextField oppnrInput;
	private TextField matnrInput;
	private Text errorTextOppnr;
	private Text errorTextMatnr;
	private QRconverter qrConverter;
	private ImageView statusImageView;
	private Image checkImg;
	private Image errorImg;

	public QRfrontend(QRconverter qrConverter) {
		this.qrConverter = qrConverter;
		this.setTitle("QR converter");
		this.setScene(new Scene(createRootPane(), 400, 400));
	}

	private Parent createRootPane() {
		this.rootPane = new GridPane();
		rootPane.setAlignment(Pos.CENTER);
		rootPane.setHgap(10);
		rootPane.setVgap(10);
		rootPane.setPadding(new Insets(25, 25, 25, 25));

		fillRootPane(rootPane);

		return rootPane;
	}

	private void fillRootPane(GridPane rootPane) {

		// Welcome text
		Text welcomeText = new Text("QR generator");
		welcomeText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		rootPane.add(welcomeText, 0, 0, 2, 1);

		// Belegart
		ObservableList<String> options = FXCollections.observableArrayList("LOGBUCH", "KUNDENTEIL", "WEISSNICH");
		ComboBox comboBox = new ComboBox(options);
		comboBox.setVisibleRowCount(8);

		/**
		 * ObservableList bestimmt Werte der Combobox; eventuell für Admin-Panel
		 * danach Programm aktualisieren; wie speichert man Werte auch nachdem
		 * die Applikation neugestartet wurde?!?!? evtl. über eine txt-File, die
		 * dann überschrieben wird? z.B. belegart-options.txt z.b. InputStreamer
		 * als Klasse und dann getList(art der combobox)
		 * 
		 **/

		// Opportunitynummer
		rootPane.add(new Label("Opportunitynummer:"), 0, 1);

		this.oppnrInput = new TextField();

		oppnrInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					oppnrInput.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		rootPane.add(oppnrInput, 1, 1);

		this.errorTextOppnr = new Text("Nummer muss 8 Stellen lang sein");
		errorTextOppnr.setFill(Color.FIREBRICK);
		errorTextOppnr.setVisible(false);
		rootPane.add(errorTextOppnr, 1, 2);

		// Materialnummer
		rootPane.add(new Label("Materialnummer:"), 0, 3);

		this.matnrInput = new TextField();

		matnrInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					matnrInput.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		rootPane.add(matnrInput, 1, 3);

		this.errorTextMatnr = new Text("Auftr.-Pos. muss 8 Stellen lang sein");
		errorTextMatnr.setFill(Color.FIREBRICK);
		errorTextMatnr.setVisible(false);
		rootPane.add(errorTextMatnr, 1, 4);

		// StatusImage + Button
		Button btn = new Button("Convert to QR");
		this.checkImg = new Image("qrView/check.png");
		this.errorImg = new Image("qrView/error.png");
		this.statusImageView = new ImageView(checkImg);
		statusImageView.setVisible(false);

		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(statusImageView);
		hbBtn.getChildren().add(btn);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				convertInputsToString();
			}
		});
		GridPane.setColumnSpan(hbBtn, 2);
		rootPane.add(hbBtn, 1, 5);
	}

	public void start() {
		this.show();
	}

	/**
	 * Retrieves input from textfields and appends them to one StringBuffer with
	 * semicolons inbetween. Checks for empty fields and has a final checksum at
	 * the end, when all fields have been put together Evokes QRconverter method
	 * with produced string
	 */
	private void convertInputsToString() {
		StringBuffer stringOutput = new StringBuffer();
		int checkSum = 0;
		boolean errorOccured = false;

		if ((oppnrInput.getText() != null && !oppnrInput.getText().isEmpty())) {
			stringOutput.append(oppnrInput.getText() + ";");
			errorTextOppnr.setVisible(false);
			checkSum++;
		} else {
			errorTextOppnr.setVisible(true);
			errorOccured = true;
		}

		if ((matnrInput.getText() != null && !matnrInput.getText().isEmpty())) {
			stringOutput.append(matnrInput.getText() + ";");
			errorTextMatnr.setVisible(false);
			checkSum++;
		} else {
			errorTextMatnr.setVisible(true);
			errorOccured = true;
		}

		if (errorOccured) {
			statusImageView.setImage(errorImg);
			statusImageView.setVisible(true);
		} else if (checkSum == 2) {
			qrConverter.convertToQr(stringOutput.toString());
			statusImageView.setImage(checkImg);
			statusImageView.setVisible(true);
			oppnrInput.clear();
			matnrInput.clear();
		} else {
			throw new IllegalArgumentException("The inputs don't match the checksum. Check if all fields are filled");
		}
	}

}
