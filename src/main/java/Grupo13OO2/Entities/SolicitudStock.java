package Grupo13OO2.Entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import Grupo13OO2.Entities.Empleado;
import Grupo13OO2.Entities.Local;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Table(name = "solicitudStock")
public class SolicitudStock extends Pedido {
	@OneToOne
	@JoinColumn(name = "colaborador_id", referencedColumnName = "id")
	private Empleado colaborador;
	private boolean aceptado;
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "local_id", referencedColumnName = "id")
	private Local localDestinatario;

	public SolicitudStock() {
	}

	public SolicitudStock(int id, Date fecha, Producto producto, int cantidad, Empleado vendedor, Cliente cliente,
			Empleado colaborador, boolean aceptado, Local localDestinatario) {
		super(id, fecha, producto, cantidad, vendedor, cliente);
		this.colaborador = colaborador;
		this.aceptado = aceptado;
		this.localDestinatario = localDestinatario;
	}

	public Empleado getColaborador() {
		return colaborador;
	}

	public void setColaborador(Empleado colaborador) {
		this.colaborador = colaborador;
	}

	public boolean isAceptado() {
		return aceptado;
	}

	public void setAceptado(boolean aceptado) {
		this.aceptado = aceptado;
	}

	public Local getLocalDestinatario() {
		return localDestinatario;
	}

	public void setLocalDestinatario(Local localDestinatario) {
		this.localDestinatario = localDestinatario;
	}

}
