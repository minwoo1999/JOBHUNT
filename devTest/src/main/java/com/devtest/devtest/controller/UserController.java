package com.devtest.devtest.controller;

import com.devtest.devtest.model.HUNTUSER_BOOKMARK;
import com.devtest.devtest.model.User;
import com.devtest.devtest.model.User_HUNTUSER_BOOKMARK;
import com.devtest.devtest.model.User_RefreshToken;
import com.devtest.devtest.repository.RedisRepository;
import com.devtest.devtest.service.LoginService;
import com.devtest.devtest.service.SecurityService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Api(tags = {"ToyProject API Test"})  // Swagger 최상단 Controller 명칭
public class UserController {

    @Autowired

    private LoginService loginService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    RedisRepository redisRepository;




    //로긴
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "로그인", notes = "JWT로그인 API User의 데이터를 넘겨줘야함")
    @CrossOrigin(origins = "http://localhost:3000")
    public String login2(@RequestBody final User params) {


        User loginUser = loginService.getUser(params.getEmail());
        Map<String, Object> token_map = new HashMap<>();
        Map<String, Object> refresh_map = new HashMap<>();
        int time = (int) ((new Date().getTime() + 60 * 60 * 1000) / 1000); //시분초가 아닌 ms 값으로 변환시키는 과정

        token_map.put("Email", params.getEmail());
        token_map.put("exp", time);
        refresh_map.put("exp", time);


        String token = securityService.createToken(token_map.toString(), 1000 * 60); //access token값 유효시간
        String refresh_token = securityService.createToken(refresh_map.toString(), 1000 * 60 * 60 * 24); // 1000 * 60 * 60*24 refresh토큰 유효시간

        if (loginUser == null) {
            return null;
        }
        //로그인 성공시
        else if (params.getPass().equals(loginUser.getPass())) {
            System.out.println("token:" + token);
            System.out.println("refresh token:" + refresh_token);

            // redis를 이용하여 캐시에 이메일에 해당하는 refresh 토큰값을 넣어줌
            User_RefreshToken user = new User_RefreshToken();
            user.setEmail(params.getEmail().toString());
            user.setRefresh_token(refresh_token.toString());
            redisRepository.save(user).toString();

            System.out.println(redisRepository.findAll().toString());
            return token + "/" + refresh_token;
        } else {
            return null;
        }
    }


    //아이디 중복찾기
    @ApiOperation(value = "아이디 중복찾기", notes = "회원가입시 아이디 중복체크")
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
    @ApiOperation(value = "닉네임 중복체크", notes = "닉넴 중복체크")
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

    @ApiOperation(value = "비밀번호 찾기", notes = "비밀번호 찾기라는데 ?")
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
    @ApiOperation(value = "회원가입", notes = "사용자 모든 데이터를 잘 넘겨줘야함")
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
    @ApiOperation(value = "마이페이지", notes = "마이페이지 정보출력해줌 헤더에 token넣어야함")
    @RequestMapping(value = "/mypage", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public List getAuthInfo(HttpServletRequest req) throws ExpiredJwtException {

        String expireCheck = null;
        try {
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

            return mine;
        }catch (JwtException e){
            System.out.println("마이페이지 토큰 만료");
            expireCheck = "false";

            e.printStackTrace();
        }

//      User_HUNTUSER_BOOKMARK user_huntuser_bookmark = new User_HUNTUSER_BOOKMARK(); //빈 dto
        return Collections.singletonList(expireCheck);


    }


    // 즐겨찾기에 회사정보저장
    @ApiOperation(value = "회사 정보 즐겨찾기", notes = "즐겨찾기에 회사정보 저장")
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


        if (this.loginService.get_HUNTUSER_BOOKMARK_Check(params.getCompanyname(), user.getUno()) == null) {
            if (this.loginService.Insert_User_BookMark(params) != 0) {
                return 1; // 즐겨찾기에 성공
            } else {
                return 3; // 로그인 후 이용해주세요
            }

        } else {
            return 2;  //이미 즐겨찾기가 되어있습니다.
        }

    }

    // 즐겨찾기에 회사정보삭제
    @ApiOperation(value = "즐겨찾기 삭제", notes = "즐겨찾기 삭제 토큰값 넘겨줘야함 헤더에서")
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


        if (this.loginService.delete_User_BookMark(params.getCompanyname(), user.getUno()) != 0) {
            return 1;
        } else {
            return 2;
        }
    }


}