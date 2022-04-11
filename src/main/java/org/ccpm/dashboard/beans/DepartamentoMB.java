package org.ccpm.dashboard.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.ccpm.dashboard.dtos.DepartamentoDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.DepartamentoService;
import org.ccpm.dashboard.specifications.DepartamentoSpecs;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Named
@SessionScoped
public class DepartamentoMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = 6293155046843300471L;
	@Autowired
	private DepartamentoService departamentoService;
	private String nombre;
	private Integer distrito;

	public DepartamentoMB() {
	}

	@PostConstruct
	private void init() {
		WebMB sessionBean = this.getSessionBean();
		if (sessionBean != null) {
			try {
				this.prepararPermisos(sessionBean.getUsuario());
			} catch (Exception e) {
			}
		}
	}

	public void prepararPermisos(UsuarioDto usuario) throws Exception {
		try {
			if (usuario.getRol() != null) {
				for (PermisoDto permiso : usuario.getRol().getPermisos()) {
					if (permiso.getNombre().equalsIgnoreCase("nuevoDepartamento")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarDepartamento")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarDepartamento")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleDepartamento")) {
						this.setDetalle(true);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("Error al recuperar las permisos del usuario");
		}
	}

	@Override
	public void limpiar() {
		this.setNombre(null);
		this.setNombreBuscar("");
	}

	@Override
	public String crear() {
		try {
			this.departamentoService
					.create(new DepartamentoDto(null, this.getNombre().toUpperCase(), true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoDepartamento");
			this.setMsgSuccessError("Departamento creado correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoDepartamento");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			this.departamentoService
					.create(new DepartamentoDto(null, this.getNombre().toUpperCase(), true));
			this.setResultado("nuevoDepartamento");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoDepartamento");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idDepartamentoAux = Long.parseLong(myRequest.getParameter("id"));
		DepartamentoDto departamentoAux = this.departamentoService.find(idDepartamentoAux);
		this.setId(departamentoAux.getId());
		this.setNombre(departamentoAux.getNombre());
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long idDepartamentoAux = Long.parseLong(myRequest.getParameter("id"));
			this.departamentoService.remove(idDepartamentoAux);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("departamentoConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoDepartamento");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idDepartamentoAux = Long.parseLong(myRequest.getParameter("id"));
		DepartamentoDto departamentoAux = this.departamentoService.find(idDepartamentoAux);
		this.setId(departamentoAux.getId());
		this.setNombre(departamentoAux.getNombre());
	}

	@Override
	public String guardarEdicion() {
		try {
			this.departamentoService
					.edit(new DepartamentoDto(this.getId(), this.getNombre().toUpperCase(), true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoDepartamento");
			this.setMsgSuccessError("Departamento editado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoDepartamento");
		}
		return this.getResultado();
	}

	@Override
	public List<SelectItem> getSelectItems() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public LazyDataModel<DepartamentoDto> getListElements() {
		LazyDataModel<DepartamentoDto> userDataList = new LazyDataModel<DepartamentoDto>() {
			private static final long serialVersionUID = -4501587204208399476L;

			@Override
			public List<DepartamentoDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<DepartamentoDto> result = new ArrayList<DepartamentoDto>();

				if (first == 0) {
					result = departamentoService.findAll(DepartamentoSpecs.getDepartamentoByNombre(getNombreBuscar())
							.and(DepartamentoSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = departamentoService.findAll(DepartamentoSpecs.getDepartamentoByNombre(getNombreBuscar())
							.and(DepartamentoSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				}

				return result;
			}


		};
		userDataList.setRowCount(departamentoService.count(
				DepartamentoSpecs.getDepartamentoByNombre(getNombreBuscar()).and(DepartamentoSpecs.getByEstado(true)))
				.intValue());
		return userDataList;
	}

}
