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
import com.ikn.ums.department.exception.DepartmentInUsageException;
import com.ikn.ums.department.exception.DepartmentNameExistsException;
import com.ikn.ums.department.exception.EmptyInputException;
import com.ikn.ums.department.exception.EmptyListException;
import com.ikn.ums.department.exception.EntityNotFoundException;
import com.ikn.ums.department.exception.ErrorCodeMessages;
import com.ikn.ums.department.repository.DepartmentRepository;
import com.ikn.ums.department.service.DepartmentService;
import com.ikn.ums.department.utils.DepartmentConstants;

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
		log.info("saveDepartment() Entered");
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		//check if dept already exists
		if(isDepartmentNameExists(department)) {
			throw new DepartmentNameExistsException(ErrorCodeMessages.ERR_DEPT_ID_ALREADY_EXISTS_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_ALREADY_EXISTS_MSG);
		}
		log.info("saveDepartment() is under execution");
		//set current date time for newly inserted record
		department.setCreatedDateTime(LocalDateTime.now());
		department.setActive(DepartmentConstants.isActive);
		savedDepartment = departmentRepository.save(department);
		log.info("saveDepartment() executed successfully");
		return savedDepartment;
	}

	@Override
	public Department findDepartmentById(Long departmentId) {
		log.info("findDepartmentById() Entered : departmentId : " + departmentId);
		Department retrievedDepartment = null;
		if (departmentId <= 0) {
			log.info("findDepartmentById() in departmentId id is <= 0.");
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		}
		log.info("findDepartmentById() is under execution...");
		retrievedDepartment = departmentRepository.findByDepartmentId(departmentId);
		if (retrievedDepartment == null) {
			log.info("findDepartmentById() in retrievedDepartment is null.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		log.info("findDepartmentById() executed successfully");
		return retrievedDepartment;
	}
	
	@Override
	public List<Department> getAllActiveDepartments() {
		log.info("getAllDepartments() Entered ");
		List<Department> departmentList = null;
		log.info("getAllDepartments() is under execution...");
		//departmentList = departmentRepository.findAll();
		departmentList = departmentRepository.findAllDepartmentByStatus(DepartmentConstants.isActive);
		if (departmentList == null || departmentList.isEmpty() || departmentList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
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
		List<Department> updatedList = departmentList;
		updatedDepartmentList.forEach(employee ->{
			for(int i=0; i<updatedList.size();i++) {
				Department department = updatedList.get(i);		
				if((employee.getEmail()).equals(updatedList.get(i).getDepartmentHead())) {
					updatedList.set(i,department).setDepartmentHead(employee.getFirstName()+" "+employee.getLastName());
					
				}
			}
			
		});
		log.info("getAllDepartments() executed successfully");
		return updatedList;
	}
	
	@Override
	public List<Department> getAllDepartments() {
		log.info("getAllDepartments() Entered ");
		List<Department> departmentList = null;
		log.info("getAllDepartments() is under execution...");
		departmentList = departmentRepository.findAll();
		if (departmentList == null || departmentList.isEmpty() || departmentList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
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
		List<Department> updatedList = departmentList;
		updatedDepartmentList.forEach(employee ->{
			for(int i=0; i<updatedList.size();i++) {
				Department department = updatedList.get(i);		
				if((employee.getEmail()).equals(updatedList.get(i).getDepartmentHead())) {
					updatedList.set(i,department).setDepartmentHead(employee.getFirstName()+" "+employee.getLastName());
					
				}
			}
			
		});
		log.info("getAllDepartments() executed successfully");
		return updatedList;
	}

	@Override
	public void deleteDepartment(Long departmentId) {
		log.info("deleteDepartment() Entered ");
		if (departmentId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		log.info("deleteDepartment() is under execution...");
		Optional<Department> optDepartment = departmentRepository.findById(departmentId);
		if(!optDepartment.isPresent() || optDepartment.get() == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE, 
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}else {
		  Long departmentCount = departmentRepository.findDepartmentIdCount(departmentId);
		  if(departmentCount > 0) {
			throw new DepartmentInUsageException(ErrorCodeMessages.ERR_DEPT_IS_IN_USAGE_CODE, 
					ErrorCodeMessages.ERR_DEPT_IS_IN_USAGE_MSG);
		  }
		  //departmentRepository.deleteById(departmentId);
		  Department dbdept = optDepartment.get();
		  dbdept.setActive(DepartmentConstants.isInActive);
		  departmentRepository.save(optDepartment.get());
	    }
		log.info("deleteDepartment() executed successfully");
	}

	@Override
	public Department updateDepartment(Department department) {
		Department updatedDepartment = null;
		log.info("updateDepartment() Entered with args : department");
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		if(isDepartmentNameExists(department)) {
			throw new DepartmentNameExistsException(ErrorCodeMessages.ERR_DEPT_ID_ALREADY_EXISTS_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_ALREADY_EXISTS_MSG);
		}
		
		log.info("updateDepartment() is under execution...");
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
		log.info("updateDepartment() executed successfully");
		return updatedDepartment;
		
	}

	@Transactional
	@Override
	public void deleteSelectedDepartmentsByIds(List<Long> ids) {
		log.info("deleteSelectedDepartmentsByIds() ENTERED with args : ids" );
		if(ids.size() <= 0) {
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE, 
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		}
		log.info("deleteSelectedDepartmentsByIds() is under execution...");
		//List<Department> departmentList = departmentRepository.findAllById(ids);
		ids.forEach(id ->{
			Long departmentCount = departmentRepository.findDepartmentIdCount(id);
			if(departmentCount > 0) {
				throw new DepartmentInUsageException(ErrorCodeMessages.ERR_DEPT_IS_IN_USAGE_CODE, 
						ErrorCodeMessages.ERR_DEPT_IS_IN_USAGE_MSG);
			}
		});
		List<Department> deptList = departmentRepository.findAllById(ids);
		deptList.forEach(dbdept -> {
			dbdept.setActive(DepartmentConstants.isInActive);
		});
		departmentRepository.saveAll(deptList);
		//departmentRepository.deleteAllById(ids);
		log.info("deleteSelectedDepartmentsByIds() executed successfully");
		
	}
	
	public boolean isDepartmentNameExists(Department department) {
		log.info("isDepartmentNameExists() ENTERED : department : " );
		boolean isDeptNameExists = false;
		
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		} else {
			log.info("isDepartmentNameExists() is under execution..." );
			log.info("DepartmentServiceImpl  : Dept Id : " + department.getDepartmentId() + " Dept Name : " + department.getDepartmentName());
			Optional<Department> optRole = departmentRepository.findByDepartmentName( department.getDepartmentName() );
//			isRoleNameExists = optRole.get().getRoleName().equalsIgnoreCase(role.getRoleName());
		    isDeptNameExists = optRole.filter(dep -> !dep.getDepartmentId().equals(department.getDepartmentId())).isPresent();
			log.info("DepartmentServiceImpl  : isDeptNameExists : " + isDeptNameExists);
		}
		log.info("isDepartmentNameExists() executed successfully" );
		return isDeptNameExists;
	}

}
