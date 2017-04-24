
import javafx.application.Application;
import javafx.stage.Stage;
import qrConverter.QRconverter;
import qrView.QRfrontend;

/**
 * @author Crunchify.com Updated: 03/20/2016 - added code to narrow border size
 */

public class QRmain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage QRfrontend) throws Exception {
		new QRfrontend(new QRconverter()).start();

	}

	// Tutorial: http://zxing.github.io/zxing/apidocs/index.html

}
