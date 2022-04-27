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

import org.ccpm.dashboard.dtos.MesaDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.MesaService;
import org.ccpm.dashboard.specifications.MesaSpecs;
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
public class MesaMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = 4458882191824695148L;
	
	@Autowired
	private MesaService mesaService;
	
	private String nombre;
	private String descripcion;
	private Integer nro;

	private String descripcionBsq;
	private String nombreBsq;
	private String codigoPostalBsq;
	
	private List<SelectItem> selectItems = new ArrayList<>();

	public MesaMB() {
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
			MesaDto mesaDto = new MesaDto();
			mesaDto.setNombre(nombre);
			mesaDto.setDescripcion(descripcion);
			mesaDto.setNro(nro);
			this.mesaService.create(mesaDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMesa");
			this.setMsgSuccessError("Meta creada correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMesa");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			this.mesaService.create(new MesaDto(null,this.getNombre().toUpperCase(), nro, descripcion, true));
			this.setResultado("nuevoMesa");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMesa");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idMesaAux = Long.parseLong(myRequest.getParameter("id"));
		MesaDto mesaAux = this.mesaService.find(idMesaAux);
		this.setId(mesaAux.getId());		
		this.setNombre(mesaAux.getNombre());
		this.setDescripcion(mesaAux.getDescripcion());
		this.setNro(mesaAux.getNro());
	
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long idMesaAux = Long.parseLong(myRequest.getParameter("id"));
			this.mesaService.remove(idMesaAux);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("mesaConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMesa");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idMunicipoAux = Long.parseLong(myRequest.getParameter("id"));
		MesaDto mesaAux = this.mesaService.find(idMunicipoAux);
		this.setId(mesaAux.getId());
		this.setNombre(mesaAux.getNombre());
		this.setDescripcion(mesaAux.getDescripcion());
		this.setNro(mesaAux.getNro());
	}

	@Override
	public String guardarEdicion() {
		try {
			this.mesaService.create(new MesaDto(null,this.getNombre().toUpperCase(), nro, descripcion, true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMesa");
			this.setMsgSuccessError("Mesa editado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMesa");
		}
		return this.getResultado();
	}

	

	@Override
	public void actualizar() {
		
		this.setNombreBsq(null);
		
		super.actualizar();
	}

	@Override
	public LazyDataModel<MesaDto> getListElements() {
		LazyDataModel<MesaDto> userDataList = new LazyDataModel<MesaDto>() {
			private static final long serialVersionUID = -4501587204208399476L;
			
			
			@Override
			public List<MesaDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<MesaDto> result = new ArrayList<MesaDto>();
				
				if (first == 0) {
					result = mesaService.findAll(MesaSpecs.getMesaByNombre(getNombreBuscar())
							.and(MesaSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = mesaService.findAll(MesaSpecs.getMesaByNombre(getNombreBuscar())
							.and(MesaSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				}
			
				
				return result;
			}


		};
		
		userDataList.setRowCount(mesaService.count(
				MesaSpecs.getMesaByNombre(getNombreBuscar()).and(MesaSpecs.getByEstado(true)))
				.intValue());
		
		userDataList.setRowCount(1);
		return userDataList;
	}

}
