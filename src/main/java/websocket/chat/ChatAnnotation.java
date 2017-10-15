package websocket.chat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;*/

/**
 *
 * @author 70508
 */
@WebServlet(urlPatterns = {"/Server"})
public class ChatAnnotation extends HttpServlet {
    private static final String STATUS_OK = "{\"status\": \"ok\"}";
    private static final String STATUS_NOK = "{\"status\": \"nok\"}";
    private static final String STATUS_ALREADY_EXISTS = "{\"status\": \"already_exists\"}";
    private static final String ACTION = "action";
    private static final String ACTION_CHECK = "check";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_SET_POS = "set_pos";
    private static final String ACTION_GET_POS = "get_pos";
    private static final String ACTION_SET_VISIBLE = "set_visible";
    private static final String ACTION_GET_SERVLET_ERRORS = "get_servlet_errors";
    private static final String ACTION_GET_DATABASE_ERRORS = "get_database_errors";
    private static final String ACTION_GET_USERS_INFO = "get_users_info";
    
    private static final String USER = "user";
    private static final String USERS = "users";
    private static final String VISIBLE = "visible";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String ERRORS = "errors";
    private static final String USERS_INFO = "info";
    
    private static Map<Integer, Position> mUsersInfo;
    
    private final StringBuilder mErrorMessage = new StringBuilder();
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String responseString = STATUS_NOK;
        //try {
            //final String action = request.getParameter(ACTION);
            /*switch (action) {
                case ACTION_ADD: {
                    final JSONObject body = getBody(request);
                    final Integer user = body.getInt(USER);*/
                    /*if (DataAccess.getInstance().addUser(user)) {*/
                        //responseString = STATUS_OK;
                    /*} else {
                        responseString = STATUS_ALREADY_EXISTS;
                    }*/
                    /*break;
                }
                case ACTION_CHECK: {
                    final JSONObject body = getBody(request);
                    final JSONArray users = body.getJSONArray(USERS);
                    //final JSONArray existsUsers = DataAccess.getInstance().checkUsers(users);
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put(USERS, new JSONArray()existsUsers*///);
                    /*responseString = jsonObject.toString();
                    break;
                }
                case ACTION_SET_VISIBLE: {
                    final JSONObject body = getBody(request);
                    final Integer user = body.getInt(USER);
                    final Boolean visible = body.getBoolean(VISIBLE);
                    if (visible) {
                        final Double lat = body.getDouble(LAT);
                        final Double lon = body.getDouble(LON);
                        final Position pos = new Position(lat, lon);
                        mUsersInfo.put(user, pos);
                    } else {
                        mUsersInfo.remove(user);
                    }
                    responseString = STATUS_OK;
                    break;
                }
                case ACTION_SET_POS: {
                    final JSONObject body = getBody(request);
                    final Integer user = body.getInt(USER);
                    final Double lat = body.getDouble(LAT);
                    final Double lon = body.getDouble(LON);
                    
                    if (mUsersInfo.containsKey(user)) {
                        final Position pos = mUsersInfo.get(user);
                        pos.setLat(lat);
                        pos.setLon(lon);
                        responseString = STATUS_OK;
                    } else {
                        responseString = STATUS_NOK;
                    }
                    break;
                }
                case ACTION_GET_POS: {
                    final JSONObject body = getBody(request);
                    final JSONArray users = body.getJSONArray(USERS);
                    
                    final JSONObject responseJson = new JSONObject();
                    final JSONArray responseUsers = new JSONArray();
                    for (int i = 0; i < users.length(); i++) {
                        final Integer user = users.getInt(i);
                        if (mUsersInfo.containsKey(user)) {
                            final Position pos = mUsersInfo.get(user);
                            final JSONObject o = new JSONObject();
                            o.put(USER, user);
                            o.put(LAT, pos.getLat());
                            o.put(LON, pos.getLon());
                            responseUsers.put(o);
                        }
                    }
                    responseJson.put(USERS, responseUsers);
                    responseString = responseJson.toString();
                    break;
                }
                case ACTION_GET_DATABASE_ERRORS: {
                    final JSONObject errors = new JSONObject();*/
                    //errors.put(ERRORS, /*DataAccess.getInstance().getErrorMessage()*/"");
                    /*responseString = errors.toString();
                    break;
                }
                case ACTION_GET_SERVLET_ERRORS: {
                    final JSONObject errors = new JSONObject();
                    errors.put(ERRORS, mErrorMessage.toString());
                    responseString = errors.toString();
                    break;
                }
                case ACTION_GET_USERS_INFO: {
                    final JSONObject responseJson = new JSONObject();
                    final JSONArray responseUsers = new JSONArray();
                    for (final Integer user : mUsersInfo.keySet()) {     
                        final Position pos = mUsersInfo.get(user);
                        final JSONObject o = new JSONObject();
                        o.put(USER, user);
                        o.put(LAT, pos.getLat());
                        o.put(LON, pos.getLon());
                        responseUsers.put(o);
                    }
                    responseJson.put(USERS, responseUsers);
                    responseString = responseJson.toString();
                    break;
                }
                default:
                    break;*/
            //}
        /*} catch (Exception e) {
            mErrorMessage.append("\n").append(e.toString());
        } finally {*/
        
        PrintWriter out = response.getWriter();
        out.println(responseString);
        out.flush();
        out.close();
            /*try () {
                
            } catch (Exception e) {
                mErrorMessage.append("\n").append(e.toString());
            }*/
        /*}*/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    @Override
    public void init() {
        //mUsersInfo = new ConcurrentHashMap<>();//DataAccess.getInstance().getAllRecords();
    }

    @Override
    public void destroy() {
        //DataAccess.getInstance().refreshRecords(mUsersInfo);
    }
    
    /*private JSONObject getBody(final HttpServletRequest request) {
        final StringBuffer jb = new StringBuffer();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (IOException e) {
            mErrorMessage.append("\n").append(e.toString());
        }  
        
        return new JSONObject(jb.toString());
    }*/
}