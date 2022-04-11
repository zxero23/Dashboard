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

import org.ccpm.dashboard.dtos.MenuDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.RolDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.MenuService;
import org.ccpm.dashboard.services.PermisoService;
import org.ccpm.dashboard.services.RolService;
import org.ccpm.dashboard.specifications.MenuSpecs;
import org.ccpm.dashboard.specifications.PermisoSpecs;
import org.ccpm.dashboard.specifications.RolSpecs;
import org.primefaces.model.DualListModel;
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
public class RolMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = -1589446212645090290L;
	@Autowired
	private RolService rolService;
	@Autowired
	private PermisoService permisoService;
	@Autowired
	private MenuService menuService;
	private String nombre;
	private String descripcion;
	private LazyDataModel<RolDto> filteredUsers = null;
	private DualListModel<PermisoDto> dualListPermisos = new DualListModel<>();
	private DualListModel<MenuDto> dualListMenus = new DualListModel<>();

	public RolMB() {
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
					if (permiso.getNombre().equalsIgnoreCase("nuevoRol")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarRol")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarRol")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleRol")) {
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
		this.setDescripcion(null);
		List<PermisoDto> permisos = this.permisoService.findAll(PermisoSpecs.getByEstado(true),
				PageRequest.of(0, this.permisoService.count(PermisoSpecs.getByEstado(true)).intValue()));
		this.dualListPermisos = new DualListModel<>(permisos, new ArrayList<>());
		
		List<MenuDto> menus = this.menuService.findAll(MenuSpecs.getByEstado(true),
				PageRequest.of(0, this.menuService.count(MenuSpecs.getByEstado(true)).intValue()));
		this.dualListMenus = new DualListModel<>(menus,  new ArrayList<>());
		
	}

	@Override
	public String crear() {
		try {
			RolDto rolDto = new RolDto();
			rolDto.setNombre(this.getNombre().toUpperCase());
			rolDto.setDescripcion(this.getDescripcion());
			rolDto.setPermisos(this.dualListPermisos.getTarget());
			rolDto.setEstado(true);
			rolDto.setMenus(this.dualListMenus.getTarget());
			this.rolService.create(rolDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoRol");
			this.setMsgSuccessError("Rol creado correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoRol");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			RolDto rolDto = new RolDto();
			rolDto.setNombre(this.getNombre().toUpperCase());
			rolDto.setDescripcion(this.getDescripcion());
			rolDto.setPermisos(this.dualListPermisos.getTarget());
			rolDto.setEstado(true);
			rolDto.setMenus(this.dualListMenus.getTarget());
			this.rolService.create(rolDto);
			this.setResultado("nuevoRol");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoRol");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(map.get("id").toString());
		RolDto rol = this.rolService.find(id);
		this.setId(rol.getId());
		this.setNombre(rol.getNombre());
		this.setDescripcion(rol.getDescripcion());
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long id = Long.parseLong(myRequest.getParameter("id"));
			this.rolService.remove(id);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("rolConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoRol");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long id = Long.parseLong(myRequest.getParameter("id"));
		RolDto rolAux = this.rolService.find(id);
		this.setId(rolAux.getId());
		this.setNombre(rolAux.getNombre());
		this.setDescripcion(rolAux.getDescripcion());
		List<PermisoDto> permisos = this.permisoService.findAll(PermisoSpecs.getByEstado(true),
				PageRequest.of(0, this.permisoService.count(PermisoSpecs.getByEstado(true)).intValue()));
		permisos.removeAll(rolAux.getPermisos());
		this.dualListPermisos = new DualListModel<>(permisos, rolAux.getPermisos());
		List<MenuDto> menus = this.menuService.findAll(MenuSpecs.getByEstado(true),
				PageRequest.of(0, this.menuService.count(MenuSpecs.getByEstado(true)).intValue()));
		menus.removeAll(rolAux.getMenus());
		this.dualListMenus = new DualListModel<>(menus, rolAux.getMenus());
	}

	@Override
	public String guardarEdicion() {
		try {
			RolDto rolDto = new RolDto();
			rolDto.setId(this.getId());
			rolDto.setNombre(this.getNombre().toUpperCase());
			rolDto.setDescripcion(this.getDescripcion());
			rolDto.setPermisos(this.dualListPermisos.getTarget());
			rolDto.setEstado(true);
			rolDto.setMenus(this.dualListMenus.getTarget());
			this.rolService.edit(rolDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoRol");
			this.setMsgSuccessError("Rol editado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoRol");
		}
		return this.getResultado();
	}

	@Override
	public LazyDataModel<RolDto> getListElements() {
		LazyDataModel<RolDto> userDataList = new LazyDataModel<RolDto>() {

			private static final long serialVersionUID = -4772421681175081281L;

			@Override
			public List<RolDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<RolDto> result = new ArrayList<RolDto>();

				if (first == 0) {
					result = rolService.findAll(
							RolSpecs.getRolByNombre(getNombreBuscar()).and(RolSpecs.getByEstado(true)),
							PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = rolService.findAll(
							RolSpecs.getRolByNombre(getNombreBuscar()).and(RolSpecs.getByEstado(true)),
							PageRequest.of(first, pageSize));
				}

				return result;
			}

		};
		userDataList.setRowCount(rolService
				.count(RolSpecs.getRolByNombre(getNombreBuscar()).and(RolSpecs.getByEstado(true))).intValue());
		return userDataList;
	}

	@Override
	public List<SelectItem> getSelectItems() {
		return null;
	}

	@Override
	public void actualizar() {
		super.actualizar();
		this.setNombreBuscar("");
	}

}
