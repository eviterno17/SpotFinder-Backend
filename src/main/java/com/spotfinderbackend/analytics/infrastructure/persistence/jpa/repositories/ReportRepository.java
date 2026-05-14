package com.spotfinderbackend.analytics.infrastructure.persistence.jpa.repositories;

import com.spotfinderbackend.analytics.domain.model.aggregates.Report;
import com.spotfinderbackend.analytics.domain.model.valueobjects.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByFacilityIdOrderByGeneratedAtDesc(Long facilityId);
    List<Report> findByReportTypeAndFacilityId(ReportType type, Long facilityId);
}
