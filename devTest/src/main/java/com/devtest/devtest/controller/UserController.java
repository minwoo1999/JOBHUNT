package com.devtest.devtest.controller;

import com.devtest.devtest.model.HUNTUSER_BOOKMARK;
import com.devtest.devtest.model.User;
import com.devtest.devtest.model.User_HUNTUSER_BOOKMARK;
import com.devtest.devtest.service.LoginService;
import com.devtest.devtest.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired

    private LoginService loginService;

    @Autowired
    private SecurityService securityService;


    //로긴
    @RequestMapping(value = "/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public String login2(@RequestBody final User params) {


        User loginUser = loginService.getUser(params.getEmail());
        Map<String, Object> map = new HashMap<>();
        int time = (int) ((new Date().getTime() + 60 * 60 * 1000) / 1000);
        map.put("Email", params.getEmail());
        map.put("exp", time);

        String token = securityService.createToken(map.toString(), 1000 * 60 * 60);

        if (loginUser == null) {
            return null;
        }
        //로그인 성공시
        else if (params.getPass().equals(loginUser.getPass())) {
            System.out.println(token);
            return token;
        } else {
            return null;
        }
    }


    //아이디 중복찾기
    @RequestMapping(value = "/idCheck", method = RequestMethod.POST)//ID중복체크
    @CrossOrigin(origins = "http://localhost:3000")
    public int idCheck(@RequestBody final User params) {

        String userId = params.getEmail();

        User joinUser = this.loginService.getUser(userId);

//        System.out.println(loginUser.getNickname());

        if (joinUser != null) { //읽어온 유저 정보가 있으면 패일

            return 0;
        } else {

            return 1;
        }
    }


    //아이디 중복체크
    @RequestMapping(value = "/nickname", method = RequestMethod.POST)//ID중복체크
    @CrossOrigin(origins = "http://localhost:3000")
    public int nickCheck(@RequestBody final User params) {

        String userNickname = params.getNickname();

//        System.out.println(userNickname);

        User joinUser = this.loginService.getNickname(userNickname);

        if (joinUser != null) { //읽어온 유저 정보가 있으면 패일

            return 0;
        } else {

            return 1;
        }
    }

    //패스워드 찾기
    @RequestMapping(value = "/passCheck", method = RequestMethod.POST)//PW중복체크
    @CrossOrigin(origins = "http://localhost:3000")
    public int passCheck(@RequestBody final User params) {

        if (!params.getPass().equals(params.getPass2())) { //읽어온 유저 정보가 있으면 패일

            return 0;
        } else {

            return 1;
        }
    }

    //회원가입
    @RequestMapping(value = "/join", method = RequestMethod.POST)//회원가입
    @CrossOrigin(origins = "http://localhost:3000")
    public int join(@RequestBody final User params) {


        if (this.loginService.insertUser(params) != 0) {
            return 1;
        } else {
            return 2;
        }
    }

    // 마이페이지 정보출력
    @RequestMapping(value = "/mypage", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public List getAuthInfo(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");
        String payload = securityService.getSubject(authorization);

        JSONObject json = new JSONObject(payload.replaceAll("=", ":"));

        String email = json.getString("Email");

        User user = loginService.getUser(email);

        List<User> userList = loginService.getUserList(email);

        List<HUNTUSER_BOOKMARK> huntuser_bookmark = loginService.get_HUNTUSER_BOOKMARK(user.getUno());

        List mine = new ArrayList<>();

        mine.addAll(userList);
        mine.addAll(huntuser_bookmark);

        System.out.println(mine);

//        User_HUNTUSER_BOOKMARK user_huntuser_bookmark = new User_HUNTUSER_BOOKMARK(); //빈 dto

        return mine;
    }


    // 즐겨찾기에 회사정보저장
    @RequestMapping(value = "/company-save", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:3000")
    public int company_save(@RequestBody final HUNTUSER_BOOKMARK params, HttpServletRequest req) {


        // 토큰값으로 해당하는 유저의 정보를 가져오는 코드
        String authorization = req.getHeader("Authorization");
        System.out.println(authorization);
        String payload = securityService.getSubject(authorization);

        JSONObject json = new JSONObject(payload.replaceAll("=", ":"));

        String email = json.getString("Email");

        User user = loginService.getUser(email);

        params.setUno(user.getUno());

        System.out.println(params.getCompanyname());
        System.out.println(params.getCompany_start());
        System.out.println(params.getCompany_end());
        System.out.println(params.getCompanyimg());
        System.out.println(params.getUno());



        if (this.loginService.get_HUNTUSER_BOOKMARK_Check(params.getCompanyname(),user.getUno())==null) {
            if(this.loginService.Insert_User_BookMark(params) != 0){
                return 1; // 즐겨찾기에 성공
            }else{
                return 3; // 로그인 후 이용해주세요
            }

        }else {
            return 2;  //이미 즐겨찾기가 되어있습니다.
        }

    }

    // 즐겨찾기에 회사정보삭제
    @RequestMapping(value = "/company-delete", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:3000")
    public int company_delete(@RequestBody final HUNTUSER_BOOKMARK params, HttpServletRequest req) {

        // 토큰값으로 해당하는 유저의 정보를 가져오는 코드
        String authorization = req.getHeader("Authorization");
        System.out.println(authorization);
        String payload = securityService.getSubject(authorization);

        JSONObject json = new JSONObject(payload.replaceAll("=", ":"));

        String email = json.getString("Email");

        User user = loginService.getUser(email);



        if (this.loginService.delete_User_BookMark(params.getCompanyname(),user.getUno()) != 0) {
            return 1;
        } else {
            return 2;
        }
    }



}