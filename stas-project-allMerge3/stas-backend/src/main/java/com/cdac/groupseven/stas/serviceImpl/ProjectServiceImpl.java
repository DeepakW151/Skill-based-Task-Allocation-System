package com.cdac.groupseven.stas.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cdac.groupseven.stas.dto.MemberDto;
import com.cdac.groupseven.stas.dto.NewProject;
import com.cdac.groupseven.stas.dto.ProjectDto;
import com.cdac.groupseven.stas.entity.Project;
import com.cdac.groupseven.stas.entity.User;
import com.cdac.groupseven.stas.entity.User;
import com.cdac.groupseven.stas.enums.ProjectStatus;
import com.cdac.groupseven.stas.enums.TaskStatus;
import com.cdac.groupseven.stas.repository.ProjectRepository;
import com.cdac.groupseven.stas.repository.UserRepository;
import com.cdac.groupseven.stas.service.ProjectService;

import jdk.jshell.spi.ExecutionControl.RunException;

@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public void createProject(Project project) {
		projectRepository.save(project);
	}		
	
	@Override
	public ProjectDto clientUpdateProject(Long projectId, NewProject newData) {
		LocalDate oneMonthFromNow = LocalDate.now().plusMonths(1);
    	if (newData.getEndDate().isBefore(LocalDate.now().plusMonths(1)))
    		throw new RuntimeException("Project completion date should be more than " + oneMonthFromNow);    	
		
		Optional<Project> project = projectRepository.findById(projectId);
		
		if(project.isPresent()) {
					
			Project updatedProject = project.get();
			
			if (updatedProject.getStatus().equals(ProjectStatus.COMPLETED)) {
				throw new RuntimeException("Can not update completed project!");
			}
			
			if (updatedProject.getStatus().equals(ProjectStatus.PENDING) || updatedProject.getStatus().equals(ProjectStatus.ONHOLD)) {
				Optional<User> manager = userRepository.findById(newData.getManagerId());
				if (manager.isPresent()) updatedProject.setManager(manager.get());
			}
			
			updatedProject.setTitle(newData.getTitle());
			updatedProject.setDescription(newData.getDescription());
			updatedProject.setEndDate(newData.getEndDate());
			
			projectRepository.save(updatedProject);
			
			return new ProjectDto(projectRepository.findById(projectId).get());
		} else {
			throw new IllegalArgumentException("Project with id " + projectId + " does not exist");
		}
	}
	
	@Override
	public ProjectDto getProjectById(Long id) {
		Optional<Project> project = projectRepository.findById(id);
		if (project.isPresent()) return new ProjectDto(project.get());
		
		throw new RuntimeException("Project with ID:" + id + " is not present.");
	}
	
	@Override
    public Page<ProjectDto> findProjectsForClient(String email, int page, int limit) {
        // 1. Create a Pageable object from the page and limit parameters.
        // This object tells the repository which page to fetch and how many items.
        Pageable pageable = PageRequest.of(page, limit);

        // 2. Call the repository method. It returns a Page of Project ENTITIES.
        Page<Project> projectPage = projectRepository.findByClientEmail(email, pageable);

        // 3. Convert the Page<Project> to a Page<ProjectDto>.
        // The .map() function on the Page object is the perfect tool for this.
        // It applies a conversion function to each item in the page's content list.
        return projectPage.map(this::convertToDto);
    }

    // A private helper method to handle the conversion from Entity to DTO.
    // This keeps your code clean and reusable.
    private ProjectDto convertToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus().toString()); // Convert enum to string

        // Calculate open tasks and completion percentage
        int totalTasks = project.getTasks().size();
        long completedTasks = project.getTasks().stream()
                .filter(task -> TaskStatus.COMPLETED.equals(task.getStatus()))
                .count();
        
        dto.setOpenTasks(totalTasks - (int) completedTasks);
        dto.setCompletion(totalTasks > 0 ? (int) Math.round(((double) completedTasks / totalTasks) * 100) : 0);

        // Map member details (assuming a MemberDto exists)
        // This is where you would convert the List<ProjectMember> to a List<MemberDto>
        // For simplicity, we'll just show the count here.
        dto.setMembers(project.getMembers().stream()
                .map(member -> new MemberDto(member.getUser().getId(), member.getUser().getName()))
                .collect(Collectors.toSet()));

        return dto;
    }
    
    @Override
    public ProjectDto createNewProject(String email, NewProject newProject) {
    	LocalDate oneMonthFromNow = LocalDate.now().plusMonths(1);
    	if (newProject.getEndDate().isBefore(LocalDate.now().plusMonths(1)))
    		throw new RuntimeException("Project completion date should be more than " + oneMonthFromNow);
    	
    	User manager = userRepository.findById(newProject.getManagerId())
			.orElseThrow(() -> new RuntimeException("Manager with ID: " + newProject.getManagerId() + " does not exist."));
    	
    	Project eProject = new Project();
    	eProject.setTitle(newProject.getTitle());
    	eProject.setDescription(newProject.getDescription());
    	eProject.setEndDate(newProject.getEndDate());
    	eProject.setStartDate(LocalDate.now());
    	eProject.setStatus(ProjectStatus.PENDING);
    	
    	eProject.setManager(manager);
    	
//    	eProject.setManager(userRepository.findById(newProject.setManagerId(null)));
//    	System.out.println(newProject.getClientId());
    	eProject.setClient(userRepository.findByEmail(email) .get());
    	
    	projectRepository.save(eProject);
    	
    	return new ProjectDto(eProject);
    }
}
