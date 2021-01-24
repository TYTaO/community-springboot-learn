package com.tytao.community.controller;

import com.tytao.community.dto.AccessTokenDTO;
import com.tytao.community.dto.GithubUser;
import com.tytao.community.mapper.UserMapper;
import com.tytao.community.model.User;
import com.tytao.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value(value = "${github.client.id}")
    private String Client_id;
    @Value(value = "${github.client.secret}")
    private String Client_secret;
    @Value(value = "${github.redirect.uri}")
    private String Redirect_uri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setClient_secret(Client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(Client_id);
        System.out.println(Client_secret);
        System.out.println(Redirect_uri);
        System.out.println(accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if (githubUser != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            // 登录成功，写cookie， session
            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        } else {
            // 登录失败
            return "redirect:/";
        }
    }
}
