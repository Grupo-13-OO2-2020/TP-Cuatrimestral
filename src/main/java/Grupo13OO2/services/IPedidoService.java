package Grupo13OO2.services;

import java.util.List;


import Grupo13OO2.Entities.Pedido;
import Grupo13OO2.Models.*;

public interface IPedidoService {

	public List<Pedido> getAll();

	public PedidoModel insertOrUpdate(PedidoModel pedidoModel);

	public PedidoModel ListarId(int id);

	public String delete(int id);

	public List<Pedido> listAll(String keyword);
}
