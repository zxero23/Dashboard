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

import org.ccpm.dashboard.dtos.ObjetivosDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.ObjetivosService;
import org.ccpm.dashboard.specifications.ObjetivosSpecs;
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
public class ObjetivosMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = 4458882191824695148L;
	
	@Autowired
	private ObjetivosService objetivosService;
	
	private String nombre;
	private String descripcion;
	private Integer nro;

	private String descripcionBsq;
	private String nombreBsq;
	private String codigoPostalBsq;
	
	private List<SelectItem> selectItems = new ArrayList<>();

	public ObjetivosMB() {
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
					if (permiso.getNombre().equalsIgnoreCase("nuevoObjetivo")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarObjetivo")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarObjetivo")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleObjetivo")) {
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
		this.setNombreBuscar("");
		this.setNombre(null);
		this.setDescripcion(null);
		this.setNro(null);
	}

	@Override
	public String crear() {
		try {
			ObjetivosDto objetivosDto = new ObjetivosDto();
			objetivosDto.setNombre(nombre);
			objetivosDto.setDescripcion(descripcion);
			objetivosDto.setNro(nro);
			this.objetivosService.create(objetivosDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoObjetivo");
			this.setMsgSuccessError("Objetivo creada correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoObjetivo");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			this.objetivosService.create(new ObjetivosDto(null,this.getNombre().toUpperCase(), nro, descripcion, true));
			this.setResultado("nuevoObjetivos");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoObjetivos");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idObjetivosAux = Long.parseLong(myRequest.getParameter("id"));
		ObjetivosDto objetivosAux = this.objetivosService.find(idObjetivosAux);
		this.setId(objetivosAux.getId());		
		this.setNombre(objetivosAux.getNombre());
		this.setDescripcion(objetivosAux.getDescripcion());
		this.setNro(objetivosAux.getNro());
	
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long idObjetivosAux = Long.parseLong(myRequest.getParameter("id"));
			this.objetivosService.remove(idObjetivosAux);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("objetivosConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoObjetivos");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idMunicipoAux = Long.parseLong(myRequest.getParameter("id"));
		ObjetivosDto objetivosAux = this.objetivosService.find(idMunicipoAux);
		this.setId(objetivosAux.getId());
		this.setNombre(objetivosAux.getNombre());
		this.setDescripcion(objetivosAux.getDescripcion());
		this.setNro(objetivosAux.getNro());
	}

	@Override
	public String guardarEdicion() {
		try {
			this.objetivosService.create(new ObjetivosDto(null,this.getNombre().toUpperCase(), nro, descripcion, true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoObjetivos");
			this.setMsgSuccessError("Objetivos editado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoObjetivos");
		}
		return this.getResultado();
	}

	

	@Override
	public void actualizar() {
		
		this.setNombreBsq(null);
		
		super.actualizar();
	}

	@Override
	public LazyDataModel<ObjetivosDto> getListElements() {
		LazyDataModel<ObjetivosDto> userDataList = new LazyDataModel<ObjetivosDto>() {
			private static final long serialVersionUID = -4501587204208399476L;
			
			
			@Override
			public List<ObjetivosDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<ObjetivosDto> result = new ArrayList<ObjetivosDto>();
				
				if (first == 0) {
					result = objetivosService.findAll(ObjetivosSpecs.getObjetivosByNombre(getNombreBuscar())
							.and(ObjetivosSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = objetivosService.findAll(ObjetivosSpecs.getObjetivosByNombre(getNombreBuscar())
							.and(ObjetivosSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				}
			
				
				return result;
			}


		};
		
		userDataList.setRowCount(objetivosService.count(
				ObjetivosSpecs.getObjetivosByNombre(getNombreBuscar()).and(ObjetivosSpecs.getByEstado(true)))
				.intValue());
		
		userDataList.setRowCount(1);
		return userDataList;
	}

}
