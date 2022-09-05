/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.dao;

import com.tdtrung.pomodorotimer.model.Task;
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
public class TaskDAO {

        private static final String GET_TASK_AFTER_LOGIN = "SELECT Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status] FROM Task WHERE UserId = ? ";
        private static final String CREATE_NEW_TASK = "INSERT INTO Task(UserId, [Name], Duration, StartTime, CompleteTime, [Status]) VALUES (?, ?, ?, ?, ?, ?) ";
        private static final String CHECK_AVAILABLE = "SELECT [Name] FROM Task WHERE Id = ? AND UserId = ? ";
        private static final String UPDATE_TASK = "UPDATE Task SET [Name]=?, Duration=?, StartTime=?, CompleteTime=?, [Status]=? WHERE Id=?   ";
        private static final String DELETE_TASK = "DELETE Task WHERE Id = ? ";

        public List<Task> getTaskPerUser(String userId) throws SQLException {
                List<Task> tasklist = new ArrayList<>();
                Connection conn = null;
                PreparedStatement ptm = null;
                ResultSet rs = null;

                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(GET_TASK_AFTER_LOGIN);
                                ptm.setString(1, userId);
                                rs = ptm.executeQuery();
                                while (rs.next()) {
                                        int taskId = rs.getInt("Id");
                                        String taskName = rs.getString("Name");
                                        int duration = rs.getInt("Duration");
                                        Date startTime = rs.getDate("StartTime");
                                        Date completeTime = rs.getDate("CompleteTime");
                                        int status = rs.getInt("Status");
                                        tasklist.add(new Task(taskId, userId, taskName, duration, startTime, completeTime, status));
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
                return tasklist;
        }

        public boolean addNewTask(String userId, Task task) throws SQLException {
                boolean check = false;
                Connection conn = null;
                PreparedStatement ptm = null;
                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(CREATE_NEW_TASK);
                                ptm.setString(1, userId);
                                ptm.setString(2, task.getName());
                                ptm.setInt(3, task.getDuration());
                                ptm.setDate(4, task.getStartTime());
                                ptm.setDate(5, task.getCompleteTime());
                                ptm.setInt(6, task.getStatus());
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

        public boolean checkAvailable(int taskId, String userId) throws SQLException {
                boolean check = false;
                Connection conn = null;
                PreparedStatement ptm = null;
                ResultSet rs = null;
                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(CHECK_AVAILABLE);
                                ptm.setInt(1, taskId);
                                ptm.setString(2, userId);
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
        
        public boolean updateTask(Task task) throws SQLException {
                boolean check = false;
                Connection conn = null;
                PreparedStatement ptm = null;
                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(UPDATE_TASK);
                                ptm.setString(1, task.getName());
                                ptm.setInt(2, task.getDuration());
                                ptm.setDate(3, task.getStartTime());
                                ptm.setDate(4, task.getCompleteTime());
                                ptm.setInt(5, task.getStatus());
                                ptm.setInt(6, task.getId());
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
        
        
        public boolean deleteTask(int taskId) throws SQLException {
                boolean check = false;
                Connection conn = null;
                PreparedStatement ptm = null;
                try {
                        conn = DBUtils.getConnection();
                        if (conn != null) {
                                ptm = conn.prepareStatement(DELETE_TASK);
                                ptm.setInt(1, taskId);
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
