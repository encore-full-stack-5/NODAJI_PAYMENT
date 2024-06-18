package com.nodaji.payment.controller;

import com.nodaji.payment.dto.request.WinRequestDto;
import com.nodaji.payment.service.WinResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/win")
public class WinResultController {

    private final WinResultService winResultService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void winResult(@PathVariable(name = "userId") String userId, @RequestBody WinRequestDto req){
        winResultService.winResultProcess(userId, req);
    }
}
