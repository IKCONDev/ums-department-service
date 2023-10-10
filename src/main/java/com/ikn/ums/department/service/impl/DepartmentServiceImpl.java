package com.ikn.ums.department.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikn.ums.department.entity.Department;
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
		//set current date time for newly inserted record
		if(department.getDepartmentId() < 1) {
			department.setCreatedDateTime(LocalDateTime.now());
		}else {
			//set modified date time for existing  record
			department.setModifiedDateTime(LocalDateTime.now());
		}
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

}
