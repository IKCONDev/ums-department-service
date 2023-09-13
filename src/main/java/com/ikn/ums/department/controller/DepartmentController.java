package com.ikn.ums.department.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikn.ums.department.VO.DepartmentListVO;
import com.ikn.ums.department.entity.Department;
import com.ikn.ums.department.exception.ControllerException;
import com.ikn.ums.department.exception.EmptyInputException;
import com.ikn.ums.department.exception.ErrorCodeMessages;
import com.ikn.ums.department.service.impl.DepartmentServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

	@Autowired
	private DepartmentServiceImpl departmentService;

	@PostMapping("/")
	public ResponseEntity<?> saveDepartment(@RequestBody Department department) {
		ResponseEntity<?> reEntity = null;
		log.info("DepartmentController.saveDepartment() Entered" + department.getDepartmentId());
		try {
			Department dbDepartment = departmentService.saveDepartment(department);
			reEntity = new ResponseEntity<Department>(dbDepartment, HttpStatus.CREATED);
			log.info("DepartmentController.saveDepartment() Exited successfully");
			return reEntity;
		} catch (Exception e) {
			ControllerException umsCE = new ControllerException("<error code>", e.getStackTrace().toString());
			reEntity = new ResponseEntity<ControllerException>(umsCE, HttpStatus.INTERNAL_SERVER_ERROR);
			log.info("DepartmentController.saveDepartment() Exited with Controller exception " + umsCE);
			return reEntity;
		}
	}

	@GetMapping("/{id}")
	public Department findDepartmentById(@PathVariable("id") Long departmentId) {
		log.info("DepartmentController.findDepartmentById() Entered :: departmentId :: " + departmentId);
		return departmentService.findDepartmentById(departmentId);
	}

	@GetMapping("/get-all")
	public ResponseEntity<?> getAllDepartments() {
		log.info("DepartmentController.getAllDepartments() ENTERED");
		List<Department> departmentDbList = departmentService.getAllDepartments();
		DepartmentListVO departmentListVO = new DepartmentListVO();
		departmentListVO.setDepartment(departmentDbList);
		return new ResponseEntity<>(departmentListVO, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable("id") Long departmentId) {
		log.info("DepartmentController.deleteDepartment() ENTERED : departmentId : " + departmentId);
		if (departmentId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		departmentService.deleteDepartment(departmentId);
		return ResponseEntity.ok("Department deleted successfully");
	}

}
