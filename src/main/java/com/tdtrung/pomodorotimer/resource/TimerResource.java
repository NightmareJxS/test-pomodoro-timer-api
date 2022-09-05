/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tdtrung.pomodorotimer.dao.TaskDAO;
import com.tdtrung.pomodorotimer.dao.UserDAO;
import com.tdtrung.pomodorotimer.model.ErrorMessage;
import com.tdtrung.pomodorotimer.model.Task;
import com.tdtrung.pomodorotimer.model.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
                        ErrorMessage error = new ErrorMessage();
                        int ERROR = 0;
                        int SUCCESS = 1;
                        int INVALID_CREDENTIALS = 2;
                        int status = ERROR;

                        try {
                                UserDAO dao = new UserDAO();
                                TaskDAO tdao = new TaskDAO();
                                userInfo = dao.checkLogin(user.getId(), user.getPassword());
                                if (userInfo != null) {
                                        userTask = tdao.getTaskPerUser(user.getId());
                                        status = SUCCESS;
                                } else {
                                        status = INVALID_CREDENTIALS;
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

                        Response msg = null;

                        switch (status) {
                                case 0:
                                        error.setMessage("Login unsuccessfully, please contact us for help!");
                                        msg = Response.status(601).entity(error).build(); // created custom return code for failed login 
                                        break;
                                case 1:
                                        msg = Response.ok().entity(multipleEntity).build();
                                        break;
                                case 2:
                                        error.setMessage("Invalid userID or Password!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for Invalid userID or Password!
                                        break;
                        }

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
                        int INVALID_USER_ID_LENGTH = 3;
                        int status = ERROR;
                        ErrorMessage error = new ErrorMessage();
                        try {
                                UserDAO dao = new UserDAO();
                                boolean checkDuplicate = dao.checkDublicate(user.getId());
                                if (!checkDuplicate) {
                                        if (user.getId().length() >= 8) {
                                                boolean checkInsert = dao.createNewUser(user);
                                                if (checkInsert) {
                                                        status = SUCCESS;
                                                }
                                        } else {
                                                status = INVALID_USER_ID_LENGTH;
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
                                        error.setMessage("Fail to create account, please contact us for help!");
                                        msg = Response.status(601).entity(error).build(); // created custom return code for failed creation
                                        break;
                                case 1:
                                        msg = Response.created(url).build();
                                        break;
                                case 2:
                                        error.setMessage("Duplicated User ID!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for dublicated userid
                                        break;
                                case 3:
                                        error.setMessage("UserID must be longer or equal to 8 characters!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for userID not long enough
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
                        int USER_NOT_FOUND = 2;
                        int status = ERROR;
                        ErrorMessage error = new ErrorMessage();
                        try {
                                UserDAO dao = new UserDAO();
                                boolean checkAvailableUser = dao.checkDublicate(userId);
                                if (checkAvailableUser) {
                                        TaskDAO tdao = new TaskDAO();
                                        boolean checkCreateTask = tdao.addNewTask(userId, task); // can only add task after login for valid userId
                                        if (checkCreateTask) {
                                                status = SUCCESS;
                                        }
                                } else {
                                        status = USER_NOT_FOUND;
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
                                        error.setMessage("Fail to add new task, please contact us for help!");
                                        msg = Response.status(601).entity(error).build(); // created custom return code for failed creation 
                                        break;
                                case 1:
                                        msg = Response.created(url).entity(task).build();
                                        break;
                                case 2:
                                        error.setMessage("User Not Found!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for USER_NOT_FOUND 
                                        break;
                        }

                        return msg;
                }

                @POST
                @Path("update") // Can disable this as : No need to use Path as direct submit POST method will automactic add new User (POST != GET)
                @Consumes(MediaType.APPLICATION_JSON)
                @Produces(MediaType.APPLICATION_JSON)
                public Response updateUser(User user) throws URISyntaxException {
                        int ERROR = 0;
                        int SUCCESS = 1;
                        int USER_NOT_FOUND = 2;
                        int status = ERROR;
                        ErrorMessage error = new ErrorMessage();
                        try {
                                UserDAO dao = new UserDAO();
                                boolean checkAvailableUser = dao.checkDublicate(user.getId());
                                if (checkAvailableUser) {
                                        boolean checkUpdateUser = dao.updateUser(user); // can only add task after login for valid userId
                                        if (checkUpdateUser) {
                                                status = SUCCESS;
                                        }
                                } else {
                                        status = USER_NOT_FOUND;
                                }

                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        Response msg = null;
                        switch (status) {
                                case 0:
                                        error.setMessage("Fail to update user profile, please contact us for help!");
                                        msg = Response.status(601).entity(error).build(); // created custom return code for failed update 
                                        break;
                                case 1:
                                        msg = Response.ok().build();
                                        break;
                                case 2:
                                        error.setMessage("User Not Found!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for USER_NOT_FOUND 
                                        break;
                        }

                        return msg;
                }

                @POST
                @Path("{userId}/updateTask") // Can disable this as : No need to use Path as direct submit POST method will automactic add new User (POST != GET)
                @Consumes(MediaType.APPLICATION_JSON)
                @Produces(MediaType.APPLICATION_JSON)
                public Response updateTask(@PathParam("userId") String userId, Task task) throws URISyntaxException {
                        int ERROR = 0;
                        int SUCCESS = 1;
                        int USER_NOT_FOUND = 2;
                        int TASK_NOT_FOUND = 3;
                        int status = ERROR;
                        ErrorMessage error = new ErrorMessage();
                        try {
                                UserDAO dao = new UserDAO();
                                boolean checkAvailableUser = dao.checkDublicate(userId);
                                if (checkAvailableUser) {
                                        TaskDAO tdao = new TaskDAO();
                                        boolean checkCorrectTask = tdao.checkAvailable(task.getId(), userId);
                                        if (checkCorrectTask) {
                                                boolean checkUpdateTask = tdao.updateTask(task); // can only add task after login for valid userId
                                                if (checkUpdateTask) {
                                                        status = SUCCESS;
                                                }
                                        } else {
                                                status = TASK_NOT_FOUND;
                                        }
                                } else {
                                        status = USER_NOT_FOUND;
                                }

                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        Response msg = null;
                        switch (status) {
                                case 0:
                                        error.setMessage("Fail to update task, please contact us for help!");
                                        msg = Response.status(601).entity(error).build(); // created custom return code for failed update User's task
                                        break;
                                case 1:
                                        msg = Response.ok().build();
                                        break;
                                case 2:
                                        error.setMessage("User Not Found!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for USER_NOT_FOUND 
                                        break;
                                case 3:
                                        error.setMessage("Task Not Found!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for TASK_NOT_FOUND 
                                        break;
                        }

                        return msg;
                }

                @DELETE
                @Path("/{userId}/delete") // add an extra "/" at the start cause some how DELETE method can't read it like GET or POST (multiple properties in url?)
                @Produces(MediaType.APPLICATION_JSON)
                public Response deleteUser(@PathParam("userId") String userId) throws URISyntaxException {
                        int ERROR = 0;
                        int SUCCESS = 1;
                        int USER_NOT_FOUND = 2;
                        int status = ERROR;
                        ErrorMessage error = new ErrorMessage();
                        try {
                                UserDAO dao = new UserDAO();
                                boolean checkAvailableUser = dao.checkDublicate(userId);
                                if (checkAvailableUser) {
                                        boolean checkDeleteUser = dao.deleteUser(userId); // can only add task after login for valid userId
                                        if (checkDeleteUser) {
                                                status = SUCCESS;
                                        }
                                } else {
                                        status = USER_NOT_FOUND;
                                }

                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        Response msg = null;
                        switch (status) {
                                case 0:
                                        error.setMessage("Fail to detele account, please contact us for help!");
                                        msg = Response.status(601).entity(error).build(); // created custom return code for failed delete User
                                        break;
                                case 1:
                                        msg = Response.ok().build();
                                        break;
                                case 2:
                                        error.setMessage("User Not Found!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for USER_NOT_FOUND 
                                        break;
                        }

                        return msg;
                }

                @DELETE
                @Path("/{userId}/{taskId}/deleteTask") // add an extra "/" at the start cause some how DELETE method can't read it like GET or POST (multiple properties in url?)
                @Produces(MediaType.APPLICATION_JSON)
                public Response deleteTask(@PathParam("userId") String userId, @PathParam("taskId") int taskId) throws URISyntaxException {
                        int ERROR = 0;
                        int SUCCESS = 1;
                        int USER_NOT_FOUND = 2;
                        int TASK_NOT_FOUND = 3;
                        int status = ERROR;
                        ErrorMessage error = new ErrorMessage();
                        try {
                                UserDAO dao = new UserDAO();
                                boolean checkAvailableUser = dao.checkDublicate(userId);
                                if (checkAvailableUser) {
                                        TaskDAO tdao = new TaskDAO();
                                        boolean checkCorrectTask = tdao.checkAvailable(taskId, userId);
                                        if (checkCorrectTask) {
                                                boolean checkUpdateTask = tdao.deleteTask(taskId); // can only add task after login for valid userId
                                                if (checkUpdateTask) {
                                                        status = SUCCESS;
                                                }
                                        } else {
                                                status = TASK_NOT_FOUND;
                                        }
                                } else {
                                        status = USER_NOT_FOUND;
                                }

                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        Response msg = null;
                        switch (status) {
                                case 0:
                                        error.setMessage("Fail to delete task, please contact us for help!");
                                        msg = Response.status(601).entity(error).build(); // created custom return code for failed update User's task
                                        break;
                                case 1:
                                        msg = Response.ok().build();
                                        break;
                                case 2:
                                        error.setMessage("User Not Found!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for USER_NOT_FOUND 
                                        break;
                                case 3:
                                        error.setMessage("Task Not Found!");
                                        msg = Response.status(602).entity(error).build(); // created custom return code for TASK_NOT_FOUND 
                                        break;
                        }

                        return msg;
                }

        }

}
