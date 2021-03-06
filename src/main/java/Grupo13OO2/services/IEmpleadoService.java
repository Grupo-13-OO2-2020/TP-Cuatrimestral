package Grupo13OO2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Grupo13OO2.Models.EmpleadoModel;

public interface IEmpleadoService {
	public List<EmpleadoModel> getAll();

	public EmpleadoModel insertOrUpdate(EmpleadoModel empleadoModel);

	public EmpleadoModel ListarId(int id);

	public String delete(int id);
	
	public Page<EmpleadoModel> getAllPages(Pageable pageable);

	public Page<EmpleadoModel> getAllPagesLocal(Pageable pageable, int id);

	public double sueldoxEmpleado(EmpleadoModel empleado);

	public boolean findDependency(int id);
}