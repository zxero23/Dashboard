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

import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.PermisoService;
import org.ccpm.dashboard.specifications.PermisoSpecs;
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
public class PermisoMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = -1589446212645090290L;
	@Autowired
	private PermisoService permisoService;
	private String nombre;
	private String descripcion;

	public PermisoMB() {
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
					if (permiso.getNombre().equalsIgnoreCase("nuevoPermiso")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarPermiso")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarPermiso")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detallePermiso")) {
						this.setDetalle(true);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("Error al recuperar los permisos del usuario");
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
			PermisoDto permisoDto = new PermisoDto();
			permisoDto.setNombre(this.getNombre());
			permisoDto.setDescripcion(this.getDescripcion());
			this.permisoService.create(permisoDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoPermiso");
			this.setMsgSuccessError("Permiso creado correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoPermiso");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			PermisoDto permisoDto = new PermisoDto();
			permisoDto.setNombre(this.getNombre());
			permisoDto.setDescripcion(this.getDescripcion());
			this.permisoService.create(permisoDto);
			this.setResultado("nuevoPermiso");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoPermiso");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(map.get("id").toString());
		PermisoDto permiso = this.permisoService.find(id);
		this.setId(permiso.getId());
		this.setNombre(permiso.getNombre());
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long id = Long.parseLong(myRequest.getParameter("id"));
			this.permisoService.remove(id);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("permisoConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoPermiso");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long id = Long.parseLong(myRequest.getParameter("id"));
		PermisoDto permisoAux = this.permisoService.find(id);
		this.setId(permisoAux.getId());
		this.setNombre(permisoAux.getNombre());
		this.setDescripcion(permisoAux.getDescripcion());
	}

	@Override
	public String guardarEdicion() {
		try {
			PermisoDto permisoDto = new PermisoDto();
			permisoDto.setId(this.getId());
			permisoDto.setNombre(this.getNombre());
			permisoDto.setDescripcion(this.getDescripcion());
			this.permisoService.edit(permisoDto);
			this.setTitle("Informaci�n");
			this.setImages("fa fa-check");
			this.setResultado("resultadoPermiso");
			this.setMsgSuccessError("Permiso creado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoPermiso");
		}
		return this.getResultado();
	}

	@Override
	public LazyDataModel<PermisoDto> getListElements() {
		LazyDataModel<PermisoDto> userDataList = new LazyDataModel<PermisoDto>() {
			private static final long serialVersionUID = -4501587204208399476L;

			@Override
			public List<PermisoDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<PermisoDto> result = new ArrayList<PermisoDto>();

				if (first == 0) {
					result = permisoService.findAll(
							PermisoSpecs.getPermisoByNombre(getNombreBuscar()).and(PermisoSpecs.getByEstado(true)),
							PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = permisoService.findAll(
							PermisoSpecs.getPermisoByNombre(getNombreBuscar()).and(PermisoSpecs.getByEstado(true)),
							PageRequest.of(first, pageSize));
				}

				return result;
			}

		};
		userDataList.setRowCount(permisoService
				.count(PermisoSpecs.getPermisoByNombre(getNombreBuscar()).and(PermisoSpecs.getByEstado(true)))
				.intValue());
		return userDataList;
	}

	@Override
	public List<SelectItem> getSelectItems() {
		return null;
	}
}
