package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smart.entity.Memos;
import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memos, Long> { 
    List<Memos> findByMeetingIdx(Long meetingIdx); 
}
