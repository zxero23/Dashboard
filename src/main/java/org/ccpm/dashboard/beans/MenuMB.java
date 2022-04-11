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
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.MenuService;
import org.ccpm.dashboard.specifications.MenuSpecs;
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
public class MenuMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = 7948809639321025949L;
	@Autowired
	private MenuService menuService;
	private String nombre;
	private String icono;
	private String link;
	private String idMenuPadre;
	private Integer orden;

	public MenuMB() {
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
					if (permiso.getNombre().equalsIgnoreCase("nuevoMenu")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarMenu")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarMenu")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleMenu")) {
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
		this.setLink(null);
		this.setIcono(null);
		this.setNombreBuscar("");
	}

	@Override
	public String crear() {
		try {
			MenuDto menuDto = new MenuDto();
			menuDto.setNombre(this.getNombre().toUpperCase());
			menuDto.setLink(this.getLink());
			menuDto.setIcono(this.getIcono());
			menuDto.setEstado(true);
			menuDto.setOrden(this.getOrden());
			this.menuService.create(menuDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMenu");
			this.setMsgSuccessError("Menú creado correctamente");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMenu");
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			MenuDto menuDto = new MenuDto();
			menuDto.setNombre(this.getNombre().toUpperCase());
			menuDto.setLink(this.getLink());
			menuDto.setIcono(this.getIcono());
			menuDto.setEstado(true);
			menuDto.setOrden(this.getOrden());
			this.menuService.create(menuDto);
			this.setResultado("nuevaMenu");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMenu");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		Long id = Long.parseLong(map.get("id").toString());
		MenuDto menuDto = this.menuService.find(id);
		this.setId(menuDto.getId());
		this.setNombre(menuDto.getNombre());
		this.setLink(menuDto.getLink());
		this.setIcono(menuDto.getIcono());
		this.setOrden(menuDto.getOrden());
	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long id = Long.parseLong(myRequest.getParameter("id"));
			this.menuService.remove(id);
			this.setTitle(null);
			this.setImages(null);
			this.setMsgSuccessError(null);
			this.setResultado("menuConf");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMenu");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long id = Long.parseLong(myRequest.getParameter("id"));
		MenuDto menuDto = this.menuService.find(id);
		this.setId(menuDto.getId());
		this.setId(menuDto.getId());
		this.setNombre(menuDto.getNombre());
		this.setLink(menuDto.getLink());
		this.setIcono(menuDto.getIcono());
		this.setOrden(menuDto.getOrden());
	}

	@Override
	public String guardarEdicion() {
		try {
			MenuDto menuDto = new MenuDto();
			menuDto.setId(this.getId());
			menuDto.setNombre(this.getNombre().toUpperCase());
			menuDto.setLink(this.getLink());
			menuDto.setIcono(this.getIcono());
			menuDto.setEstado(true);
			menuDto.setOrden(this.getOrden());
			this.menuService.edit(menuDto);
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoMenu");
			this.setMsgSuccessError("Menú editado correctamente");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoMenu");
		}
		return this.getResultado();
	}

	@Override
	public LazyDataModel<MenuDto> getListElements() {
		LazyDataModel<MenuDto> userDataList = new LazyDataModel<MenuDto>() {
			private static final long serialVersionUID = -4501587204208399476L;

			@Override
			public List<MenuDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<MenuDto> result = new ArrayList<MenuDto>();

				if (first == 0) {
					result = menuService.findAll(
							MenuSpecs.getMenuByNombre(getNombreBuscar()).and(MenuSpecs.getByEstado(true)),
							PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = menuService.findAll(
							MenuSpecs.getMenuByNombre(getNombreBuscar()).and(MenuSpecs.getByEstado(true)),
							PageRequest.of(first, pageSize));
				}

				return result;
			}

		};
		userDataList.setRowCount(menuService
				.count(MenuSpecs.getMenuByNombre(getNombreBuscar()).and(MenuSpecs.getByEstado(true))).intValue());
		return userDataList;
	}

	@Override
	public List<SelectItem> getSelectItems() {
		List<SelectItem> selectItems = new ArrayList<>();
		List<MenuDto> lstMenu = this.menuService.findAll(MenuSpecs.getByEstado(true),
				PageRequest.of(0, this.menuService.count(MenuSpecs.getByEstado(true)).intValue()));
		if (!(lstMenu.isEmpty())) {
			for (MenuDto menuAux : lstMenu) {
				selectItems.add(new SelectItem(menuAux.getId(), menuAux.getNombre()));
			}
		}
		return selectItems;
	}

}
