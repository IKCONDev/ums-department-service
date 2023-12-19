package com.ikn.ums.department.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ikn.ums.department.VO.EmployeeVO;
import com.ikn.ums.department.entity.Department;
import com.ikn.ums.department.exception.DepartmentNameExistsException;
import com.ikn.ums.department.exception.EmptyInputException;
import com.ikn.ums.department.exception.EmptyListException;
import com.ikn.ums.department.exception.EntityNotFoundException;
import com.ikn.ums.department.exception.ErrorCodeMessages;
import com.ikn.ums.department.repository.DepartmentRepository;
import com.ikn.ums.department.service.DepartmentService;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
    
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public Department saveDepartment(Department department) {
		Department savedDepartment = null;
		log.info("DepartmentService.saveDepartment() Entered");
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		//check if dept already exists
		if(isDepartmentNameExists(department)) {
			throw new DepartmentNameExistsException(ErrorCodeMessages.ERR_DEPT_ID_ALREADY_EXISTS_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_ALREADY_EXISTS_MSG);
		}
		log.info("DepartmentService.saveDepartment() is under execution");
		//set current date time for newly inserted record
		department.setCreatedDateTime(LocalDateTime.now());
		
		savedDepartment = departmentRepository.save(department);
		log.info("DepartmentService.saveDepartment() executed successfully");
		return savedDepartment;
	}

	@Override
	public Department findDepartmentById(Long departmentId) {
		log.info("DepartmentService.findDepartmentById() Entered : departmentId : " + departmentId);
		Department retrievedDepartment = null;
		if (departmentId <= 0) {
			log.info("DepartmentService.findDepartmentById() in departmentId id is <= 0.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		}
		log.info("DepartmentService.findDepartmentById() is under execution...");
		retrievedDepartment = departmentRepository.findByDepartmentId(departmentId);
		if (retrievedDepartment == null) {
			log.info("DepartmentService.findDepartmentById() in retrievedDepartment is null.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		log.info("DepartmentService.findDepartmentById() executed successfully");
		return retrievedDepartment;
	}
	
	@Override
	public List<Department> getAllDepartments() {
		log.info("DepartmentService.getAllDepartments() Entered ");
		List<Department> departmentList = null;
		log.info("DepartmentService.getAllDepartments() is under execution...");
		departmentList = departmentRepository.findAll();
		if (departmentList == null || departmentList.isEmpty() || departmentList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		log.info("DepartmentService.getAllDepartments() executed successfully");
		StringBuilder st = new StringBuilder();
		departmentList.forEach(department ->{
			st.append(department.getDepartmentHead()+",");
		});
		ResponseEntity<List<com.ikn.ums.department.VO.EmployeeVO>> responseEntity = restTemplate.exchange(
			    "http://UMS-EMPLOYEE-SERVICE/employees/attendees/"+st,
			    HttpMethod.GET,
			    null,
			    new ParameterizedTypeReference<List<com.ikn.ums.department.VO.EmployeeVO>>() {}
		);
		List<EmployeeVO> updatedDepartmentList = responseEntity.getBody();
		System.out.println("the employee updated list is"+responseEntity.getBody());
		List<Department> updatedList = departmentList;
		updatedDepartmentList.forEach(employee ->{
			for(int i=0; i<updatedList.size();i++) {
				Department department = updatedList.get(i);
				System.out.println("current department Object:"+ department);			
				if((employee.getEmail()).equals(updatedList.get(i).getDepartmentHead())) {
					System.out.println("Equal matches");
					updatedList.set(i,department).setDepartmentHead(employee.getFirstName()+" "+employee.getLastName());
					
				}
			}
			
		});
		System.out.println("updated Department List is"+updatedList);
		return updatedList;
	}

	@Override
	public void deleteDepartment(Long departmentId) {
		log.info("DepartmentService.deleteDepartment() Entered ");
		if (departmentId == 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		log.info("DepartmentService.deleteDepartment() is under execution...");
		departmentRepository.deleteById(departmentId);
		log.info("DepartmentService.deleteDepartment() executed successfully");
	}

	@Override
	public Department updateDepartment(Department department) {
		Department updatedDepartment = null;
		log.info("DepartmentService.updateDepartment() Entered with args : department");
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		log.info("DepartmentService.updateDepartment() is under execution...");
		Department dbDepartment = null;
		if(department.getDepartmentId() != null) {
			dbDepartment = departmentRepository.findByDepartmentId(department.getDepartmentId());
			dbDepartment.setDepartmentName(department.getDepartmentName());
			dbDepartment.setDepartmentCode(department.getDepartmentCode());
			dbDepartment.setDepartmentAddress(department.getDepartmentAddress());
			dbDepartment.setDepartmentHead(department.getDepartmentHead());
			dbDepartment.setModifiedBy(department.getModifiedBy());
			dbDepartment.setModifiedDateTime(LocalDateTime.now());
		}
		updatedDepartment = departmentRepository.save(dbDepartment);
		log.info("DepartmentService.updateDepartment() executed successfully");
		return updatedDepartment;
		
	}

	@Transactional
	@Override
	public void deleteSelectedDepartmentsByIds(List<Long> ids) {
		log.info("DepartmentServiceImpl.deleteSelectedDepartmentsByIds() ENTERED with args : ids" );
		if(ids.size() < 1) {
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE, 
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		}
		log.info("DepartmentServiceImpl.deleteSelectedDepartmentsByIds() is under execution...");
		List<Department> departmentList = departmentRepository.findAllById(ids);
		if(departmentList.size() > 0) {
			departmentRepository.deleteAll(departmentList);
		}
		log.info("DepartmentServiceImpl.deleteSelectedDepartmentsByIds() executed successfully");
		
	}
	
	public boolean isDepartmentNameExists(Department department) {
		log.info("DepartmentServiceImpl.isDepartmentNameExists() ENTERED : department : " );
		boolean isDeptNameExists = false;
		
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		} else {
			log.info("DepartmentServiceImpl.isDepartmentNameExists() is under execution..." );
			log.info("DepartmentServiceImpl  : Dept Id : " + department.getDepartmentId() + " Dept Name : " + department.getDepartmentName());
			Optional<Department> optRole = departmentRepository.findByDepartmentName( department.getDepartmentName() );
		//	isRoleNameExists = optRole.get().getRoleName().equalsIgnoreCase(role.getRoleName());
			isDeptNameExists = optRole.isPresent();
			log.info("DepartmentServiceImpl  : isDeptNameExists : " + isDeptNameExists);
		}
		log.info("DepartmentServiceImpl.isDepartmentNameExists() executed successfully" );
		return isDeptNameExists;
	}

}
