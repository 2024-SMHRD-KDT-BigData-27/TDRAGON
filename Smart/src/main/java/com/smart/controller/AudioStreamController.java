package com.smart.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stream")
public class AudioStreamController {

	@GetMapping(value = "/{filename}", produces = "audio/wav")
    public void streamAudio(@PathVariable String filename, HttpServletResponse response) {
        File audioFile = new File("C:/uploads/" + filename);

        if (!audioFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("audio/wav");
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");

        try (
            FileInputStream fis = new FileInputStream(audioFile);
            ServletOutputStream os = response.getOutputStream()
        ) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}