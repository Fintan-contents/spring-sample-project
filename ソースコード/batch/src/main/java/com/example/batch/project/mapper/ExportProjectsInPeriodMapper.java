package com.example.batch.project.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.common.generated.model.Project;

/**
 * 期間内プロジェクト一括出力バッチのMapper。
 */
@Mapper
public interface ExportProjectsInPeriodMapper {

    /**
     * 出力対象のプロジェクトを検索する。
     * @param businessDate 業務日付
     * @return 出力対象のプロジェクトの一覧
     */
    List<Project> selectProjectsInPeriod(LocalDate businessDate);
}
