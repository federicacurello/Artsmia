package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.get("object_id")==null) {
				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				idMap.put(artObj.getId(), artObj);
				result.add(artObj);
				} else {
					result.add(idMap.get(res.getInt("object_id")));
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Adiacenza> listAdiacenze(){
		String sql= "SELECT eo1.object_id AS o1, eo2.object_id AS o2, COUNT(*) AS cnt " + 
				"FROM exhibition_objects eo1, exhibition_objects eo2 " + 
				"WHERE eo1.exhibition_id= eo2.exhibition_id " + 
				"  AND eo2.object_id>eo1.object_id " + 
				"GROUP BY eo1.object_id, eo2.object_id ";
		Connection conn= DBConnect.getConnection();
		List<Adiacenza> adj= new LinkedList<Adiacenza>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				adj.add(new Adiacenza(res.getInt("o1"), res.getInt("o2"), res.getInt("cnt")));}
			conn.close();
			return adj;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
			
		}
	}
	
}
