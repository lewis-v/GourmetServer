package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class SqlConnection {  
	private static final String URL="jdbc:mysql://39.108.236.30:3306/gourmet?";//���ݿ������ַ����������deomΪ���ݿ���  
	private static final String NAME="root";//��¼��  
	private static final String PASSWORD="admin211";//����  

	private static SqlConnection Instance;//����
	private Connection conn = null;
	private String log = "";//��־,�Ͽ�����ʱ����

	public static synchronized SqlConnection getInstance(){
		if (Instance == null){
			Instance = new SqlConnection();
		}
		return Instance;
	}

	private SqlConnection(){
		//1.��������  
		try {  
			Class.forName("com.mysql.jdbc.Driver");  
		} catch (ClassNotFoundException e) {  
			log = log + "δ�ܳɹ������������������Ƿ�����������\n";  
			e.printStackTrace();  
		} 
	}

	/**
	 * ��ȡ���ݿ����ӵ�log��־
	 */
	public String getLog(){
		return log;
	}

	/**
	 * �������ݿ�
	 * @param id ��Ҫ���ص���������","�ָ�
	 * @param where ɸѡ����
	 * @param formName ����
	 * @return
	 */
	public synchronized List<JSONObject> search(String id,String where,String formName){
		try {  
			conn = DriverManager.getConnection(URL, NAME, PASSWORD);  
		} catch (SQLException e) {  
			log = log + "��ȡ���ݿ�����ʧ�ܣ�\n";  
			e.printStackTrace();  
		}  
		List<JSONObject> list = new ArrayList<JSONObject>();
		String[] idName = null;
		if (!id.equals("*")){
			idName = id.split(",");
		}
		String sql = "select "+id+" from "+formName;
		if(where != null && where.length()>0){
			sql = sql +" where "+where;  
		}
		log = log + sql + "\n";
		try {
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) { 
				JSONObject js = new JSONObject();
				if (id.equals("*")){//����ȫ��
					ResultSetMetaData myData = rs.getMetaData();
					for (int i = 0; i < myData.getColumnCount();i++){
						js.put(myData.getColumnName(i+1),rs.getString(i+1));
					}
				}else{//�����ض���
					for (int i = 0;i<idName.length; i++){
						js.put(idName[i],rs.getString(i+1));
					}
				}
				list.add(js);
			}  
		} catch (SQLException e) {
			e.printStackTrace();
			log = log + e.getMessage()+"\n";
			if(conn!=null)  
			{  
				try {  
					conn.close();  
				} catch (SQLException e1) {  
					// TODO Auto-generated catch block  
					e1.printStackTrace();  
					conn=null;  
				}  
			}
			return list;
		} 
		if(conn!=null)  
		{  
			try {  
				conn.close();  
			} catch (SQLException e) {  
				// TODO Auto-generated catch block  
				e.printStackTrace();  
				conn=null;  
			}  
		}

		return list;
	}


	public synchronized boolean setData(String id,String data,String where,String formName){
		try {  
			conn = DriverManager.getConnection(URL, NAME, PASSWORD);  
		} catch (SQLException e) {  
			log = log + "��ȡ���ݿ�����ʧ�ܣ�\n";  
			e.printStackTrace();  
		}  
		List<JSONObject> list = new ArrayList<JSONObject>();
		String sql = "UPDATE "+formName+" set "+id+" = "+data;
		if(where != null && where.length()>0){
			sql = sql +" where "+where;  
		}
		log = log + sql + "\n";
		try {
			int result = conn.createStatement().executeUpdate(sql);
			if (result > 0){
				return true;
			}
		} catch (SQLException e1) {
			if(conn!=null)  
			{  
				try {  
					conn.close();  
				} catch (SQLException e) {  
					e.printStackTrace();  
					conn=null;  
				}  
			}
		}
		if(conn!=null)  
		{  
			try {  
				conn.close();  
			} catch (SQLException e) {  
				e.printStackTrace();  
				conn=null;  
			}  
		}

		return false;
	}
}  