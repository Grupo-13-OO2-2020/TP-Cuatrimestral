package Grupo13OO2.services;

import java.util.List;
import Grupo13OO2.Entities.Cliente;
import Grupo13OO2.Models.*;

public interface IClienteService {
	public List<Cliente> getAll();

	public ClienteModel insertOrUpdate(ClienteModel personaModel);

	public ClienteModel ListarId(int id);

	public String delete(int id);
}