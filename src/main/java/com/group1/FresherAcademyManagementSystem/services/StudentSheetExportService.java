package com.group1.FresherAcademyManagementSystem.services;

import com.group1.FresherAcademyManagementSystem.dtos.StudentSheetDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface StudentSheetExportService {

    @Deprecated
    Map<String, Object> exportXLSSheet(String[] ids) throws IOException;

    @Deprecated
    Map<String, Object> exportCSV(String[] ids) throws IOException;

    List<StudentSheetDTO> exportDataSheet(String[] ids);
}
