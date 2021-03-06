package Grupo13OO2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Grupo13OO2.Entities.Producto;
import Grupo13OO2.Models.ProductoModel;

public interface IProductoService {

	public List<Producto> getAll();

	public ProductoModel insertOrUpdate(ProductoModel productoModel);

	public ProductoModel ListarId(int id);

	public String delete(int id);

	public Page<ProductoModel> getAllPages(Pageable pageable);

	public List<ProductoModel> listAll(String keyword);

	public List<ProductoModel> findDependency(int id);
}
