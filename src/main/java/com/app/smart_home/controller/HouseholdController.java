package com.app.smart_home.controller;

import com.app.smart_home.domain.dto.HouseholdLogin;
import com.app.smart_home.service.HouseholdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/household")
public class HouseholdController {

    @Autowired
    HouseholdService householdService;


    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid HouseholdLogin loginRequest) throws Exception {
        return ResponseEntity.ok().body(householdService.login(loginRequest));
    }
}
