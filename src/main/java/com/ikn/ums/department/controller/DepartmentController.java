package com.ikn.ums.department.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.department.VO.DepartmentListVO;
import com.ikn.ums.department.entity.Department;
import com.ikn.ums.department.exception.ControllerException;
import com.ikn.ums.department.exception.EmptyInputException;
import com.ikn.ums.department.exception.EmptyListException;
import com.ikn.ums.department.exception.EntityNotFoundException;
import com.ikn.ums.department.exception.ErrorCodeMessages;
import com.ikn.ums.department.service.impl.DepartmentServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

	@Autowired
	private DepartmentServiceImpl departmentService;

	@PostMapping("/save")
	public ResponseEntity<?> saveDepartment(@RequestBody Department department) {
		log.info("DepartmentController.saveDepartment() Entered : department : " + department);
		if (department == null) {
			log.info("DepartmentController.saveDepartment() : department Object is NULL !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		try {
			Department savedDepartment = departmentService.saveDepartment(department);
			log.info("DepartmentController.saveDepartmente() : Post Employee method calling .... " + savedDepartment);
			return new ResponseEntity<Department>(savedDepartment, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("DepartmentController.saveDepartment() : Exception Occured !" + e.fillInStackTrace());
			throw new ControllerException(ErrorCodeMessages.DEPT_SAVE_SUCCESS_CODE,
					ErrorCodeMessages.DEPT_SAVE_SUCCESS_MSG);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Department> updateDepartment(@RequestBody Department department) {
		log.info("DepartmentController.updateDepartment() ENTERED ");
		Department updatedDeparment = new Department();
		if (department == null)
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		try {
			updatedDeparment = departmentService.updateDepartment(department);
			return new ResponseEntity<Department>(updatedDeparment, HttpStatus.CREATED);
		} catch (Exception e) {
			log.info("DepartmentController.updateEmployee() : Exception Occured !" + e.fillInStackTrace());
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_UPDATE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_UPDATE_UNSUCCESS_MSG);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteDepartment(@PathVariable("id") Long departmentId) {
		boolean isDeleted= false;
		log.info("DepartmentController.deleteDepartment() ENTERED : departmentId : " + departmentId);
		if (departmentId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		try {
			departmentService.deleteDepartment(departmentId);
			isDeleted = true;
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		} catch (Exception e) {
			log.info("DepartmentController.deleteDepartment() : Exception Occured while deleting Department !"
					+ e.fillInStackTrace());
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_DELETE_UNSUCCESS_CODE);
		}
	}

	@GetMapping("/{id}")
	public Department findDepartmentById(@PathVariable("id") Long departmentId) {
		log.info("DepartmentController.findDepartmentById() Entered :: departmentId :: " + departmentId);

		if (departmentId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		try {
			System.out.println(departmentService.findDepartmentById(departmentId));
			return departmentService.findDepartmentById(departmentId);
		} catch (Exception e) {
			log.info("DepartmentController.findDepartmentById() : Exception Occured while getting Department Details !"
					+ e.fillInStackTrace());
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_DETAILS_GET_UNSUCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_DETAILS_GET_UNSUCESS_MSG);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllDepartments() {
		log.info("DepartmentController.getAllDepartments() ENTERED");
		List<Department> departmentDbList = departmentService.getAllDepartments();
		//DepartmentListVO departmentListVO = new DepartmentListVO();
		if ( departmentDbList.isEmpty() )
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		try {
			//departmentListVO.setDepartment(departmentDbList);
			return new ResponseEntity<>(departmentDbList, HttpStatus.OK);
		}catch (Exception e) {
			log.info("DepartmentController.getAllDepartments() : Exception Occured while getting All Department Details !"
					+ e.fillInStackTrace());
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_RETRIEVE_ALL_UNSUCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_RETRIEVE_ALL_UNSUCESS_MSG);
		}
	}

}
