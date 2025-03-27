package com.smart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.smart.repository.RecordingRepository;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class AudioService {

    @Value("${upload.path}")  // 파일 저장 경로 (application.properties에서 설정)
    private String uploadDir;

    private final RecordingRepository recordingRepository;

    @Autowired
    public AudioService(RecordingRepository recordingRepository) {
        this.recordingRepository = recordingRepository;
    }

    // 오디오 파일 저장
    public String saveAudioFile(MultipartFile file, String userFilename) {
        try {
            // 확장자 추출 (예: .wav, .mp3, .webm 등)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // ✅ timestamp 추가 (예: 20250320_123456)
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());

            // ✅ 사용자가 입력한 파일명 + timestamp 형식으로 저장
            String safeFilename = (userFilename != null && !userFilename.trim().isEmpty()) ? userFilename.trim() : "audio";
            String uniqueFilename = safeFilename + "_" + timestamp + ".wav";

            // 저장할 파일 경로 설정
            File targetFile = new File(uploadDir, uniqueFilename);
            if (!new File(uploadDir).exists()) {
                new File(uploadDir).mkdirs();
            }

            // 파일 저장
            file.transferTo(targetFile);

            return uniqueFilename; // 저장된 파일명 반환
        } catch (IOException e) {
            return "파일 저장 실패: " + e.getMessage();
        }
    }
}