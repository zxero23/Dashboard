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

import org.ccpm.dashboard.dtos.ConfiguracionDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.ConfiguracionService;
import org.ccpm.dashboard.specifications.ConfiguracionSpecs;
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
public class ConfiguracionMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = -544737723344023229L;
	@Autowired
	private ConfiguracionService configuracionService;
	private String llave;
	private String valor;
	private String llaveBsq;
	private String valorBsq;

	public ConfiguracionMB() {
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
					if (permiso.getNombre().equalsIgnoreCase("nuevaConfiguracion")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarConfiguracion")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarConfiguracion")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleConfiguracion")) {
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
		this.setLlave(null);
		this.setValor(null);
	}

	@Override
	public String crear() {
		try {
			this.configuracionService.create(new ConfiguracionDto(null, this.getLlave(), this.getValor(), true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoConfiguracion");
			this.setMsgSuccessError("Configuración creada correctamente.");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoConfiguracion");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			this.configuracionService.create(new ConfiguracionDto(null, this.getLlave(), this.getValor(), true));
			this.setResultado("nuevaConfiguracion");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoConfiguracion");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idConfiguracionAux = Long.parseLong(myRequest.getParameter("id"));
		ConfiguracionDto configuracionAux = this.configuracionService.find(idConfiguracionAux);
		this.setId(configuracionAux.getId());
		this.setLlave(configuracionAux.getLlave());
		this.setValor(configuracionAux.getValor());
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long idConfiguracionAx = Long.parseLong(myRequest.getParameter("id"));
			this.configuracionService.remove(idConfiguracionAx);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("configuracionConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoConfiguracion");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idConfiguracionAux = Long.parseLong(myRequest.getParameter("id"));
		ConfiguracionDto configuracionAux = this.configuracionService.find(idConfiguracionAux);
		this.setId(configuracionAux.getId());
		this.setLlave(configuracionAux.getLlave());
		this.setValor(configuracionAux.getValor());
	}

	@Override
	public String guardarEdicion() {
		try {
			this.configuracionService.edit(new ConfiguracionDto(this.getId(), this.getLlave(), this.getValor(), true));
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoConfiguracion");
			this.setMsgSuccessError("Configuraci�n editada correctamente.");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoConfiguracion");
		}
		return this.getResultado();
	}

	@Override
	public List<SelectItem> getSelectItems() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void actualizar() {
		this.setLlaveBsq(null);
		this.setValorBsq(null);
		super.actualizar();
	}

	@Override
	public LazyDataModel<ConfiguracionDto> getListElements() {
		LazyDataModel<ConfiguracionDto> userDataList = new LazyDataModel<ConfiguracionDto>() {
			private static final long serialVersionUID = -4501587204208399476L;

			@Override
			public List<ConfiguracionDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<ConfiguracionDto> result = new ArrayList<ConfiguracionDto>();

				if (first == 0) {
					result = configuracionService.findAll(ConfiguracionSpecs.getConfiguracionByLLave(getLlaveBsq())
							.and(ConfiguracionSpecs.getConfiguracionByValor(getValorBsq()))
							.and(ConfiguracionSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = configuracionService.findAll(ConfiguracionSpecs.getConfiguracionByLLave(getLlaveBsq())
							.and(ConfiguracionSpecs.getConfiguracionByValor(getValorBsq()))
							.and(ConfiguracionSpecs.getByEstado(true)), PageRequest.of(first, pageSize));
				}

				return result;
			}

		};
		userDataList.setRowCount(configuracionService.count(ConfiguracionSpecs.getConfiguracionByLLave(getLlaveBsq())
				.and(ConfiguracionSpecs.getConfiguracionByValor(getValorBsq()))
				.and(ConfiguracionSpecs.getByEstado(true))).intValue());
		return userDataList;
	}

}
