/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tdtrung.pomodorotimer.config.CORSFilter;
import com.tdtrung.pomodorotimer.dao.TaskDAO;
import com.tdtrung.pomodorotimer.dao.UserDAO;
import com.tdtrung.pomodorotimer.model.Task;
import com.tdtrung.pomodorotimer.model.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jason 2.0
 */
public class TimerResource {
        
        @Path("/users")
        public static class Users {

                @Context
                UriInfo ui; // To store url from Tomcat (when create new user)

                @GET
                @Produces(MediaType.APPLICATION_JSON)
                public List<User> getAll() {
                        List<User> listUser = new ArrayList();
                        try {
                                UserDAO dao = new UserDAO();
                                listUser = dao.getAllUser();
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                        return listUser;
                }

//                @GET
//                @Path("one")
//                @Produces(MediaType.APPLICATION_JSON)
//                public User getUser() {
//                        return new User("PTU12345", "testingGetOneUser@test.com", "000000", 48830, 876734); // (1st method)
//                }
                @GET
                @Path("{id}")
                @Produces(MediaType.APPLICATION_JSON)
                public Response getBy(@PathParam("id") String id) {
                        User user = null;
                        try {
                                UserDAO dao = new UserDAO();
                                user = dao.getAPublicUserProfile(id);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        Response msg = Response.ok().entity(user).build();
                        return msg; // Create response msg (2nd method)
                }

                @POST
                @Path("login") // Can disable this as : No need to use Path as direct submit POST method will automactic add new User (POST != GET)
                @Consumes(MediaType.APPLICATION_JSON)
                @Produces(MediaType.APPLICATION_JSON)
                public Response login(User user) {
                        User userInfo = null;
                        List<Task> userTask = new ArrayList<>();

                        try {
                                UserDAO dao = new UserDAO();
                                TaskDAO tdao = new TaskDAO();
                                userInfo = dao.checkLogin(user.getId(), user.getPassword());
                                if (userInfo != null) {
                                        userTask = tdao.getTaskPerUser(user.getId());
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

//                        String[] multipleEntity = new String[2];
//                        multipleEntity[0]= userInfo.toString();
//                        multipleEntity[1]= userTask.toString();
//                        String multipleEntity = "[" + userInfo + "," + userTask + "]"; // is this valid JSON object?
                        

                        //ref : https://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
                        String multipleEntity = "";
                        try {
                                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                                String json1 = ow.writeValueAsString(userInfo);
                                String json2 = ow.writeValueAsString(userTask);
                                multipleEntity = "{" + "\"userInfo\": " + json1 + ",\n\"tasklist\":" + json2 + "}";
                        } catch (Exception e) {
                        }

                        Response msg = Response.ok().entity(multipleEntity).build();

                        return msg;
                }

                @POST
                @Path("create") // Can disable this as : No need to use Path as direct submit POST method will automactic add new User (POST != GET)
                @Consumes(MediaType.APPLICATION_JSON)
                @Produces(MediaType.APPLICATION_JSON)
                public Response createNewUser(User user) throws URISyntaxException {
                        int DUBLICATE = 2;
                        int ERROR = 0;
                        int SUCCESS = 1;
                        int status = ERROR;
                        try {
                                UserDAO dao = new UserDAO();
                                boolean checkDuplicate = dao.checkDublicate(user.getId());
                                if (!checkDuplicate) {
                                        if (user.getId().length() >= 8) {
                                                boolean checkInsert = dao.createNewUser(user);
                                                if (checkInsert) {
                                                        status = SUCCESS;
                                                }
                                        }

                                } else {
                                        status = DUBLICATE;
                                }

                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        // Return new api url to new public user
                        // Use Postman to check
                        // Ref: https://youtu.be/nKXAKEQ2Y58?t=5218 
                        URI url = new URI(ui.getPath().replace("create", "") + "users/" + user.getId());

                        Response msg = null;
                        switch (status) {
                                case 0:
                                        msg = Response.status(404).build(); // created custom return code for failed creation (doesn't have to be 404)
                                        break;
                                case 1:
                                        msg = Response.created(url).build();
                                        break;
                                case 2:
                                        msg = Response.status(999).entity(user).build(); // created custom return code for dublicated userid
                                        break;
                        }

                        return msg;
                }

                @POST
                @Path("{userId}/addTask") // Can disable this as : No need to use Path as direct submit POST method will automactic add new User (POST != GET)
                @Consumes(MediaType.APPLICATION_JSON)
                @Produces(MediaType.APPLICATION_JSON)
                public Response addNewTask(@PathParam("userId") String userId, Task task) throws URISyntaxException {
                        int ERROR = 0;
                        int SUCCESS = 1;
                        int status = ERROR;
                        try {
                                TaskDAO dao = new TaskDAO();
                                boolean checkCreateTask = dao.addNewTask(userId, task); // can only add task after login for valid userId
                                if (checkCreateTask) {
                                        status = SUCCESS;
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        // Return new api url to new public user
                        // Use Postman to check
                        // Ref: https://youtu.be/nKXAKEQ2Y58?t=5218 
                        URI url = new URI(ui.getPath().replace("/addTask", ""));

                        Response msg = null;
                        switch (status) {
                                case 0:
                                        msg = Response.status(404).build(); // created custom return code for failed creation (doesn't have to be 404)
                                        break;
                                case 1:
                                        msg = Response.created(url).entity(task).build();
                                        break;
                        }

                        return msg;
                }

        }

//        @Path("/tasks")
//        public static class Tasks {
//
//                @Context
//                UriInfo ui; // To store url from Tomcat (when create new user)
//
//                @GET
//                @Produces(MediaType.APPLICATION_JSON)
//                public List<Task> getAll() {
//                        // tmp list (not connected to DB)
//                        List<Task> list = new ArrayList();
//                        list.add(new Task("17", "SE160947", "OJT Signup", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 0));
//                        list.add(new Task("18", "SE160947", "Something1", 600, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 0));
//                        list.add(new Task("19", "SE160947", "Something2", 120, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 0));
//                        list.add(new Task("12", "SE160947", "Something3", 172800, Date.valueOf("2022-08-16"), Date.valueOf("2022-08-18"), 0));
//                        list.add(new Task("1", "SE160947", "Something4", 1209600, Date.valueOf("2022-08-18"), Date.valueOf("2022-09-01"), 1));
//                        list.add(new Task("5", "PTU02847", "Something5", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 1));
//                        list.add(new Task("55", "PTU43252", "Something6", 120, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 3));
//                        list.add(new Task("48", "PTU65438", "Something7", 120, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 2));
////                        Date date = Date.valueOf("2022-08-15");
////                        System.out.println(date);
//                        // Uses https://www.epochconverter.com/ if can't understand number
//                        return list;
//                }
//                
//                @GET
//                @Path("one")
//                @Produces(MediaType.APPLICATION_JSON)
//                public Task getTask() {
//                        return new Task("17", "SE160947", "OJT Signup", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 0); // (1st method)
//                }
//
//                @GET
//                @Path("{userId}/{taskId}")
//                @Produces(MediaType.APPLICATION_JSON)
//                public Response getBy(@PathParam("userId") String userId, @PathParam("taskId") String taskId) {
//                        // Can replace with search function in dao
//                        Task task = new Task(taskId, userId, "OJT Signup", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 0);
//
//                        Response msg = Response.ok().entity(task).build();
//                        return msg; // Create response msg (2nd method)
//                }
//
//                @POST
////        @Path("create") // No need to use Path as direct submit POST method will automactic add new User (POST != GET)
//                @Consumes(MediaType.APPLICATION_JSON)
//                public Response add(Task task) throws URISyntaxException {
//                        // Can replace with create method in dao
//                        // tmp sout for understading
//                        System.out.println("Created a new user: " + task);
//
//                        // Return new api url to new task
//                        // Use Postman to check
//                        // Ref: https://youtu.be/nKXAKEQ2Y58?t=5218 
//                        URI url = new URI(ui.getBaseUri() + "tasks/" + task.getUserId() + "/" + task.getId());
//
//                        Response msg = Response.created(url).build();
//
//                        return msg;
//                }
//        }
}
