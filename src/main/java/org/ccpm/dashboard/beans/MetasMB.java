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

import org.ccpm.dashboard.dtos.MetasDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.MetasService;
import org.ccpm.dashboard.specifications.MetasSpecs;
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
public class MetasMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = 4458882191824695148L;
	
	@Autowired
	private MetasService metasService;
	
	private String nombre;
	private String descripcion;
	private Integer nro;

	private String descripcionBsq;
	private String nombreBsq;
	private String codigoPostalBsq;
	
	private List<SelectItem> selectItems = new ArrayList<>();

	public MetasMB() {
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
					if (permiso.getNombre().equalsIgnoreCase("nuevaMeta")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarMeta")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarMeta")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleMeta")) {
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
			MetasDto metasDto = new MetasDto();
			metasDto.setNombre(nombre);
			metasDto.setDescripcion(descripcion);
			metasDto.setNro(nro);
			this.metasService.create(metasDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMetas");
			this.setMsgSuccessError("Meta creada correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMetas");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			this.metasService.create(new MetasDto(null,this.getNombre().toUpperCase(), nro, descripcion, true));
			this.setResultado("nuevoMetas");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMetas");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idMetasAux = Long.parseLong(myRequest.getParameter("id"));
		MetasDto metasAux = this.metasService.find(idMetasAux);
		this.setId(metasAux.getId());		
		this.setNombre(metasAux.getNombre());
		this.setDescripcion(metasAux.getDescripcion());
		this.setNro(metasAux.getNro());
	
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long idMetasAux = Long.parseLong(myRequest.getParameter("id"));
			this.metasService.remove(idMetasAux);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("metasConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMetas");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idMunicipoAux = Long.parseLong(myRequest.getParameter("id"));
		MetasDto metasAux = this.metasService.find(idMunicipoAux);
		this.setId(metasAux.getId());
		this.setNombre(metasAux.getNombre());
		this.setDescripcion(metasAux.getDescripcion());
		this.setNro(metasAux.getNro());
	}

	@Override
	public String guardarEdicion() {
		try {
			this.metasService.create(new MetasDto(null,this.getNombre().toUpperCase(), nro, descripcion, true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMetas");
			this.setMsgSuccessError("Metas editado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMetas");
		}
		return this.getResultado();
	}

	

	@Override
	public void actualizar() {
		
		this.setNombreBsq(null);
		
		super.actualizar();
	}

	@Override
	public LazyDataModel<MetasDto> getListElements() {
		LazyDataModel<MetasDto> userDataList = new LazyDataModel<MetasDto>() {
			private static final long serialVersionUID = -4501587204208399476L;
			
			
			@Override
			public List<MetasDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<MetasDto> result = new ArrayList<MetasDto>();
				
				if (first == 0) {
					result = metasService.findAll(MetasSpecs.getMetasByNombre(getNombreBuscar())
							.and(MetasSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = metasService.findAll(MetasSpecs.getMetasByNombre(getNombreBuscar())
							.and(MetasSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				}
			
				
				return result;
			}


		};
		
		userDataList.setRowCount(metasService.count(
				MetasSpecs.getMetasByNombre(getNombreBuscar()).and(MetasSpecs.getByEstado(true)))
				.intValue());
		
		userDataList.setRowCount(1);
		return userDataList;
	}

}
