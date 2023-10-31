package com.ikn.ums.department.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
		//set current date time for newly inserted record
			department.setCreatedDateTime(LocalDateTime.now());
		
		savedDepartment = departmentRepository.save(department);
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
		retrievedDepartment = departmentRepository.findByDepartmentId(departmentId);
		if (retrievedDepartment == null) {
			log.info("DepartmentService.findDepartmentById() in retrievedDepartment is null.");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		return retrievedDepartment;
	}
	
	@Override
	public List<Department> getAllDepartments() {
		log.info("DepartmentService.getAllDepartments() Entered ");
		List<Department> departmentList = null;
		departmentList = departmentRepository.findAll();
		if (departmentList == null || departmentList.isEmpty() || departmentList.size() == 0 )
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		return departmentList;
	}

	@Override
	public void deleteDepartment(Long departmentId) {
		log.info("DepartmentService.deleteDepartment() Entered ");
		if (departmentId == 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		departmentRepository.deleteById(departmentId);
	}

	@Override
	public Department updateDepartment(Department department) {
		Department updatedDepartment = null;
		log.info("DepartmentService.updateDepartment() Entered");
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
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
		return updatedDepartment;
		
	}

	@Transactional
	@Override
	public void deleteSelectedDepartmentsByIds(List<Long> ids) {
		if(ids.size() < 1) {
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE, 
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		}
		List<Department> departmentList = departmentRepository.findAllById(ids);
		if(departmentList.size() > 0) {
			departmentRepository.deleteAll(departmentList);
		}
		
	}
	
	public boolean isDepartmentNameExists(Department department) {
		log.info("DepartmentServiceImpl.isDepartmentNameExists() ENTERED : role : " );
		boolean isDeptNameExists = false;
		
		if (department == null) {
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		} else {
			log.info("DepartmentServiceImpl  : Dept Id : " + department.getDepartmentId() + " Dept Name : " + department.getDepartmentName());
			Optional<Department> optRole = departmentRepository.findByDepartmentName( department.getDepartmentName() );
		//	isRoleNameExists = optRole.get().getRoleName().equalsIgnoreCase(role.getRoleName());
			isDeptNameExists = optRole.isPresent();
			log.info("DepartmentServiceImpl  : isDeptNameExists : " + isDeptNameExists);
		}
		return isDeptNameExists;
	}

}
