package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.entity.meetings;
import com.smart.entity.records;
import com.smart.entity.users;
import com.smart.service.MeetingsService;
import com.smart.service.RecordsService;
import com.smart.service.SmartService;

@Controller
public class MemberController {

    @Autowired
    private SmartService smart;

    @Autowired
    private MeetingsService meetingsservice;

    @Autowired
    private RecordsService recordsService;

    // ✅ 파일 업로드 처리
    @PostMapping("/uploadfile")
    public String handleFileUpload(
        @RequestParam("meeting_title") String meetingTitle,
        @RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes, HttpSession session, Model model
    ) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "❌ 파일이 없습니다.");
            return "redirect:/upload";
        }
        

        try {
            // ✅ 파일 저장 경로 설정 (Windows: C:/uploads/, Linux: /home/uploads/)
            String uploadDir = "C:/uploads/";
            File saveFile = new File(uploadDir + file.getOriginalFilename());

            // ✅ 파일 저장
            file.transferTo(saveFile);

            System.out.println("📂 저장된 파일: " + saveFile.getAbsolutePath());
            System.out.println("📌 회의 제목: " + meetingTitle);

            // ✅ 회의 정보 저장 (DB)
            users user = (users) session.getAttribute("users");
            meetingsservice.insertMeetings(meetingTitle, user.getUser_email());
            
            // ✅ 회의 ID 가져오기
            int meetingIdx = meetingsservice.getLastMeetingIdx();
            int userIdx = user.getUser_idx();
            String fileName = file.getOriginalFilename();

            // ✅ FastAPI 비동기 호출
            new Thread(() -> {
                try {
                    String fastapiUrl = "https://6ed5-34-66-230-197.ngrok-free.app/process_audio/";

                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("meeting_idx", meetingIdx);
                    requestMap.put("user_idx", userIdx);
                    requestMap.put("file_name", fileName);  // 👈 파일명만 전송

                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

                    ResponseEntity<String> response = restTemplate.postForEntity(fastapiUrl, entity, String.class);
                    System.out.println("📡 FastAPI 응답 코드: " + response.getStatusCode());
                    System.out.println("📡 FastAPI 응답 본문: " + response.getBody());
                } catch (Exception e) {
                    System.err.println("❌ FastAPI 요청 실패: " + e.getMessage());
                }
            }).start();

            redirectAttributes.addFlashAttribute("message", "✅ 업로드 성공!");
            model.addAttribute("user", user);
            return "redirect:/allnotes";

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "❌ 업로드 실패!");
            return "redirect:/upload";
        }
    }

    // ✅ 전체 노트 조회
    @GetMapping("/allnotes")
    public String getAllMeetings(Model model, HttpSession session) {
        users user = (users) session.getAttribute("users");

        List<meetings> meetings = meetingsservice.getActiveMeetingsByEmail(user.getUser_email());

        model.addAttribute("meetings", meetings);
        return "allnotes";
    }

    @GetMapping("/main")
    public String main(Model model, HttpSession session) {
        users user = (users) session.getAttribute("users");

        List<meetings> meetings = meetingsservice.getMeetingsByUserEmail(user.getUser_email());

        model.addAttribute("meetings", meetings);
        model.addAttribute("user", user);

        return "main";
    }

    // ✅ 회의록 상세 페이지
    @GetMapping("/notepage")
    public String notepage(@RequestParam("idx") Long meetingIdx, Model model) {
        meetings meeting = meetingsservice.getNoteByMeetingIdx(meetingIdx);
        records records = recordsService.getRecordsByMeetingIdx(meetingIdx);

        model.addAttribute("meeting", meeting);
        model.addAttribute("records", records);

        return "notepage";
    }

    // ✅ 로그인 처리
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, users>> login(@RequestParam("user_email") String userEmail,
        @RequestParam("user_pw") String userPw, HttpSession session) {
        users temp = new users();
        temp.setUser_email(userEmail);
        temp.setUser_pw(userPw);

        users user = smart.login(temp);
        Map<String, users> response = new HashMap<>();
        response.put("users", user);

        if (user != null) {
            session.setAttribute("users", user);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/folders")
    public String folders() {
        return "folders";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
    
    @GetMapping("/meetings")
	@ResponseBody
	public ResponseEntity<?> getMeetings(HttpSession session) {
	    users user = (users) session.getAttribute("users");

	    // ✅ 로그인 여부 확인
	    if (user == null) {
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Unauthorized - Please login.");
	        return ResponseEntity.status(401).body(errorResponse); // JSON으로 반환
	    }

	    List<meetings> meetingList = meetingsservice.getMeetingsByUserEmail(user.getUser_email());
	    List<Map<String, String>> meetings = new ArrayList<>();

	    for (meetings meeting : meetingList) {
	        Map<String, String> meetingData = new HashMap<>();
	        meetingData.put("meeting_idx", String.valueOf(meeting.getMeeting_idx())); //  숫자를 문자열로 변환
	        meetingData.put("meeting_title", meeting.getMeeting_title());
	        meetingData.put("meeting_date", meeting.getCreated_at().toLocalDate().toString());
	        meetings.add(meetingData);
	    }

	    return ResponseEntity.ok(meetings); // JSON 형태로 데이터 반환
	}

    @GetMapping("/recoding")
    public String recoding() {
        return "recoding";
    }

    @GetMapping("/trash")
    public String trash(Model model, HttpSession session) {
    	 users user = (users) session.getAttribute("users");

         List<meetings> meetings = meetingsservice.getDeletedMeetings(user.getUser_email());
         
         System.out.println(user.getUser_email());

         model.addAttribute("meetings", meetings);
         
         return "trash";

    }
    
    @PostMapping("/trash")
    @ResponseBody
    public Map<String, Boolean> updateTrashStatus(@RequestBody Map<String, Object> payload) {
        List<Integer> noteIds = (List<Integer>) payload.get("noteIds");
        String action = (String) payload.get("action"); // "delete", "restore", "moveToTrash"

        List<Long> meetingIds = noteIds.stream().map(Long::valueOf).toList();

        switch (action) {
            case "moveToTrash":
                meetingsservice.moveToTrash(meetingIds); // isActive = 0
                break;
            case "restore":
                meetingsservice.restoreFromTrash(meetingIds); // isActive = 1
                break;
            case "delete":
            	meetingsservice.deletePermanently(meetingIds); // 진짜 삭제
                break;
            default:
                return Map.of("success", false); // 잘못된 요청
        }

        return Map.of("success", true);
    }

    
    

//    // ✅ 회의록 삭제 처리
//    @PostMapping("/trash")
//    @ResponseBody
//    public Map<String, Boolean> deleteNotes(@RequestBody Map<String, Object> payload) {
//        List<Integer> noteIds = (List<Integer>) payload.get("noteIds");
//        System.out.println(noteIds.toString());
//
//        List<Long> meetingId = noteIds.stream().map(Long::valueOf).toList();
//
//        meetingsservice.deleteMeetings(meetingId);
//
//        return Map.of("success", true);
//    }

    @GetMapping("/update")
    public String update() {
        return "update";
    }

    // ✅ 사용자 정보 조회
    @GetMapping("/user-info")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpSession session) {
        users user = (users) session.getAttribute("users");

        if (user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unauthorized - Please login.");
            return ResponseEntity.status(401).body(errorResponse);
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user_idx", user.getUser_idx());
        userInfo.put("user_email", user.getUser_email());
        userInfo.put("user_name", user.getUser_name());

        return ResponseEntity.ok(userInfo);
    }

    // ✅ 비밀번호 변경
    @PostMapping("/update-user")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> updateUser(
        @RequestBody Map<String, String> payload, HttpSession session) {

        users user = (users) session.getAttribute("users");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        if (!user.getUser_pw().equals(currentPassword)) {
            return ResponseEntity.ok(Map.of("success", false));
        }

        user.setUser_pw(newPassword);
        smart.updateUser(user);

        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    // ✅ 회의록 추가
    @PostMapping("/upload")
    public String insertUpload(@RequestParam("host_email") String host_email, @RequestParam("meeting_title") String meeting_title) {
        int ct = meetingsservice.insertMeetings(meeting_title, host_email);

        return "upload";
    }

    @PostMapping("/join")
    public String join(users st, RedirectAttributes redirectAttributes) {
        if (!smart.idCheck(st.getUser_email())) {
            redirectAttributes.addFlashAttribute("email_check", 1);
            return "redirect:/login";
        } else {
            smart.join(st);
            return "redirect:/login";
        }
    }

    @PostMapping(value = "/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, String>> checkEmail(@RequestParam("user_email") String userEmail) {
        Map<String, String> response = new HashMap<>();

        if (!smart.idCheck(userEmail)) {
            response.put("status", "duplicate");
        } else {
            response.put("status", "available");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/join")
    public String joinsu() {
        return "join";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
