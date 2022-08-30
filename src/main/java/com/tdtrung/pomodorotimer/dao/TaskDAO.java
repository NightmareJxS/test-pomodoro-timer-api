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
                                        int taskId = 0; // the tracking id is for the server side, no need to show for the client
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

}
