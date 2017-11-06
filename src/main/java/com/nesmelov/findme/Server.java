package com.nesmelov.findme;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.UserDataHandler;

/**
 *
 * @author 70508
 */
@WebServlet(urlPatterns = {"/Server"})
public class Server extends HttpServlet {
    private static final String USERS_FILE = "users.txt";
    
    private static final JSONObject STATUS_OK = new JSONObject("{\"status\": \"ok\"}");
    private static final JSONObject STATUS_NOK = new JSONObject("{\"status\": \"nok\"}");
    private static final JSONObject STATUS_ALREADY_EXISTS = new JSONObject("{\"status\": \"already_exists\"}");
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
    private static Map<Integer, UserInfo> mUsersDataStorage;
    
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JSONObject responseObject = STATUS_NOK;
        try {
            final String action = request.getParameter(ACTION);
            if (ACTION_ADD.equals(action)) {
                final JSONObject body = getBody(request);
                final Integer user = body.getInt(USER);
                if (mUsersDataStorage.containsKey(user)) {
                    responseObject = STATUS_ALREADY_EXISTS;
                } else {
                    mUsersDataStorage.put(user, new UserInfo(false, new Position()));
                    responseObject = STATUS_OK;
                }
            } else if (ACTION_CHECK.equals(action)) {
                final JSONObject body = getBody(request);
                final JSONArray users = body.getJSONArray(USERS);
                
                final JSONArray existsUsers = new JSONArray();
                for (int i = 0; i < users.length(); i++) {
                    final Integer vkId = users.getInt(i);
                    if (mUsersDataStorage.containsKey(vkId)) {
                        existsUsers.put(vkId);
                    }
                }

                final JSONObject jsonObject = new JSONObject();
                jsonObject.put(USERS, existsUsers);
                responseObject = jsonObject;
            } else if (ACTION_SET_VISIBLE.equals(action)) {
                final JSONObject body = getBody(request);
                final Integer user = body.getInt(USER);
                final Boolean visible = body.getBoolean(VISIBLE);
                final UserInfo ui = mUsersDataStorage.get(user);
                if (ui != null) {
                    ui.setVisible(true);
                }
                if (visible) {
                    final Double lat = body.getDouble(LAT);
                    final Double lon = body.getDouble(LON);
                    final Position pos = new Position(lat, lon);
                    mUsersInfo.put(user, pos);
                } else {
                    mUsersInfo.remove(user);
                }
                responseObject = STATUS_OK;
            } else if (ACTION_SET_POS.equals(action)) {
                final JSONObject body = getBody(request);
                final Integer user = body.getInt(USER);
                final Double lat = body.getDouble(LAT);
                final Double lon = body.getDouble(LON);
                final UserInfo ui = mUsersDataStorage.get(user);
                if (ui != null) {
                    ui.getPosition().setLatLon(lat, lon);
                }
                if (mUsersInfo.containsKey(user)) {
                    final Position pos = mUsersInfo.get(user);
                    pos.setLat(lat);
                    pos.setLon(lon);
                    responseObject = STATUS_OK;
                } else {
                    responseObject = STATUS_NOK;
                }
            } else if (ACTION_GET_POS.equals(action)) {
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
                responseObject = responseJson;
            } else if (ACTION_GET_DATABASE_ERRORS.equals(action)) {
                final JSONObject errors = new JSONObject();
                errors.put(ERRORS, /*DataAccess.getInstance().getErrorMessage()*/"");
                responseObject = errors;
            } else if (ACTION_GET_SERVLET_ERRORS.equals(action)) {
                final JSONObject errors = new JSONObject();
                errors.put(ERRORS, mErrorMessage.toString());
                responseObject = errors;
            } else if (ACTION_GET_USERS_INFO.equals(action)) {
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
                responseObject = responseJson;
            }
        } catch (Exception e) {
            mErrorMessage.append("\n").append(e.toString());
        } finally {
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.print(responseObject);
            } catch (Exception e) {
                mErrorMessage.append("\n").append(e.toString());
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
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
       mUsersInfo = new ConcurrentHashMap<Integer, Position>();
       mUsersDataStorage = new ConcurrentHashMap<Integer, UserInfo>();
       /*BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(USERS_FILE));
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                final String[] usersInfo = line.split(";");
                if (usersInfo.length == 4) {
                    final Integer vkId = Integer.valueOf(usersInfo[0]);
                    final Double lat = Double.valueOf(usersInfo[1]);
                    final Double lon = Double.valueOf(usersInfo[2]);
                    final Boolean visible = Boolean.valueOf(usersInfo[3]);
                    final Position pos = new Position(lat, lon);
                    mUsersInfo.put(vkId, pos);
                    mUsersDataStorage.put(vkId, new UserInfo(visible, pos));
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                }
            }
        }*/
    }

    @Override
    public void destroy() {
        /*PrintWriter pw = null;
        try {
            pw = new PrintWriter(USERS_FILE);
            for (final Integer user : mUsersDataStorage.keySet()) {
                final UserInfo userInfo = mUsersDataStorage.get(user);
                pw.println(user + ";" + userInfo.getPosition().getLat() + ";" +
                        userInfo.getPosition().getLon() + ";" + userInfo.getVisibility());
            }
            pw.flush();
        } catch (IOException ignored) {     
        } finally {
            if (pw != null) {
                pw.close();
            }
        }*/
    }
    
    private JSONObject getBody(final HttpServletRequest request) {
        final StringBuffer jb = new StringBuffer();
        String line;
        try {
            final BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (IOException e) {
            mErrorMessage.append("\n").append(e.toString());
        }  
        
        return new JSONObject(jb.toString());
    }
}
