package com.ucampus.repository;

import com.ucampus.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    List<Share> findAllByUserId(Long userId);

}
