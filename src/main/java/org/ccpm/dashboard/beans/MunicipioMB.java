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
import org.ccpm.dashboard.dtos.MunicipioDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.DepartamentoService;
import org.ccpm.dashboard.services.MunicipioService;
import org.ccpm.dashboard.specifications.DepartamentoSpecs;
import org.ccpm.dashboard.specifications.MunicipioSpecs;
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
public class MunicipioMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = 4458882191824695148L;
	@Autowired
	private DepartamentoService departamentoService;
	@Autowired
	private MunicipioService municipioService;
	private Long idDepto;
	private String nombre;
	private String codigoPostal;
	private String departamento;

	private Long idDeptoBsq;
	private String nombreBsq;
	private String codigoPostalBsq;
	
	private List<SelectItem> selectItems = new ArrayList<>();

	public MunicipioMB() {
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
		this.cargarSelectItems();
	}

	public void prepararPermisos(UsuarioDto usuario) throws Exception {
		try {
			if (usuario.getRol() != null) {
				for (PermisoDto permiso : usuario.getRol().getPermisos()) {
					if (permiso.getNombre().equalsIgnoreCase("nuevoMunicipio")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarMunicipio")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarMunicipio")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleMunicipio")) {
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
		this.setCodigoPostal(null);
		this.setDepartamento(null);
		this.setIdDepto(null);
	}

	@Override
	public String crear() {
		try {
			this.municipioService.create(new MunicipioDto(null, this.getNombre().toUpperCase(), this.getCodigoPostal(), this.departamentoService.find(this.getIdDepto()), true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMunicipio");
			this.setMsgSuccessError("Municipio creado correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMunicipio");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			this.municipioService.create(new MunicipioDto(null, this.getNombre().toUpperCase(), this.getCodigoPostal(), this.departamentoService.find(this.getIdDepto()), true));
			this.setResultado("nuevoMunicipio");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMunicipio");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idMunicipioAux = Long.parseLong(myRequest.getParameter("id"));
		MunicipioDto municipioAux = this.municipioService.find(idMunicipioAux);
		this.setId(municipioAux.getId());
		this.setDepartamento(municipioAux.getDepartamento().getNombre());
		this.setNombre(municipioAux.getNombre());
		this.setCodigoPostal(municipioAux.getCodigoPostal());
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long idMunicipioAux = Long.parseLong(myRequest.getParameter("id"));
			this.municipioService.remove(idMunicipioAux);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("municipioConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMunicipio");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idMunicipoAux = Long.parseLong(myRequest.getParameter("id"));
		MunicipioDto municipioAux = this.municipioService.find(idMunicipoAux);
		this.setId(municipioAux.getId());
		this.setNombre(municipioAux.getNombre());
		this.setIdDepto(municipioAux.getDepartamento().getId());
		this.setCodigoPostal(municipioAux.getCodigoPostal());
	}

	@Override
	public String guardarEdicion() {
		try {
			this.municipioService.create(new MunicipioDto(this.getId(), this.getNombre().toUpperCase(),
					this.getCodigoPostal(), this.departamentoService.find(this.getIdDepto()), true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMunicipio");
			this.setMsgSuccessError("Municipio editado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMunicipio");
		}
		return this.getResultado();
	}

	public void cargarSelectItems() {
		Integer pageSize = this.departamentoService.count(DepartamentoSpecs.getByEstado(true)).intValue();
		List<DepartamentoDto> lstDepto = this.departamentoService.findAll(DepartamentoSpecs.getByEstado(true),
				PageRequest.of(0, pageSize == 0 ? 1 : pageSize));
		if (!(lstDepto.isEmpty())) {
			for (DepartamentoDto departamentoAux : lstDepto) {
				selectItems.add(new SelectItem(departamentoAux.getId(), departamentoAux.getNombre()));
			}
		}
	}

	@Override
	public void actualizar() {
		this.setIdDeptoBsq(null);
		this.setNombreBsq(null);
		this.setCodigoPostalBsq(null);
		super.actualizar();
	}

	@Override
	public LazyDataModel<MunicipioDto> getListElements() {
		LazyDataModel<MunicipioDto> userDataList = new LazyDataModel<MunicipioDto>() {
			private static final long serialVersionUID = -4501587204208399476L;

			@Override
			public List<MunicipioDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<MunicipioDto> result = new ArrayList<MunicipioDto>();

				if (first == 0) {
					result = municipioService.findAll(MunicipioSpecs.getMunicipioByDepartamento(getIdDeptoBsq())
							.and(MunicipioSpecs.getMunicipioByNombre(getNombreBuscar()))
							.and(MunicipioSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = municipioService.findAll(MunicipioSpecs.getMunicipioByDepartamento(getIdDeptoBsq())
							.and(MunicipioSpecs.getMunicipioByNombre(getNombreBuscar()))
							.and(MunicipioSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				}

				return result;
			}


		};
		userDataList.setRowCount(municipioService.count(MunicipioSpecs.getMunicipioByDepartamento(getIdDeptoBsq())
				.and(MunicipioSpecs.getMunicipioByNombre(getNombreBuscar())).and(MunicipioSpecs.getByEstado(true)))
				.intValue());
		return userDataList;
	}

}
