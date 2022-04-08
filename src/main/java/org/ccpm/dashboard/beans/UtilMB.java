package org.ccpm.dashboard.beans;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.model.LazyDataModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UtilMB {

	private String nombreBuscar = "";
	private boolean alta, baja, modificacion, detalle;
	private List<?> list;
	private Long id;
	private String resultado;
	private String title;
	private String images;
	private Integer page = 0;
	boolean flag = true;
	private String msgSuccessError;

	public UtilMB() {
	}

	public abstract LazyDataModel<?> getListElements();

	public abstract List<SelectItem> getSelectItems();

	public abstract void limpiar();

	public abstract String crear();

	public abstract String crearOtro();

	public abstract void verDetalle();

	public abstract void guardarBorrado();

	public abstract void prepararParaEditar();

	public abstract String guardarEdicion() throws Exception;

	protected WebMB getSessionBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (WebMB) context.getApplication().evaluateExpressionGet(context, "#{webMB}",
				WebMB.class);
	}

	public void aplicarFiltro() {
		this.setFlag(true);
	}

	public void actualizar() {
		this.setPage(0);
		this.setFlag(true);
		this.setNombreBuscar("");

	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public String getVersion() throws Exception {
		return System.getProperty("proyect.version");
	}
}
