package Grupo13OO2.Models;

public class ProductoModel {
	private int id;
	private String descripcion;
	private double precioUnitario;
	private int codigoProducto;
	private String talle;
	
	
	
	
	public ProductoModel() {
	}
	public ProductoModel(int id, String descripcion, double precioUnitario, int codigoProducto, String talle) {
	
		this.setId(id);
		this.descripcion = descripcion;
		this.precioUnitario = precioUnitario;
		this.codigoProducto = codigoProducto;
		this.talle = talle;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public int getCodigoProducto() {
		return codigoProducto;
	}
	public void setCodigoProducto(int codigoProducto) {
		this.codigoProducto = codigoProducto;
	}
	public String getTalle() {
		return talle;
	}
	public void setTalle(String talle) {
		this.talle = talle;
	}


}