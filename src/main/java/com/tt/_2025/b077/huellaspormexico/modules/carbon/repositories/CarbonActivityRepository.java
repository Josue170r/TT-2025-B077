package com.tt._2025.b077.huellaspormexico.modules.carbon.repositories;

import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CarbonActivityRepository extends JpaRepository<CarbonActivity, Long>, JpaSpecificationExecutor<CarbonActivity> {
}
