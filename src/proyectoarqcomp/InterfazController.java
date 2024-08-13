package proyectoarqcomp;

import backend.Procesador;
import backend.Conversiones;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InterfazController implements Initializable {

    @FXML
    private MenuItem menuCargarPrograma;
    @FXML
    private TextField registroPC;
    @FXML
    private TextField registroAR;
    @FXML
    private TextField registroDR;
    @FXML
    private TextField registroTR;
    @FXML
    private TextField registroAC;
    @FXML
    private TextField registroIR;
    @FXML
    private TextField registroI;
    @FXML
    private TextField registroE;
    @FXML
    private TextField registroINPR;
    @FXML
    private TextField registroOUTR;
    @FXML
    private TextField registroSALIDA;
    @FXML
    private TextField registroINSTRICCION;
    @FXML
    private Button botonStep;
    @FXML
    private Label etiquetaTerminar;
    @FXML
    private SVGPath flechaIndicador;
    @FXML
    private TableView vistaTabla;
    @FXML
    private TableColumn columnaDireccion;
    @FXML
    private TableColumn columnaInstruccion;

    int[] memoria;
    Procesador proce1;
    List<Integer> lista;
    @FXML
    private MenuItem menuCerrarPrograma;
    @FXML
    private MenuItem menuInformacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("Direccion"));
        columnaInstruccion.setCellValueFactory(new PropertyValueFactory<>("Valor"));
        flechaIndicador.setManaged(false);

    }

    private int[] inicializadorMemoria(String path) {

        lista = new ArrayList();

        try {
            Scanner sc = new Scanner(new File(path));

            while (sc.hasNext()) {
                lista.add(sc.nextInt(16));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int mem[] = new int[lista.size()];

        for (int i = 0; i < lista.size(); i++) {
            mem[i] = lista.get(i);

        }

        return mem;
    }

    @FXML
    private void cargarPrograma(ActionEvent event) throws Exception {

        Stage escenario = new Stage();
        FileChooser fc1 = new FileChooser();
        fc1.setTitle("Cargar programa desde archivo .txt");
        File archivo = fc1.showOpenDialog(escenario);

        if (archivo != null) {
            System.out.println(archivo.getPath());
            memoria = inicializadorMemoria(archivo.getPath());
            proce1 = new Procesador(memoria);
            moverFlecha(-1);

            vistaTabla.getItems().clear();
            for (int i = 0; i < lista.size(); i++) {
                actualizarTableView(i, memoria[i]);
            }

            botonStep.setDisable(false);
            etiquetaTerminar.setText(null);
            registroSALIDA.setText(null);
            registroINPR.setText(null);
            menuCargarPrograma.setDisable(true);
        } else {
            System.out.println("No hay archivo seleccionado!");
        }
    }

    @FXML
    private void cerrarPrograma(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void avanzarPaso(ActionEvent event) {

        proce1.step();
        registroPC.setText(String.valueOf(Conversiones.intToHex(proce1.getPC())).substring(1).toUpperCase());
        registroAR.setText(String.valueOf(Conversiones.intToHex(proce1.getAR())).substring(1).toUpperCase());
        registroDR.setText(String.valueOf(Conversiones.intToHex(proce1.getDR())).toUpperCase());
        registroTR.setText(String.valueOf(Conversiones.intToHex(proce1.getTR())).toUpperCase());
        registroAC.setText(String.valueOf(Conversiones.intToHex(proce1.getAC())).toUpperCase());
        registroIR.setText(String.valueOf(Conversiones.intToHex(proce1.getIR())).toUpperCase());
        registroI.setText(String.valueOf(Conversiones.intToHex(proce1.getI())).substring(3).toUpperCase());
        registroE.setText(String.valueOf(Conversiones.intToHex(proce1.getE())).substring(3).toUpperCase());
        registroOUTR.setText(String.valueOf(Conversiones.intToHex(proce1.getOUTR())).substring(2).toUpperCase());
        registroINSTRICCION.setText(String.valueOf(proce1.getInstruccion()).toUpperCase());

        vistaTabla.getItems().clear();
        for (int i = 0; i < lista.size(); i++) {
            actualizarTableView(i, memoria[i]);
        }

        moverFlecha(proce1.getPoscision());
        insertarCaracter();

        if (proce1.getInstruccion().equals("HLT")) {
            botonStep.setDisable(true);
            etiquetaTerminar.setText("Finalizado!");
            moverFlecha(3);
            menuCargarPrograma.setDisable(false);
        } else {
            botonStep.setDisable(false);
        }

        if (proce1.getInstruccion().equals("OUT") && proce1.getTiempo() - 1 == -1) {
            registroSALIDA.appendText(String.valueOf((char) proce1.getOUTR()));
        }

    }

    @FXML
    private void desplegarInfoPrograma(ActionEvent event) {
        Alert informacion = new Alert(Alert.AlertType.INFORMATION);
        informacion.setTitle("Info");
        informacion.setContentText("https://github.com/davidsito48");
        informacion.setHeaderText("David Reyes | TSJ Chapala - ISC | 2022-2024");
        informacion.showAndWait();
    }

    private void actualizarTableView(int direccion, int valor) {
        vistaTabla.getItems().add(new ModeloMemoria(Conversiones.intToHex(direccion).substring(1).toUpperCase(), Conversiones.intToHex(valor).toUpperCase()));
    }

    private void insertarCaracter() {

        if (proce1.getPoscision() == 1 && registroIR.getText().equalsIgnoreCase("F800")) {
            botonStep.setDisable(true);
            Alert informacion = new Alert(Alert.AlertType.INFORMATION);
            informacion.setTitle("Accion requerida!");
            informacion.setHeaderText("Inserta un caracter en el registro INP");
            informacion.setContentText("Presiona ENTER");
            informacion.showAndWait();

        } else {
            if (proce1.getPoscision() == 2 && registroIR.getText().equalsIgnoreCase("F800")) {

                proce1.setINPR((registroINPR.getText().charAt(0)));
            }
        }

    }

    @FXML
    private void fakeStep(KeyEvent event) {

        switch (event.getCode()) {
            case ENTER:
                botonStep.setDisable(false);
                break;

        }
    }

    private void moverFlecha(int paso) {

        switch (paso) {
            case -1:
                flechaIndicador.setLayoutX(246);
                flechaIndicador.setLayoutY(37);
                break;
            case 0:
                flechaIndicador.setLayoutX(259);
                flechaIndicador.setLayoutY(89);
                break;
            case 1:
                flechaIndicador.setLayoutX(308);
                flechaIndicador.setLayoutY(135);
                break;
            case 2:
                flechaIndicador.setLayoutX(324);
                flechaIndicador.setLayoutY(193);

                break;
            case 3:
                if ((Conversiones.intToHex(proce1.getIR()).charAt(0) == '7') || (Conversiones.intToHex(proce1.getIR()).charAt(0) == 'f')) {
                    if (proce1.getI() == 0) {
                        flechaIndicador.setLayoutX(239);
                        flechaIndicador.setLayoutY(389);
                    } else {
                        flechaIndicador.setLayoutX(120);
                        flechaIndicador.setLayoutY(389);
                    }
                } else {
                    if (proce1.getI() == 0) {
                        flechaIndicador.setLayoutX(430);
                        flechaIndicador.setLayoutY(361);
                    } else {
                        flechaIndicador.setLayoutX(347);
                        flechaIndicador.setLayoutY(361);
                    }
                }
                break;
            case 4:
                switch (proce1.getInstruccion()) {

                    case "AND":
                        flechaIndicador.setLayoutX(111);
                        flechaIndicador.setLayoutY(574);
                        break;
                    case "ADD":
                        flechaIndicador.setLayoutX(209);
                        flechaIndicador.setLayoutY(574);
                        break;
                    case "LDA":
                        flechaIndicador.setLayoutX(308);
                        flechaIndicador.setLayoutY(574);
                        break;
                    case "ISZ":
                        flechaIndicador.setLayoutX(318);
                        flechaIndicador.setLayoutY(716);

                        break;
                    case "STA":
                        flechaIndicador.setLayoutX(409);
                        flechaIndicador.setLayoutY(574);
                        break;

                    case "BUN":
                        flechaIndicador.setLayoutX(105);
                        flechaIndicador.setLayoutY(716);

                        break;

                    case "BSA":
                        flechaIndicador.setLayoutX(205);
                        flechaIndicador.setLayoutY(716);
                        break;
                }
                break;

            case 5:

                switch (proce1.getInstruccion()) {

                    case "AND":
                        flechaIndicador.setLayoutX(111);
                        flechaIndicador.setLayoutY(624);
                        break;

                    case "ADD":
                        flechaIndicador.setLayoutX(209);
                        flechaIndicador.setLayoutY(624);
                        break;

                    case "LDA":
                        flechaIndicador.setLayoutX(308);
                        flechaIndicador.setLayoutY(624);
                        break;

                    case "ISZ":
                        flechaIndicador.setLayoutX(318);
                        flechaIndicador.setLayoutY(768);
                        break;

                    case "BSA":
                        flechaIndicador.setLayoutX(205);
                        flechaIndicador.setLayoutY(768);
                        break;
                }
                break;

            case 6:
                flechaIndicador.setLayoutX(341);
                flechaIndicador.setLayoutY(833);
                break;
        }
    }

}
