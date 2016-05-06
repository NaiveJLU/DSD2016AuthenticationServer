package edu.jlu.cs.servlet;


import org.json.*;
import edu.jlu.cs.module.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Train
 */
public class Train extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Train() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DBCall dbcall=new DBCall();
		FacCall facCall=new FacCall();
		DecodeSave decodeSave=new DecodeSave();
		int responseCode=500;
		SaveUserImages2DB sui2db=new SaveUserImages2DB();
		JSONObject respJsonO=new JSONObject();
		String usr=request.getParameter("username");
		String passwd=request.getParameter("passwordMd5");
		String imageJson=request.getReader().readLine();
		JSONObject imageJsonO=new JSONObject(imageJson);
		String images=imageJsonO.getString("image");
		decodeSave.work(usr,images);
		String dbs=dbcall.work(usr, passwd);
		JSONObject dbjsonO= new JSONObject(dbs);
		int code=dbjsonO.getInt("code");
		if(code==201)
		{
			String indentifier=dbjsonO.getString("userFACIDs");
			String internal_ID=dbjsonO.getString("internal_ID");
			String facs=facCall.work(internal_ID,indentifier,images);
			JSONObject facjsonO=new JSONObject(facs);
			int faccode=facjsonO.getInt("code");
			if(faccode==201)
			{
				String user_FAC_ids=facjsonO.getString("user_FAC_ids");
				Boolean train_status=facjsonO.getBoolean("train_status");
				String suistr=sui2db.work(internal_ID, user_FAC_ids, train_status, images);
				JSONObject sui2dbJsonO=new JSONObject(suistr);
				responseCode=201;
			}
		}
		int appcode=dbjsonO.getInt("appCode");
		String Message=dbjsonO.getString("message");
		respJsonO.put("Message", Message);
		respJsonO.put("code", responseCode);
		response.setStatus(responseCode);
	}
}
