package com.smart.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.smart.service.AudioService;

@RestController
@RequestMapping("/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file, 
                                              @RequestParam("filename") String userFilename) {
        System.out.println("🚀 컨트롤러 도착! 📩");
        System.out.println("파일명: " + userFilename);
        System.out.println("파일 크기: " + file.getSize() + " bytes");

        String savedFilename = audioService.saveAudioFile(file, userFilename);
        return ResponseEntity.ok("업로드 완료: " + savedFilename);
    }
}




