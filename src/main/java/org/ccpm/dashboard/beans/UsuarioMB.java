package org.ccpm.dashboard.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.ccpm.dashboard.dtos.ConfiguracionDto;
import org.ccpm.dashboard.dtos.MunicipioDto;
import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.dtos.PersonaDto;
import org.ccpm.dashboard.dtos.RolDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.services.ConfiguracionService;
import org.ccpm.dashboard.services.MunicipioService;
import org.ccpm.dashboard.services.RolService;
import org.ccpm.dashboard.services.UsuarioService;
import org.ccpm.dashboard.specifications.MunicipioSpecs;
import org.ccpm.dashboard.specifications.RolSpecs;
import org.ccpm.dashboard.specifications.UsuarioSpecs;
import org.ccpm.dashboard.utilities.EncriptadorAES;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Named
@SessionScoped
public class UsuarioMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = -5429249399286312626L;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private RolService rolService;
	@Autowired
	private MunicipioService municipioService;
	@Autowired
	private ConfiguracionService configuracionService;

	@Value("${secretKey}")
	private String secretKey;

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuarioMB.class);

	private UsuarioDto usuario;

	private List<SelectItem> selectItemsMunicipio = new ArrayList<SelectItem>();
	private List<SelectItem> selectItemsRol  = new ArrayList<SelectItem>();

	private UsuarioDto usuarioAux;
	private String claveAcceso;
	private String apellidoNombre;
	private Date fechaNacimiento;
	private String domicilio;
	private String nacionalidad;
	private String telefono;
	private Long idMunicipio;
	private String nombreUsuario;
	private String ussViejo;
	private String correo;
	private Long documento;
	private Long idMesa;
	private String foto;
	private Long idRol;
	private List<Long> idItems = new ArrayList<>();
	
	/*Atributos cambio de clave*/
	private String claveAnteriorRegistrada;
    private String claveAnterior;
    private String claveNueva;
    private String claveNuevaRepetir;
	
	private String apellidoNombreBsq;
	private String nombreUsuarioBsq;
	private Long idRolBsq;

	public UsuarioMB() {
	}

	@PostConstruct
	private void init() {
		WebMB sessionBean = this.getSessionBean();
		if (sessionBean != null) {
			this.setUsuario(sessionBean.getUsuario());
			try {
				this.prepararPermisos(sessionBean.getUsuario());
			} catch (Exception e) {
			}
		}
		this.loadMunicipios();
		this.loadRoles();
	}

	public List<Long> getIdItems() {
		return idItems;
	}

	public void setIdItems(List<Long> idItems) {
		this.idItems = idItems;
	}

	public void prepararPermisos(UsuarioDto usuario) throws Exception {
		try {
			if (usuario.getRol() != null) {
				for (PermisoDto permiso : usuario.getRol().getPermisos()) {
					if (permiso.getNombre().equalsIgnoreCase("nuevoUsuario")) {
						this.setAlta(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("editarUsuario")) {
						this.setModificacion(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("borrarUsuario")) {
						this.setBaja(true);
					}
					if (permiso.getNombre().equalsIgnoreCase("detalleUsuario")) {
						this.setDetalle(true);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("Error al recuperar las permisos del usuario");
		}
	}

	@Override
	public void actualizar() {
		super.actualizar();
		this.setApellidoNombreBsq(null);
		this.setNombreBuscar(null);
		this.setNombreUsuarioBsq(null);
		this.setIdRolBsq(0L);
		this.setDocumento(null);
	}

	@Override
	public void limpiar() {
		this.setClaveAcceso(null);
		this.setApellidoNombre(null);
		this.setDocumento(null);
		this.setNombreUsuario(null);
		this.setUssViejo(null);
		this.setCorreo(null);
		this.setDomicilio(null);
		this.setTelefono(null);
		this.setIdMunicipio(null);
		this.setFechaNacimiento(null);
		this.setNacionalidad(null);
		this.setIdMesa(null);
		this.setIdItems(new ArrayList<>());
		this.setUsuarioAux(null);
		this.setFoto(null);
	}

	@Override
	public String crear() {
		try {
			UsuarioDto usuario = new UsuarioDto();
			usuario.setPersona(new PersonaDto());
			MunicipioDto municipio = this.municipioService.find(this.getIdMunicipio());
			usuario.getPersona().setApellidoNombreRazonSocial(this.getApellidoNombre().toUpperCase());
			usuario.getPersona().setCorreo(this.getCorreo());
			usuario.getPersona().setDocumento(this.getDocumento());
			usuario.getPersona().setFechaNacimiento(this.getFechaNacimiento());
			usuario.getPersona().setDomicilio(this.getDomicilio());
			usuario.getPersona().setNacionalidad(this.getNacionalidad());
			usuario.getPersona().setTelefono(this.getTelefono());
			usuario.getPersona().setMunicipio(municipio);
			usuario.setFoto(this.getFoto());
			usuario.setFechaAlta(new Date());
			usuario.setEstado(true);
			usuario.setNombreUsuario(this.getNombreUsuario());
			usuario.setClaveAcceso(this.getClaveAcceso());
			usuario.setRol(this.rolService.find(idRol));
			this.usuarioService.create(usuario);
			this.limpiar();
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoUsuario");
			this.setMsgSuccessError("Usuario creado correctamente.");
			this.limpiar();
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoUsuario");
			this.limpiar();
		}
		return this.getResultado();
	}

	@Override
	public String crearOtro() {
		try {
			UsuarioDto usuario = new UsuarioDto();
			usuario.setPersona(new PersonaDto());
			MunicipioDto municipio = this.municipioService.find(this.getIdMunicipio());
			usuario.getPersona().setApellidoNombreRazonSocial(this.getApellidoNombre().toUpperCase());
			usuario.getPersona().setCorreo(this.getCorreo());
			usuario.getPersona().setDocumento(this.getDocumento());
			usuario.getPersona().setFechaNacimiento(this.getFechaNacimiento());
			usuario.getPersona().setDomicilio(this.getDomicilio());
			usuario.getPersona().setNacionalidad(this.getNacionalidad());
			usuario.getPersona().setTelefono(this.getTelefono());
			usuario.getPersona().setMunicipio(municipio);
			usuario.setFoto(this.getFoto());
			usuario.setFechaAlta(new Date());
			usuario.setEstado(true);
			usuario.setNombreUsuario(this.getNombreUsuario());
			usuario.setClaveAcceso(this.getClaveAcceso());
			usuario.setRol(this.rolService.find(idRol));
			this.usuarioService.create(usuario);
			this.setResultado("nuevoUsuario");
			this.setUsuarioAux(null);
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoUsuario");
		}
		return this.getResultado();
	}

	@Override
	public void verDetalle() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idUsuarioAux = Long.parseLong(myRequest.getParameter("id"));
		UsuarioDto usuarioAux = this.usuarioService.find(idUsuarioAux);
		this.setUsuarioAux(usuarioAux);

	}

	@Override
	public void guardarBorrado() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
			Long idUsuarioAux = Long.parseLong(myRequest.getParameter("id"));
			this.usuarioService.remove(idUsuarioAux);
			this.limpiar();
			this.setResultado("usuarioConf");
		} catch (Exception ex) {
			this.limpiar();
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoUsuario");
		}
	}

	@Override
	public void prepararParaEditar() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
		Long idUsuarioAux = Long.parseLong(myRequest.getParameter("id"));
		UsuarioDto usuarioAux = this.usuarioService.find(idUsuarioAux);
		this.setId(usuarioAux.getId());
		this.setNombreUsuario(usuarioAux.getNombreUsuario());
		this.setUssViejo(usuarioAux.getNombreUsuario());
		this.setCorreo(usuarioAux.getPersona().getCorreo());
		this.setApellidoNombre(usuarioAux.getPersona().getApellidoNombreRazonSocial());
		EncriptadorAES encriptadorAES = new EncriptadorAES();
		try {
			this.setClaveAcceso(encriptadorAES.desencriptar(usuarioAux.getClaveAcceso(), secretKey));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDocumento(usuarioAux.getPersona().getDocumento());
		this.setFoto(usuarioAux.getFoto());
		this.setCorreo(usuarioAux.getPersona().getDomicilio());
		this.setDomicilio(usuarioAux.getPersona().getDomicilio());
		this.setFechaNacimiento(usuarioAux.getPersona().getFechaNacimiento());
		this.setIdMunicipio(usuarioAux.getPersona().getMunicipio().getId());
		this.setNacionalidad(usuarioAux.getPersona().getNacionalidad());
		this.setTelefono(usuarioAux.getPersona().getTelefono());
		if (usuarioAux.getRol() != null) {
			this.setIdRol(usuarioAux.getRol().getId());
		}
	}

	@Override
	public String guardarEdicion() {
		try {
			UsuarioDto usuario = this.usuarioService.find(this.getId());
			usuario.setPersona(new PersonaDto());
			MunicipioDto municipio = this.municipioService.find(this.getIdMunicipio());
			usuario.getPersona().setApellidoNombreRazonSocial(this.getApellidoNombre().toUpperCase());
			usuario.getPersona().setCorreo(this.getCorreo());
			usuario.getPersona().setDocumento(this.getDocumento());
			usuario.getPersona().setFechaNacimiento(this.getFechaNacimiento());
			usuario.getPersona().setDomicilio(this.getDomicilio());
			usuario.getPersona().setNacionalidad(this.getNacionalidad());
			usuario.getPersona().setTelefono(this.getTelefono());
			usuario.getPersona().setMunicipio(municipio);
			usuario.setFoto(this.getFoto());
			usuario.setFechaAlta(new Date());
			usuario.setEstado(true);
			usuario.setNombreUsuario(this.getNombreUsuario());
			usuario.setClaveAcceso(this.getClaveAcceso());
			usuario.setRol(this.rolService.find(idRol));
			this.usuarioService.edit(usuario);
			this.limpiar();
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("resultadoUsuario");
			this.setMsgSuccessError("Usuario editado correctamente.");
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("resultadoUsuario");
		}
		return this.getResultado();
	}

	@Override
	public List<SelectItem> getSelectItems() {
		return null;
	}

	private void loadRoles() {
		try {
			List<RolDto> roles = rolService.findAll(RolSpecs.getByEstado(true),
					PageRequest.of(0, rolService.count(RolSpecs.getByEstado(true)).intValue()));
			if (!(roles.isEmpty())) {
				for (RolDto rol : roles) {
					selectItemsRol.add(new SelectItem(rol.getId(), rol.getNombre()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadMunicipios() {
		try {
			Integer pageSize = this.municipioService.count(MunicipioSpecs.getByEstado(true)).intValue();
			List<MunicipioDto> lstDepto = this.municipioService.findAll(MunicipioSpecs.getByEstado(true),
					PageRequest.of(0, pageSize == 0 ? 1 : pageSize));
			if (!(lstDepto.isEmpty())) {
				for (MunicipioDto municipioAux : lstDepto) {
					selectItemsMunicipio.add(new SelectItem(municipioAux.getId(), municipioAux.getNombre()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void validarEmail(FacesContext context, UIComponent validate, Object value) {
		String email = (String) value;

		if (email.indexOf('@') == -1) {
			((UIInput) validate).setValid(false);
			FacesMessage msg = new FacesMessage("Correo electrónico inválido.");
			context.addMessage(validate.getClientId(context), msg);
		}
	}

	public void subirArchivo(FileUploadEvent event) throws Exception {
		FacesMessage message = new FacesMessage("Información", event.getFile().getFileName() + " ha sido subido.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		try {
			this.setFoto(copyFile(event.getFile().getFileName(), event.getFile().getInputStream()));
		} catch (IOException e) {

		}

	}

	public String copyFile(String fileName, InputStream in) throws Exception {
		String path = "";
		try {
			ConfiguracionDto configuracion = configuracionService.findByLlave("path_archivos_usuarios");
			if (configuracion != null) {
				Files.createDirectories(Paths.get(configuracion.getValor()));
				OutputStream out = new FileOutputStream(new File(configuracion.getValor() + fileName));
				path = configuracion.getValor() + fileName;
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}

				in.close();
				out.flush();
				out.close();
			} else {

			}
		} catch (IOException e) {

		}
		return path;
	}

	public StreamedContent getFotoImage() {
		StreamedContent fotoRetorno = null;
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			fotoRetorno = new DefaultStreamedContent();
		} else {
			try {
				Path path = Paths.get(this.getFoto());
				File file = new File(this.getFoto());
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				byte[] data = Files.readAllBytes(path);
				return DefaultStreamedContent.builder().contentType(mimeType).stream(() -> {
					try {
						return new ByteArrayInputStream(data);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}).build();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
		return (fotoRetorno);
	}

	@Override
	public LazyDataModel<UsuarioDto> getListElements() {
		LazyDataModel<UsuarioDto> userDataList = new LazyDataModel<UsuarioDto>() {
			private static final long serialVersionUID = -4501587204208399476L;

			@Override
			public List<UsuarioDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				List<UsuarioDto> result = new ArrayList<UsuarioDto>();
				if (first == 0) {
					result = usuarioService
							.findAll(
									UsuarioSpecs.getByEstado(true)
											.and(UsuarioSpecs.getUsuarioByApellidoNombre(getApellidoNombreBsq()))
											.and(UsuarioSpecs.getUsuarioByNombreUsuario(getNombreUsuarioBsq()))
											.and(UsuarioSpecs.getUsuarioByRol(getIdRolBsq())),
									PageRequest.of(first, pageSize));
				} else {
					first = first / pageSize;
					result = usuarioService
							.findAll(UsuarioSpecs.getByEstado(true)
											.and(UsuarioSpecs.getUsuarioByApellidoNombre(getApellidoNombreBsq()))
											.and(UsuarioSpecs.getUsuarioByNombreUsuario(getNombreUsuarioBsq()))
											.and(UsuarioSpecs.getUsuarioByRol(getIdRolBsq())),
									PageRequest.of(first, pageSize));
				}

				return result;
			}


		};
		userDataList.setRowCount(usuarioService.count(UsuarioSpecs.getByEstado(true).and(UsuarioSpecs.getUsuarioByApellidoNombre(getApellidoNombreBsq()))
				.and(UsuarioSpecs.getUsuarioByNombreUsuario(getNombreUsuarioBsq()))
				.and(UsuarioSpecs.getUsuarioByRol(getIdRolBsq()))).intValue());
		return userDataList;
	}
	
	public void prepararParaCambiarClave() {
		EncriptadorAES encriptadorAES = new EncriptadorAES();
		try {
			this.setClaveAnterior(null);
			this.setClaveNueva(null);
			this.setClaveNuevaRepetir(null);
			this.setClaveAnteriorRegistrada(encriptadorAES.desencriptar(usuario.getClaveAcceso(), secretKey));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String guardarCambioClave() {
		try {
			UsuarioDto usuario = this.getUsuario();
			EncriptadorAES encriptadorAES = new EncriptadorAES();
			if(!this.getClaveAnterior().equals(this.getClaveAnteriorRegistrada()) || !this.getClaveNueva().equals(this.getClaveNuevaRepetir())) {
				throw new Exception("No se cumplen la condiciones necesarias para el cambio de clave.");
			}
			usuario.setClaveAcceso(encriptadorAES.encriptar(this.getClaveNueva(), secretKey));
			usuario = this.usuarioService.edit(usuario);
			this.setUsuario(usuario);
			this.limpiar();
			this.setTitle("Información");
			this.setImages("fa fa-check");
			this.setResultado("index");
			this.setMsgSuccessError("Clave cambiada correctamente.");
			this.addMessage(FacesMessage.SEVERITY_INFO, "Éxito!", this.getMsgSuccessError());
		} catch (Exception ex) {
			this.setTitle("Error");
			this.setImages("fa fa-times");
			this.setMsgSuccessError(ex.getMessage());
			this.addMessage(FacesMessage.SEVERITY_INFO, "Éxito!", this.getMsgSuccessError());
			this.setResultado("administracion");
		}
		return this.getResultado();
	}
	
	public void validarClaveAnterior(FacesContext context, UIComponent validate, Object value) {
		String claveNuevaR = (String) value;

		if (this.getClaveAnteriorRegistrada()!=null && claveNuevaR!=null && !claveNuevaR.equals(this.getClaveAnteriorRegistrada()) ) {
			((UIInput) validate).setValid(false);
			FacesMessage msg = new FacesMessage("Las clave ingresada no es válida.");
			context.addMessage(validate.getClientId(context), msg);
		}
	}
	
	public void validarClaveNuevaRepetir(FacesContext context, UIComponent validate, Object value) {
		String claveNuevaR = (String) value;

		if (this.getClaveNueva()!=null && claveNuevaR!=null && !claveNuevaR.equals(this.getClaveNueva()) ) {
			((UIInput) validate).setValid(false);
			FacesMessage msg = new FacesMessage("Las claves no coinciden.");
			context.addMessage(validate.getClientId(context), msg);
		}
	}
}
