package com.devtest.devtest.controller;

import com.devtest.devtest.service.SecurityService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class DevController {

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public String getAuthInfo(HttpServletRequest req) throws ExpiredJwtException {
        Boolean expireCheck = null;
        String authorization = req.getHeader("Authorization");
        System.out.println("authorization : " + authorization);

        if(authorization.equals("null")) {
            System.out.println("noLogin");
            return "noLogin";
        }else {
            try{
                String payload = securityService.getSubject(authorization);

                JSONObject json = new JSONObject(payload.replaceAll("=", ":"));

//        System.out.println(json);

                int exp = json.getInt("exp");
//        System.out.println(exp);

                expireCheck = securityService.jwtExpireCheck(exp);

                return expireCheck.toString();
            }catch (JwtException e){
                expireCheck = false;
                e.printStackTrace();
            }
        }

        return expireCheck.toString();


    }
}
