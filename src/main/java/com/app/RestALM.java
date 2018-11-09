package com.app;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import com.app.qc.rest.examples.infrastructure.Entities;
import com.app.qc.rest.examples.infrastructure.Entity;
import com.app.qc.rest.examples.infrastructure.Entity.Fields.Field;
import com.app.qc.rest.examples.infrastructure.EntityMarshallingUtils;
//http://cbigdc-isapk901.cbi.net:8080/qcbin/authentication-point/authenticate
//td://wms_tms.wine_operations.cbigdc-isapk901.cbi.net:8080/qcbin/AnalysisModule-00000002220154988?EntityType=IAnalysisItem&EntityID=1096
public class RestALM {
    private static final String almURL = "http://cbigdc-isapk901.cbi.net:8080/qcbin";
    private static final String isAuthenticatedPath = "authentication-point/authenticate";
    private static final String qcSiteSession = "rest/site-session";
    private static final String logoutPath = "authentication-point/logout";
    private static String lswoocookie;
    private static String qcsessioncookie;

    public static String strDomain = "WINE_OPERATIONS";
    public static String strProject = "WMS_TMS_1";
    public static String strUserName = "jchandran";
    public static String strPassword = "jchandran";

    public static Client client;
    public static WebTarget target;
    public static Invocation.Builder invocationBuilder;
    public static Response res;


    private static String getEncodedAuthString() {
        String auth = strUserName + ":" + strPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);

        return authHeader;
    }

    public static void main(String args[]) throws JAXBException{
        client = ClientBuilder.newBuilder().build();

        /* Get LWSSO Cookie */
        target = client.target(almURL).path(
                isAuthenticatedPath);
        invocationBuilder = target.request(new String[] { "application/xml" });
        invocationBuilder.header("Authorization", getEncodedAuthString());
        res = invocationBuilder.get();
        lswoocookie = res.getCookies().get("LWSSO_COOKIE_KEY").getValue();

        /* Get QCSession Cookie */
        target = client.target(almURL).path(qcSiteSession);
        invocationBuilder = target
                .request();
        invocationBuilder.cookie("LWSSO_COOKIE_KEY", lswoocookie);
        res = invocationBuilder.post(null);
        qcsessioncookie = res.getCookies().get("QCSession").getValue();

        /* Get the first defect */
        String midPoint = "rest/domains/" + strDomain + "/projects/" + strProject;
        target = client.target(almURL).path(midPoint).path("defects");
        invocationBuilder = target
                .request(new String[] { "application/xml" });
        invocationBuilder.cookie("LWSSO_COOKIE_KEY", lswoocookie);
        invocationBuilder.cookie("QCSession", qcsessioncookie);
        res = invocationBuilder.get();
        String ss= res.getEntity().toString();
		

        System.out.println(res.toString());
        String responseAsString = res.readEntity(String.class);

      //  System.out.println( responseAsString);
        
		String postedEntityReturnedXml = res.toString();
		Entities entities =  EntityMarshallingUtils.marshal(Entities.class,
				responseAsString);
 
		
		 // Print all names available in entity defect to screen.
		StringBuilder sb=new StringBuilder("<html><body><head><style>\r\n" + 
				"table {\r\n" + 
				"    border-collapse: collapse;\r\n" + 
				"    width: 100%;\r\n" + 
				"    font-size:10px;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"th, td {\r\n" + 
				"    text-align: left;\r\n" + 
				"    padding: 8px;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"tr:nth-child(even) {background-color: #f2f2f2;}\r\n" + 
				"</style></head><table border='1'>");
		sb.append("<tr><td>SR no # </td><td>description </td><td>priority </td><td>dev-comments</td><td>status </td><td>closing-date</td><td>owner </td><td>subject </td><td>creation-time</td><td>last-modified</td><td>severity</td></tr>");
          int count=1;
          int repoSize=entities.getEntity().size();
          System.out.println("Reports size: "+repoSize);
		for (Entity entity : entities.getEntity()) {
			List<Field> fields = entity.getFields().getField();

			sb.append("<tr>");
			sb.append("<td>").append(count).append("</td>");

			for (Field field : fields) {
				//if(field.getName().equals("status") && !field.getValue().toString().equals("[Closed]")) {
					System.out.println(field.getName() + " : " + field.getValue());
					

					if(field.getName().equals("status")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("closing-date")) {
					//	sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("subject")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("owner")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("creation-time")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("last-modified")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("severity")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("dev-comments")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("description")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					if(field.getName().equals("priority")) {
						//sb.append("<td>").append(field.getName()).append("</td>");
						sb.append("<td>").append(field.getValue().toString().replace("[","").replace("]", "")).append("</td>");
					}
					
					
					
				//}
					//status : 
			}
			
			sb.append("</tr>");
			System.out.println("---------------------------------------");

			count++;
			if(count==5)
			break;


		}
		sb.append("</table></body></html>");
		writeFile(sb.toString(),"C:/@workspace/reports/repo.html");
        /* Logout */
        target = client.target(almURL).path(logoutPath);
        invocationBuilder = target
                .request();
        invocationBuilder.cookie("LWSSO_COOKIE_KEY", lswoocookie);
        invocationBuilder.cookie("QCSession", qcsessioncookie);
        res = invocationBuilder.post(null);
    }   
    
    
    /***
	 * Write file
	 * @param data {@link String}
	 * @param filePath {@link String}
	 */
	public static void writeFile(String data, String filePath) {
		Path filepath = Paths.get(filePath);
		byte[] bytes = data.getBytes();
		try (OutputStream out = Files.newOutputStream(filepath)) {
			out.write(bytes);
		} catch (Exception e) {
               e.fillInStackTrace();
		}
	}

}
