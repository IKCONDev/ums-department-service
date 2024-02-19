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
import com.ikn.ums.department.exception.DepartmentInUsageException;
import com.ikn.ums.department.exception.DepartmentNameExistsException;
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
	public ResponseEntity<Department> saveDepartment(@RequestBody Department department) {
		log.info("saveDepartment() Entered : department : " + department);
		if (department == null) {
			log.info("saveDepartment() : department Object is NULL !");
			throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
					ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
		}
		try {
			Department savedDepartment = departmentService.saveDepartment(department);
			log.info("saveDepartment() : Post Employee method calling .... " + savedDepartment);
			return new ResponseEntity<Department>(savedDepartment, HttpStatus.CREATED);
		} 
		catch (DepartmentNameExistsException | EntityNotFoundException businessException) {
			log.error("saveDepartment() : Exception Occured !" + businessException.getMessage(), businessException);
			throw businessException;
		}
		catch (Exception e) {
			log.error("saveDepartment() : Exception Occured !" + e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.DEPT_SAVE_SUCCESS_CODE,
					ErrorCodeMessages.DEPT_SAVE_SUCCESS_MSG);
		}
	}

	@PutMapping("/update")
		public ResponseEntity<Department> updateDepartment(@RequestBody Department department) {
			log.info("updateDepartment() ENTERED ");
			Department updatedDeparment = new Department();
			if (department == null)
				throw new EntityNotFoundException(ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_CODE,
						ErrorCodeMessages.ERR_DEPT_ENTITY_IS_NULL_MSG);
			try {
				log.info("updateDepartment() is under execution...");
				updatedDeparment = departmentService.updateDepartment(department);
				log.info("updateDepartment() executed successfully");
				return new ResponseEntity<Department>(updatedDeparment, HttpStatus.CREATED);
			}
			catch (DepartmentNameExistsException | EntityNotFoundException businessException) {
				log.error("updateEmployee() : Exception Occured !" + businessException.getMessage(),businessException);
				throw businessException;
			}
			catch (Exception e) {
				log.error("updateEmployee() : Exception Occured !" + e.getMessage(),e);
				throw new ControllerException(ErrorCodeMessages.ERR_DEPT_UPDATE_UNSUCCESS_CODE,
						ErrorCodeMessages.ERR_DEPT_UPDATE_UNSUCCESS_MSG);
			}
		}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Boolean> deleteDepartment(@PathVariable("id") Long departmentId) {
		boolean isDeleted= false;
		log.info("DepartmentController.deleteDepartment() ENTERED : departmentId : " + departmentId);
		if (departmentId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		try {
			log.info("deleteDepartment() is under execution...");
			departmentService.deleteDepartment(departmentId);
			isDeleted = true;
			log.info("deleteDepartment() executed successfully");
			return new ResponseEntity<>(isDeleted, HttpStatus.OK);
		}
		catch (EmptyListException | DepartmentInUsageException businessException) {
			log.error("deleteDepartment() : Exception Occured while deleting Department !"+ businessException.getMessage(),
					businessException);
			throw businessException;
		} 
		catch (Exception e) {
			log.error("deleteDepartment() : Exception Occured while deleting Department !"
					+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_DELETE_UNSUCCESS_CODE);
		}
	}

	@GetMapping("/{id}")
	public Department findDepartmentById(@PathVariable("id") Long departmentId) {
		log.info("findDepartmentById() Entered :: departmentId :: " + departmentId);

		if (departmentId <= 0)
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE,
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		try {
			log.info("findDepartmentById() is under execution...");
			System.out.println(departmentService.findDepartmentById(departmentId));
			log.info("findDepartmentById() executed successfully");
			return departmentService.findDepartmentById(departmentId);
		} catch (Exception e) {
			log.error("findDepartmentById() : Exception Occured while getting Department Details !"
					+ e.getMessage(),e);
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_DETAILS_GET_UNSUCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_DETAILS_GET_UNSUCESS_MSG);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<Department>> getAllDepartments() {
		log.info("getAllDepartments() ENTERED");
		log.info("getAllDepartments() is under execution...");
		List<Department> departmentDbList = departmentService.getAllDepartments();
		//DepartmentListVO departmentListVO = new DepartmentListVO();
		if ( departmentDbList.isEmpty() )
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		try {
			log.info("getAllDepartments() executed successfully");
			//departmentListVO.setDepartment(departmentDbList);
			return new ResponseEntity<>(departmentDbList, HttpStatus.OK);
		}catch (Exception e) {
			log.error("getAllDepartments() : Exception Occured while getting All Department Details !"
					+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_RETRIEVE_ALL_UNSUCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_RETRIEVE_ALL_UNSUCESS_MSG);
		}
	}
	
	@GetMapping("/all/active")
	public ResponseEntity<List<Department>> getAllActiveDepartments() {
		log.info("getAllActiveDepartments() ENTERED");
		log.info("getAllActiveDepartments() is under execution...");
		List<Department> departmentDbList = departmentService.getAllActiveDepartments();
		//DepartmentListVO departmentListVO = new DepartmentListVO();
		if ( departmentDbList.isEmpty() )
			throw new EmptyListException(ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_CODE,
					ErrorCodeMessages.ERR_DEPT_LIST_IS_EMPTY_MSG);
		try {
			log.info("getAllActiveDepartments() executed successfully");
			//departmentListVO.setDepartment(departmentDbList);
			return new ResponseEntity<>(departmentDbList, HttpStatus.OK);
		}catch (Exception e) {
			log.error("getAllActiveDepartments() : Exception Occured while getting All Department Details !"
					+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_RETRIEVE_ALL_UNSUCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_RETRIEVE_ALL_UNSUCESS_MSG);
		}
	}
	
	@DeleteMapping("/delete/all/{ids}")
	public ResponseEntity<Boolean> deleteAllDepartmentsByIds(@PathVariable List<Long> ids){
		log.info("deleteAllDepartmentsByIds() ENTERED with args: ids");
		if(ids == null || ids.size() == 0 || ids.equals((null))){
			throw new EmptyInputException(ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_CODE, 
					ErrorCodeMessages.ERR_DEPT_ID_NOT_FOUND_MSG);
		}
		try {
			log.info("deleteAllDepartmentsByIds() is under execution..");
			departmentService.deleteSelectedDepartmentsByIds(ids);
			log.info("deleteAllDepartmentsByIds() executed successfully");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		catch (EmptyListException | DepartmentInUsageException businessException) {
			log.error("deleteAllDepartmentsByIds() : Exception Occured while deleting Department Details !"
					+ businessException.getMessage(), businessException);
			throw businessException;
		} 
		catch (Exception e) {
			log.error("deleteAllDepartmentsByIds() : Exception Occured while deleting Department Details !"
					+ e.getMessage(), e);
			throw new ControllerException(ErrorCodeMessages.ERR_DEPT_DELETE_UNSUCCESS_CODE,
					ErrorCodeMessages.ERR_DEPT_DELETE_UNSUCCESS_MSG);
		}
	}

}
