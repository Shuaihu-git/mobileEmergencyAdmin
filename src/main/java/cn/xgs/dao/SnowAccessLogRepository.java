package cn.xgs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnowAccessLogRepository extends JpaRepository<SnowAccessLog, Long> {

}
