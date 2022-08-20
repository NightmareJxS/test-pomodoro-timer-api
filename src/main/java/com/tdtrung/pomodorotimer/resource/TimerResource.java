/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.resource;

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
                        // tmp list (not connected to DB)
                        List<User> list = new ArrayList();
                        list.add(new User("SE160947", "testing@test.com", "123456", 13575, 794850));
                        list.add(new User("PTU55748", "testing1@test.com", "654321", 10596, 305863));
                        list.add(new User("SE160111", "testing2@test.com", "666666", 5947, 50437));
                        list.add(new User("PTU00007", "testing3@test.com", "555555", 0, 0));
                        list.add(new User("PTU35235", "testing4@test.com", "444444", 5464, 705833));
                        list.add(new User("PTU33513", "testing5@test.com", "333333", 4432, 475623));
                        list.add(new User("PTU67545", "testing6@test.com", "222222", 2768, 428783));
                        list.add(new User("PTU32452", "testing7@test.com", "111111", 8675, 67857));
                        list.add(new User("PTU87980", "testing8@test.com", "000000", 15068, 990472));
                        return list;
                }

                @GET
                @Path("one")
                @Produces(MediaType.APPLICATION_JSON)
                public User getUser() {
                        return new User("PTU12345", "testingGetOneUser@test.com", "000000", 48830, 876734); // (1st method)
                }

                @GET
                @Path("{id}")
                @Produces(MediaType.APPLICATION_JSON)
                public Response getBy(@PathParam("id") String id) {
                        // Can replace with search function in dao
                        User user = new User(id, "testingReturnASpecificUser@test.com", "000000", 45698, 123973);

                        Response msg = Response.ok().entity(user).build();
                        return msg; // Create response msg (2nd method)
                }

                @POST
//        @Path("create") // No need to use Path as direct submit POST method will automactic add new User (POST != GET)
                @Consumes(MediaType.APPLICATION_JSON)
                public Response add(User user) throws URISyntaxException {
                        // Can replace with create method in dao
                        // tmp sout for understading
                        System.out.println("Created a new user: " + user);

                        // Return new api url to new user
                        // Use Postman to check
                        // Ref: https://youtu.be/nKXAKEQ2Y58?t=5218 
                        URI url = new URI(ui.getBaseUri() + "users/" + user.getId());

                        Response msg = Response.created(url).build();

                        return msg;
                }
        }

        @Path("/tasks")
        public static class Tasks {

                @Context
                UriInfo ui; // To store url from Tomcat (when create new user)

                @GET
                @Produces(MediaType.APPLICATION_JSON)
                public List<Task> getAll() {
                        // tmp list (not connected to DB)
                        List<Task> list = new ArrayList();
                        list.add(new Task("17", "SE160947", "OJT Signup", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 0));
                        list.add(new Task("18", "SE160947", "Something1", 600, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 0));
                        list.add(new Task("19", "SE160947", "Something2", 120, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 0));
                        list.add(new Task("12", "SE160947", "Something3", 172800, Date.valueOf("2022-08-16"), Date.valueOf("2022-08-18"), 0));
                        list.add(new Task("1", "SE160947", "Something4", 1209600, Date.valueOf("2022-08-18"), Date.valueOf("2022-09-01"), 1));
                        list.add(new Task("5", "PTU02847", "Something5", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 1));
                        list.add(new Task("55", "PTU43252", "Something6", 120, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 3));
                        list.add(new Task("48", "PTU65438", "Something7", 120, Date.valueOf("2022-08-15"), Date.valueOf("2022-08-15"), 2));
//                        Date date = Date.valueOf("2022-08-15");
//                        System.out.println(date);
                        // Uses https://www.epochconverter.com/ if can't understand number
                        return list;
                }

                @GET
                @Path("one")
                @Produces(MediaType.APPLICATION_JSON)
                public Task getTask() {
                        return new Task("17", "SE160947", "OJT Signup", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 0); // (1st method)
                }

                @GET
                @Path("{userId}/{taskId}")
                @Produces(MediaType.APPLICATION_JSON)
                public Response getBy(@PathParam("userId") String userId, @PathParam("taskId") String taskId) {
                        // Can replace with search function in dao
                        Task task = new Task(taskId, userId, "OJT Signup", 7200, Date.valueOf("2022-08-13"), Date.valueOf("2022-08-14"), 0);

                        Response msg = Response.ok().entity(task).build();
                        return msg; // Create response msg (2nd method)
                }

                @POST
//        @Path("create") // No need to use Path as direct submit POST method will automactic add new User (POST != GET)
                @Consumes(MediaType.APPLICATION_JSON)
                public Response add(Task task) throws URISyntaxException {
                        // Can replace with create method in dao
                        // tmp sout for understading
                        System.out.println("Created a new user: " + task);

                        // Return new api url to new task
                        // Use Postman to check
                        // Ref: https://youtu.be/nKXAKEQ2Y58?t=5218 
                        URI url = new URI(ui.getBaseUri() + "tasks/" + task.getUserId()+ "/" + task.getId());

                        Response msg = Response.created(url).build();

                        return msg;
                }
        }
        
}
