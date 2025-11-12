package com.projeto.apigateway.controller;


import com.projeto.apigateway.controller.dto.ComplaintRequest;
import com.projeto.apigateway.controller.dto.ComplaintResponse;
import com.projeto.apigateway.service.ComplaintService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/complaint")
@Slf4j
@AllArgsConstructor
public class ComplaintController {

    private ComplaintService complaintService;
    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(@RequestBody ComplaintRequest complaintRequest){


        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(complaintService.createComplaint(complaintRequest));
       
    }
}
