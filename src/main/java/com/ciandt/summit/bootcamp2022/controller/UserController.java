package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.dto.Data;
import com.ciandt.summit.bootcamp2022.dto.UserDTO;
import com.ciandt.summit.bootcamp2022.dto.UsernameDto;
import com.ciandt.summit.bootcamp2022.services.UserService;
import com.ciandt.summit.bootcamp2022.utils.TokenService;
import com.ciandt.summit.bootcamp2022.utils.exceptions.InvalidLogDataException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")//mudar endpoint
public class UserController {

    @Autowired
    TokenService tokenFeignClient;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "This request generates the authentication token")
    @PostMapping
    public String getToken(@RequestBody UsernameDto username) throws InvalidLogDataException {
        return tokenFeignClient.createToken(username);
    }


    @ApiOperation(value = "This request get User by Id")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId
            , @RequestHeader(value = "name") String nome
            , @RequestHeader(value = "token") String token) {

        UsernameDto usernameDto = new UsernameDto(new Data(nome, token));
        UserDTO dto = userService.getUserById(userId, usernameDto);
        return ResponseEntity.ok().body(dto);
    }
}
