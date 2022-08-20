/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Jason 2.0
 */
@ApplicationPath("/api")
public class TimerApplication extends ResourceConfig{
        public TimerApplication(){
                packages("com.tdtrung.pomodorotimer.resource");
        }
}
