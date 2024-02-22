package com.example.accessrecord;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final MessageService messageService;

    @GetMapping("/history")
    public ResponseEntity<List<LogMessage>> getHistory(@RequestParam("tenant-id")Long id){
        return ResponseEntity.ok(this.messageService.getHistory(id));
    }
}
