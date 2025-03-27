package com.smart.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.entity.Chunks;
import com.smart.entity.users;
import com.smart.service.ChunksService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FaissController {

    private final ChunksService chunksService;
    private final ObjectMapper objectMapper;

    @PostMapping("/search")
    public String search(@RequestParam("query") String query,
                         HttpSession session,
                         Model model) throws IOException {
    	
    	System.out.println("검색요청");
    	users user = (users) session.getAttribute("users");
    	if (user == null) return "redirect:/index";
    	Long userIdx = Long.valueOf(user.getUser_idx());
        System.out.println(userIdx);
        if (userIdx == null) return "redirect:/index"; // 로그인 안 된 경우

        // ✅ FastAPI 요청
        String fastApiUrl = "https://gb-suggesting-compare-commercial.trycloudflare.com/faiss_search/";
        URL url = new URL(fastApiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", query);
        requestMap.put("top_k", 70);
        String jsonRequest = objectMapper.writeValueAsString(requestMap);
        conn.getOutputStream().write(jsonRequest.getBytes());

        // ✅ 결과 파싱
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder responseStr = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            responseStr.append(line);
        }

        // 로그 찍기
        System.out.println("📡 FastAPI 응답 원문:\n" + responseStr.toString());

        // ✅ 안전하게 파싱
        Map<String, List<Integer>> responseMap = objectMapper.readValue(
            responseStr.toString(),
            new com.fasterxml.jackson.core.type.TypeReference<Map<String, List<Integer>>>() {}
        );
        List<Long> chunkIds = responseMap.get("chunk_ids").stream()
                .map(Long::valueOf)
                .filter(id -> id != -1)
                .collect(Collectors.toList());

        // ✅ 상위 5개만 필터링
        List<Long> top5Ids = chunkIds.stream()
            .limit(50)
            .collect(Collectors.toList());

        List<Chunks> chunks = chunksService.getChunksByIdsAndUser(top5Ids, userIdx);
        model.addAttribute("searchResults", chunks);

        return "fragments/searchResults :: results";
    }
}