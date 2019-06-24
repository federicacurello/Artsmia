/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader
	private int id;

	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		this.model.creaGrafo();
		txtResult.appendText("Grafo creato "+ model.getVertexSize()+ " vertici e "+ model.getEdgeSixe()+ " archi");
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {
		try {
			id= Integer.parseInt(txtObjectId.getText());
			
		
		if(model.cercaOggetto(id)==null) {
			txtResult.appendText("\nErrore, identificativo dell'oggetto errato!\n");
			return;
		}
		else {
			txtResult.appendText("\nOggetto trovato!\n");
		}
		txtResult.appendText("\nComponente connessa: \n");
		for(ArtObject a: model.componenteConnessa(model.cercaOggetto(id))){
			txtResult.appendText(a.toString() +" - ");
		}
		
		txtResult.appendText("\nNumero di componenti connesse: "+model.numeroConnessi(model.cercaOggetto(id))+"\n");
		setBox(model.numeroConnessi(model.cercaOggetto(id)));
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return;
		}
	}

	private void setBox(int numeroConnessi) {
		boxLUN.getItems().removeAll();
		for(int i=2; i<=numeroConnessi; i++) {
		boxLUN.getItems().add(i);
		}
	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		txtResult.appendText("\nCammino di peso massimo: :\n");
		for(ArtObject a:model.getPercorsoPesoMassimo(boxLUN.getValue(),model.cercaOggetto(id))) {
			txtResult.appendText(a.toString()+" \n ");
		}
		txtResult.appendText("Peso massimo: "+ model.getPercorsoPesoMassimo(boxLUN.getValue(),model.cercaOggetto(id)).size());
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}

	public void setModel(Model model) {
		this.model=model;
		
	}
}
