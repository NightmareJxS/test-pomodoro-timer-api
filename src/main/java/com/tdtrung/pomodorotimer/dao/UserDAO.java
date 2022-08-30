/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.dao;

import com.tdtrung.pomodorotimer.model.Task;
import com.tdtrung.pomodorotimer.model.User;
import com.tdtrung.pomodorotimer.utils.DBUtils;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jason 2.0
 */
public class UserDAO {

        private static final String GET_ALL_USER = "SELECT Id, Email, [Password], TimeFocusToday, TimeFocusThisWeek FROM [User] ";
        private static final String GET_A_PUBLIC_PROFILE_USER = "SELECT Id, Email, [Password], TimeFocusToday, TimeFocusThisWeek FROM [User] WHERE Id = ? ";
        private static final String LOGIN = "SELECT Id, Email, [Password], TimeFocusToday, TimeFocusThisWeek FROM [User] WHERE Id = ? AND [Password] = ? ";
        private static final String CHECK_DUPLICATE = "SELECT Email FROM [User] WHERE Id = ? ";
        private static final String CREATE_NEW_USER = "INSERT INTO [User](Id, Email, [Password], TimeFocusToday, TimeFocusThisWeek) VALUES (?, ?, ?, ?, ?) ";

        public List<User> getAllUser() throws SQLException {
                List<User> listUser = new ArrayList<>();
                Connection conn = null;
                PreparedStatement ptm = null;
                ResultSet rs = null;

                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(GET_ALL_USER);
                                rs = ptm.executeQuery();
                                while (rs.next()) {
                                        String userId = rs.getString("Id");
                                        String email = rs.getString("Email");
                                        String password = "******";
                                        int timeFocusToday = rs.getInt("TimeFocusToday");
                                        int timeFocusThisWeek = rs.getInt("TimeFocusThisWeek");
                                        listUser.add(new User(userId, email, password, timeFocusToday, timeFocusThisWeek));
                                }
                        }

                } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                } finally {
                        if (rs != null) {
                                if (rs.next()) {
                                        rs.close();
                                }
                                if (ptm != null) {
                                        ptm.close();
                                }
                                if (conn != null) {
                                        conn.close();
                                }
                        }
                }
                return listUser;
        }

        public User getAPublicUserProfile(String id) throws SQLException {
                User user = null;
                Connection conn = null;
                PreparedStatement ptm = null;
                ResultSet rs = null;

                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(GET_A_PUBLIC_PROFILE_USER);
                                ptm.setString(1, id);
                                rs = ptm.executeQuery();
                                if (rs.next()) {
                                        String userId = rs.getString("Id");
                                        String email = rs.getString("Email");
                                        String password = "******";
                                        int timeFocusToday = rs.getInt("TimeFocusToday");
                                        int timeFocusThisWeek = rs.getInt("TimeFocusThisWeek");
                                        user = new User(userId, email, password, timeFocusToday, timeFocusThisWeek);
                                }
                        }

                } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                } finally {
                        if (rs != null) {
                                if (rs.next()) {
                                        rs.close();
                                }
                                if (ptm != null) {
                                        ptm.close();
                                }
                                if (conn != null) {
                                        conn.close();
                                }
                        }
                }
                return user;
        }

        public User checkLogin(String id, String password) throws SQLException {
                User user = null;
                Connection conn = null;
                PreparedStatement ptm = null;
                ResultSet rs = null;

                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(LOGIN);
                                ptm.setString(1, id);
                                ptm.setString(2, password);
                                rs = ptm.executeQuery();
                                if (rs.next()) {
                                        String email = rs.getString("Email");
                                        int timeFocusToday = rs.getInt("TimeFocusToday");
                                        int timeFocusThisWeek = rs.getInt("TimeFocusThisWeek");
                                        user = new User(id, email, password, timeFocusToday, timeFocusThisWeek);
                                }
                        }

                } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                } finally {
                        if (rs != null) {
                                if (rs.next()) {
                                        rs.close();
                                }
                                if (ptm != null) {
                                        ptm.close();
                                }
                                if (conn != null) {
                                        conn.close();
                                }
                        }
                }
                return user;
        }

        public boolean checkDublicate(String userId) throws SQLException {
                boolean check = false;
                Connection conn = null;
                PreparedStatement ptm = null;
                ResultSet rs = null;
                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(CHECK_DUPLICATE);
                                ptm.setString(1, userId);
                                rs = ptm.executeQuery();
                                if (rs.next()) {
                                        check = true;
                                }
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        if (rs != null) {
                                rs.close();
                        }
                        if (ptm != null) {
                                ptm.close();
                        }
                        if (conn != null) {
                                conn.close();
                        }
                }

                return check;
        }

        public boolean createNewUser(User user) throws SQLException {
                boolean check = false;
                Connection conn = null;
                PreparedStatement ptm = null;
                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(CREATE_NEW_USER);
                                ptm.setString(1, user.getId());
                                ptm.setString(2, user.getEmail());
                                ptm.setString(3, user.getPassword());
                                ptm.setInt(4, 0);
                                ptm.setInt(5, 0);
                                check = ptm.executeUpdate() > 0 ? true : false;
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        if (ptm != null) {
                                ptm.close();
                        }
                        if (conn != null) {
                                conn.close();
                        }
                }

                return check;
        }

}
