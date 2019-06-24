package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject, DefaultWeightedEdge> graph;
	private Map<Integer, ArtObject> idMap;
	private LinkedList<ArtObject> ottima;
	
	public Model() {
		idMap= new HashMap<Integer, ArtObject>();
		graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);	
	}
	
	public void creaGrafo() {
		ArtsmiaDAO dao= new ArtsmiaDAO();
		dao.listObjects(idMap); //mi riempirà la idMap
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.graph, idMap.values());
		
		//aggiungo gli archi
		List<Adiacenza>adj= dao.listAdiacenze();
		
		for(Adiacenza a :adj) {
			ArtObject source= idMap.get(a.getO1());
			ArtObject dest= idMap.get(a.getO2());
			try{
				Graphs.addEdge(graph, source, dest, a.getPeso());
			}catch(Throwable t) {}
		}
		System.out.println("Grafo creato: "+ graph.vertexSet().size()+ " vertici e "+ graph.edgeSet().size()+" archi");
	}

	public int getVertexSize() {
		// TODO Auto-generated method stub
		return graph.vertexSet().size();
	}

	public int getEdgeSixe() {
		// TODO Auto-generated method stub
		return graph.edgeSet().size();
	}
	
	public Set<ArtObject> componenteConnessa(ArtObject input) {
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> c = new ConnectivityInspector<ArtObject, DefaultWeightedEdge>(graph);
		return c.connectedSetOf(input);
	}

	public ArtObject cercaOggetto(int id) {
		ArtObject res=null;
		for(ArtObject a :graph.vertexSet()) {
			if(a.getId()==id)
				res=a;
		}
		return res;
	}
	public int numeroConnessi(ArtObject input) {
		return componenteConnessa(input).size();
	}
	public List<ArtObject> getPercorsoPesoMassimo(int LUN, ArtObject partenza){
		this.ottima = new LinkedList<ArtObject>();
		List<ArtObject> parziale = new LinkedList<ArtObject>();
		parziale.add(partenza);
	
		cercaPercorso(0,LUN, parziale, partenza);
		Collections.sort(ottima);
		return this.ottima;
	}

	private void cercaPercorso(int step, int LUN, List<ArtObject> parziale, ArtObject partenza ) {
		
		//vedere se la soluzione corrente è migliore della ottima corrente
		if(step+1==LUN) {
			if(pesoMax(parziale)>pesoMax(ottima)) {
				this.ottima = new LinkedList<ArtObject>(parziale);
			}
		}
				
		//ottengo tutti i candidati
		List<ArtObject> candidati = new LinkedList<ArtObject>(this.trovaCandidati(partenza, parziale));
		
		for(ArtObject candidato : candidati) {
			if(!parziale.contains(candidato)) {
				//è un candidato che non ho ancora considerato
				parziale.add(candidato);
				this.cercaPercorso(step+1, LUN, parziale, partenza);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private LinkedList<ArtObject> trovaCandidati(ArtObject partenza, List<ArtObject> parziale) {
		LinkedList<ArtObject> list= new LinkedList<ArtObject>();
		for(ArtObject a: Graphs.successorListOf(graph, parziale.get(parziale.size()-1))) {
			if(a.getClassification().equals(partenza.getClassification())) {
				list.add(a);
			}
		}
		return list;
	}

	private double pesoMax(List<ArtObject> list) {
		double peso=0;
		for(ArtObject a:list) {
			if(list.indexOf(a) != (list.size()-1) ) {
			peso+=graph.getEdgeWeight(graph.getEdge(a, list.get(list.indexOf(a)+1)));
			}
		}
			
		return peso;
	}
}
