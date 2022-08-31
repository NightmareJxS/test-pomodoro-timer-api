/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 *
 * @author Jason 2.0
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(final ContainerRequestContext requestContext,
                final ContainerResponseContext cres) throws IOException {
                cres.getHeaders().add("Access-Control-Allow-Origin", "*"); // can change to specific site as allow all site is not secure (vd: http://localhost:3000 )
                cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
                cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
                cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                cres.getHeaders().add("Access-Control-Max-Age", "1209600");
        }
}


// temporary remove as it's useless but will keep it here for later study

// this ref: https://stackoverflow.com/questions/23450494/how-to-enable-cross-domain-requests-on-jax-rs-web-services
        // in servlet: https://stackoverflow.com/questions/54239666/enabling-cors-in-java-web-service 
// there's other ref for enable server: https://openliberty.io/guides/cors.html#enabling-a-simple-cors-configuration (don't know is do-able)

        
