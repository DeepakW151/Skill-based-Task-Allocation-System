package com.cdac.groupseven.stas.service;

import org.springframework.data.domain.Page;

import com.cdac.groupseven.stas.dto.NewProject;
import com.cdac.groupseven.stas.dto.ProjectDto;
import com.cdac.groupseven.stas.entity.Project;

public interface ProjectService {
	
	void createProject(Project project);
	
	ProjectDto getProjectById(Long id);
//	
//	void updateProject(long id,Project project) ;

	Page<ProjectDto> findProjectsForClient(String email, int page, int limit);
	ProjectDto createNewProject(String email, NewProject newProject);
	ProjectDto clientUpdateProject(Long projectId, NewProject newProject);


}
