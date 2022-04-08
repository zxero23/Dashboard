package org.ccpm.dashboard.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.ccpm.dashboard.dtos.MenuDto;
import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.services.EmailService;
import org.ccpm.dashboard.services.UsuarioService;
import org.ccpm.dashboard.utilities.Utilidad;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Named
@SessionScoped
public class WebMB extends UtilMB implements Serializable {

	private static final long serialVersionUID = -3727342589028832013L;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	EmailService emailService;

	private String nombreUsuario;
	private String claveAcceso;
	private String resultado;
	private String msgSuccessError;
	private String title;
	private String images;
	private UsuarioDto usuario;
	private List<MenuDto> menu;
	private String rol;
	private String claveNueva;
	private String claveConfirma;
	private String usuarioRecuperarClave;
	private String correoRecuperarClave;
	private static final Logger logger = LoggerFactory.getLogger(WebMB.class);

	public String login() {
		try {
			this.removeSessionBean(false);
			UsuarioDto usuarioDto = usuarioService.login(this.getNombreUsuario().toLowerCase(),
					this.getClaveAcceso());
			this.setUsuario(usuarioDto);
			this.cargarMenu(usuarioDto);
			this.setRol(usuarioDto.getRol().getNombre());
			this.setMsgSuccessError("");
			this.setResultado("administracion");
		} catch (Exception ex) {
			this.setMsgSuccessError(ex.getMessage());
			this.addMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
		}
		return resultado;
	}

	public void removeSessionBean(boolean isLogout) {
		{
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("accionMB");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("menuMB");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("grupoMB");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuarioMB");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("expedienteMB");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("mesaMB");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("permisoMB");

			if (isLogout) {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webMB");
			}
		}
	}

	public void logout() {
		this.setNombreUsuario(null);
		this.setClaveAcceso(null);
		this.setUsuario(null);
		this.removeSessionBean(true);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	@Override
	public void limpiar() {
		this.setNombreUsuario(null);
		this.setClaveAcceso(null);
		this.setCorreoRecuperarClave(null);
		this.setUsuarioRecuperarClave(null);
	}

	public void cargarMenu(UsuarioDto usuario) {
		this.setMenu(new ArrayList<>());
		if (usuario.getRol() != null) {
			menu.addAll(usuario.getRol().getMenus());
		}
	}

	@Override
	public List<SelectItem> getSelectItems() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String crear() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String crearOtro() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void verDetalle() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void guardarBorrado() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void prepararParaEditar() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String guardarEdicion() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public StreamedContent fotoImage(String foto) {
		StreamedContent fotoRetorno = null;
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			fotoRetorno = new DefaultStreamedContent();
		} else {
			try {
				Path path = Paths.get(foto);
				File file = new File(foto);
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
	public LazyDataModel<?> getListElements() {
		return null;
	}

	public String recuperarClave() {
		String resultado = "recuperarClave";
		try {
			if (this.getCorreoRecuperarClave().isBlank() || this.getUsuarioRecuperarClave().isBlank()) {
				throw new Exception("Debe ingresar los datos de correo y usuario.");
			}
			UsuarioDto usuarioAux = usuarioService.findByUsuario(this.getUsuarioRecuperarClave());
			if (usuarioAux != null) {
				if (usuarioAux.getPersona().getCorreo() != null && !usuarioAux.getPersona().getCorreo().isBlank()) {
					if (usuarioAux.getPersona().getCorreo().equals(this.getCorreoRecuperarClave())) {
                           String randomPassword = Utilidad.generateRandomPassword(10);
                           usuarioAux.setClaveAcceso(randomPassword);
                           this.usuarioService.edit(usuarioAux);
                           /* Se envia el correo */ 
                           String asunto = "Reestablecimiento de clave de: " + usuarioAux.getPersona().getApellidoNombreRazonSocial();
                           StringBuilder mensaje = new StringBuilder();
                           Calendar c = Calendar.getInstance();
                           int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                           if(timeOfDay >= 0 && timeOfDay < 12){
                        	   mensaje.append("Buen día ");        
                        	}else if(timeOfDay >= 12 && timeOfDay < 16){
                        		mensaje.append("Buenas tardes "); 
                        	}else if(timeOfDay >= 16 && timeOfDay < 21){
                        		mensaje.append("Buenas tardes "); 
                        	}else if(timeOfDay >= 21 && timeOfDay < 24){
                        		mensaje.append("Buenas noches "); 
                        	}
                            mensaje.append("estimado ").append(usuarioAux.getPersona().getApellidoNombreRazonSocial()).append(": <br/>");
                            mensaje.append("<br/>");
                            mensaje.append("Le enviamos a continuación la clave provisoria para que pueda acceder al sistema. <br/>"); 
                            mensaje.append("<br/>");
                            mensaje.append("Clave provisoria: ").append(randomPassword).append("<br/>");
                            mensaje.append("<br/>");
                            mensaje.append("Link del sistema: ").append("https://www.dirpuertos.misiones.gov.ar/").append("<br/>");
                            mensaje.append("<br/>");
                            mensaje.append("Saludos cordiales, <br/>");
                            mensaje.append("Centro de Cómputos Misiones. <br/>");
                            this.emailService.enviarEmail(usuarioAux.getPersona().getCorreo(), asunto, mensaje.toString(), new ArrayList<>());
                            resultado = "index";
                			this.addMessage(FacesMessage.SEVERITY_INFO, "Mail Enviado", "Le hemos enviado la clave provisoria a su correo.");
                            
					} else {
						throw new Exception("El correo ingresado no coincide con el del usuario.");
					}

				} else {
					throw new Exception("El usuario no tiene correo asignado, por favor comuníquese con sistemas del Centro de Cómputos..");
				}
			} else {
				throw new RecordNotFoundException("Usuario no existe en el sistema.");
			}
		} catch (Exception e) {
			this.setMsgSuccessError(e.getMessage());
			this.addMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
		}
		return resultado;
	}
	
	
}
